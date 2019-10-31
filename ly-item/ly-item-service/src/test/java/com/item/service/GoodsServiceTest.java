package com.item.service;


import com.leyou.LyItemService;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = LyItemService.class)
@RunWith(SpringRunner.class)
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void goodsTest(){
        List<Sku> skus = goodsService.querySkuBySpuId(2L);
        skus.forEach(sku -> System.out.println(sku.getImages()));
    }
}
