package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.JsonUtils;
import com.leyou.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.reposiory.GoodsReposiory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecClient specClient;

    @Autowired
    private GoodsReposiory goodsReposiory;

    @Autowired
    private ElasticsearchTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    //把查询到的spu转化为goods
    public Goods buildGoods(Spu spu){

        //获取all(搜索条件：拼接：标题、分类、品牌)
        String title = spu.getTitle(); //标题
        List<String> names = categoryClient.queryNameByCategoryIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3())); //分类
        String brandName = brandClient.findBrand(spu.getBrandId()).getName();  //品牌


        //获取price
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        List<Map<String,Object>> skuList = new ArrayList<>();
        List<Long> price = new ArrayList<>();
        for(Sku sku : skus){
            price.add(sku.getPrice());

            //处理一下sku的字段，我们只需要部分字段
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("image", StringUtils.substringBefore(sku.getImages(),","));
            map.put("price",sku.getPrice());
            skuList.add(map);
        }

        //获取specs
        List<SpecParam> params = specClient.querySpecParams(null,spu.getCid3(),true,null);

        SpuDetail detail = goodsClient.querySpuDetailById(spu.getId());

        //处理规格参数
        Map<Long,String> genericMap = JsonUtils.parseMap(detail.getGenericSpec(), Long.class, String.class);
        Map<Long,List<String>> specialMap = JsonUtils.nativeRead(detail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
        });

        Map<String, Object> specs = new HashMap<>();

        for(SpecParam param : params){
            if(param.getGeneric()){
                //通用参数
                String value = genericMap.get(param.getId());
                if(param.getNumeric()){
                    //数值类型，需要存储一个分段
                    value = this.chooseSegment(value,param);
                }
                specs.put(param.getName(),value);
            }else{
                //特有参数
                specs.put(param.getName(),specialMap.get(param.getId()));
            }
        }


        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setAll(title + " " + StringUtils.join(names," ") + " " +brandName);
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(price);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);


        return goods;
    }


    private String chooseSegment(String value, SpecParam param) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : param.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + param.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + param.getUnit() + "以下";
                } else {
                    result = segment + param.getUnit();
                }
                break;
            }
        }
        return result;
    }


    public PageResult<Goods> search(SearchRequest request) {

        String key = request.getKey();

        //判断是否有搜索条件，如果没有，直接返回null，不允许搜索全部商品
        if(StringUtils.isBlank(key)){
            return null;
        }


        //1 创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //2 查询
        //2.1对结果进行过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[] {"id","skus","subTitle"},null));

        //2.2基本查询
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all",key);
        QueryBuilder basicQuery = buildBaseQuery(request);
        queryBuilder.withQuery(basicQuery);

        //2.3分页排序
        searchWithPageAndSort(queryBuilder,request);

        //2.4聚合
        String categoryAggName = "category_agg"; //商品分类聚合名称
        String brandAggName = "brand_agg"; //品牌聚合名称
        //对商品分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //3 查询、返回结果
        //AggregatedPage<Goods> pageInfo = (AggregatedPage<Goods>) goodsReposiory.search(queryBuilder.build());
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        //4 解析结果
        //4.1分页信息
        long total = result.getTotalElements(); //总条数
        //Integer totalPage = result.getTotalPages(); //总页数 -->前台显示为1，未知错误
        Long totalPage = (total + request.getSize() -1) / request.getSize();

        //4.2商品分类的聚合结果
        List<Category> categories = getCateoryAggResult(result.getAggregation(categoryAggName));
        //4.3品牌的聚合结果
        List<Brand> brands = getBrandAggResult(result.getAggregation(brandAggName));

        //判断商品分类数量，看是否需要对规格参数进行聚合
        List<Map<String,Object>> specs = null;
        if(categories.size() == 1){
            //如果商品分类只剩下1个，那么进行聚合操作
            specs = buildSpecsAgg(categories.get(0).getId(), basicQuery );
        }

        return new SearchResult(total,totalPage.intValue(),result.getContent(),categories,brands,specs);
    }

    private QueryBuilder buildBaseQuery(SearchRequest request) {
        //创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        //过滤条件
        Map<String, String> map = request.getFilter();
        for(Map.Entry<String,String> entry : map.entrySet()){
            String key = entry.getKey();
            //处理key
            if(!"cid3".equals(key) && !"brandId".equals(key)){
                key = "specs."+key+".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return queryBuilder;
    }

    //聚合规格参数
    private List<Map<String, Object>> buildSpecsAgg(Long cid, QueryBuilder baseQuery) {
        try{
            //根据分类查询规格
            List<SpecParam> params = this.specClient.querySpecParams(null,cid,true, null);
            //创建集合，保存规格的过滤条件
            List<Map<String,Object>> specs = new ArrayList<>();
            //创建查询构造器
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(baseQuery);

            //聚合规格参数
            for(SpecParam param : params) {
                String name = param.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
                //System.out.println(name);
            }

            //查询
            AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
            //解析聚合结果
            Aggregations aggs = result.getAggregations();
            for(SpecParam param : params) {
                String name = param.getName();
                StringTerms terms = aggs.get(name);
                //System.out.println(terms.getName());
                List<String> options = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());

                Map<String,Object> map = new HashMap<>();
                map.put("k",name);
                map.put("options",options);
                specs.add(map);
                //System.out.println(specs.size());
            }
            return specs;
        }catch(Exception e){
            logger.error("聚合规格参数出错: ",e);
            return null;
        }
    }

    //解析商品分类聚合结果
    private List<Category> getCateoryAggResult(Aggregation aggregation){
        try{
            LongTerms terms = (LongTerms) aggregation;
            List<Long> ids = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categories = categoryClient.queryByCategoryIds(ids);
            return categories;
        } catch (Exception e){
            logger.error("解析商品分类聚合结果出错: ",e);
            return null;
        }
    }

    //解析商品品牌聚合结果
    private List<Brand> getBrandAggResult(Aggregation aggregation){
        try{
            LongTerms brandAgg = (LongTerms) aggregation;
            List<Long> bids = new ArrayList<>();
            for(LongTerms.Bucket bucket : brandAgg.getBuckets()){
                bids.add(bucket.getKeyAsNumber().longValue());
            }
            //根据id查询商品品牌名称
            List<Brand> brands = brandClient.queryBrandByIds(bids);
            return brands;
        }catch (Exception e){
            logger.error("解析商品品牌聚合结果出错： ",e);
            return null;
        }
    }


    //封装分页排序查询
    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder,SearchRequest request){
        //准备分页参数
        int page = request.getPage()-1; //page从0开始
        int size = request.getSize();
        //分页
        queryBuilder.withPageable(PageRequest.of(page,size));

        //排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if(StringUtils.isNotBlank(sortBy)){
            //如果不为空，则进行排序
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
    }

    public List<Category> findCategoryByCid3(Long cid3) {
        List<Category> categories = categoryClient.queryCategoriesByCid3(cid3);
        return categories;
    }
}
