package com.leyou.goodspage;


import com.leyou.goodspage.client.GoodsClient;
import com.leyou.pojo.Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = LyGoodsPageService.class)
@RunWith(SpringRunner.class)
public class LyGoodsPageTest {

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testGoods(){
        List<Sku> skus = goodsClient.querySkuBySpuId(2L);
        skus.forEach(sku -> System.out.println(sku.getImages()));
    }
}
