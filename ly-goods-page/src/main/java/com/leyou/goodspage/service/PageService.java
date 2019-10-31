package com.leyou.goodspage.service;

import com.leyou.goodspage.client.BrandClient;
import com.leyou.goodspage.client.CategoryClient;
import com.leyou.goodspage.client.GoodsClient;
import com.leyou.goodspage.client.SpecClient;
import com.leyou.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;

    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    public Map<String, Object> loadModel(Long id){

        try{
            //模型数据
            Map<String,Object> modelMap = new HashMap<>();

            //spu
            Spu spu = goodsClient.querySpuById(id);
            modelMap.put("spu",spu);

            //spuDetail
            SpuDetail spuDetail = goodsClient.querySpuDetailById(id);
            modelMap.put("spuDetail",spuDetail);

            //sku
            List<Sku> skus = goodsClient.querySkuBySpuId(id);
            modelMap.put("skus",skus);

            //商品分类
            List<Category> categories = getCategories(spu);
            if(categories != null){
                modelMap.put("categories",categories);
            }

            //品牌数据
            List<Brand> brands = this.brandClient.queryBrandByIds(
                    Arrays.asList(spu.getBrandId()));
            modelMap.put("brand", brands.get(0));

            //查询规格组及组内参数
            List<SpecGroup> groups = specClient.querySpecsByCid(spu.getCid3());
            modelMap.put("groups",groups);

            //查询商品分类下的特殊规格
            List<SpecParam> params =
                    this.specClient.querySpecParams(null, spu.getCid3(), null, false);
            // 处理成id:name格式的键值对
            Map<Long,String> paramMap = new HashMap<>();
            for (SpecParam param : params) {
                paramMap.put(param.getId(), param.getName());
            }
            modelMap.put("params", paramMap);

            return modelMap;

        }catch (Exception e){
            logger.error("加载商品数据出错，spuId: ",id,e);
        }
        return null;
    }

    private List<Category> getCategories(Spu spu) {
        try{
            List<String> names = categoryClient.queryNameByCategoryIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            Category c1 = new Category();
            c1.setId(spu.getCid1());
            c1.setName(names.get(0));

            Category c2 = new Category();
            c2.setId(spu.getCid2());
            c2.setName(names.get(1));

            Category c3 = new Category();
            c3.setId(spu.getCid3());
            c3.setName(names.get(2));

            return Arrays.asList(c1,c2,c3);

        }catch (Exception e){
            logger.error("查询商品分类出错，spuId： ",spu.getId(), e);
            return null;
        }
    }



}
