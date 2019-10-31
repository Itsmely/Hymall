package com.leyou.search.client;

import com.leyou.LySearchApplication;
import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.reposiory.GoodsReposiory;
import com.leyou.search.service.SearchService;
import com.leyou.vo.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = LySearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private GoodsReposiory goodsReposiory;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;


    @Test
    public void createIndex(){
        //创建索引
        elasticsearchTemplate.createIndex(Goods.class);
        //配置索引
        elasticsearchTemplate.putMapping(Goods.class);

    }

    @Test
    public void deleteIndex(){
        elasticsearchTemplate.deleteIndex(Goods.class);
    }

    @Test
    public void loadGoods(){

        int page = 1;
        int row = 100;
        int size = 0;

        do{
            //查询spu
            PageResult<SpuVo> result = goodsClient.querySpuByPage(page, row, true, null);
            List<SpuVo> spus = result.getItems();
            if(CollectionUtils.isEmpty(spus)){
                break;
            }
            //把spu转化为goods
            List<Goods> goods = spus.stream()
                    .map(SpuVo -> searchService.buildGoods(SpuVo))
                    .collect(Collectors.toList());

            //把goods放入索引库
            goodsReposiory.saveAll(goods);

            size = spus.size();
            page++;
        }while (size == 100);

    }
}
