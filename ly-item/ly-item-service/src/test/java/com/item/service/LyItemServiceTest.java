package com.item.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.LyItemService;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SpecificationService;
import com.leyou.item.service.impl.SpecificationServiceImpl;
import com.leyou.pojo.Category;
import com.leyou.pojo.SpecParam;
import com.leyou.pojo.SpuDetail;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = LyItemService.class)
@RunWith(SpringRunner.class)
public class LyItemServiceTest {

    @Autowired
    private SpecificationService specificationService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testSpec(){
        List<SpecParam> specParams = specificationService.querySpecParams(null, 76L, true, null);
        specParams.forEach(s-> System.out.println(s.toString()));

    }

    @Test
    public void testGoods() {
        SpuDetail spuDetail = goodsService.querySpuDetailById(2L);
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
    }

    @Test
    public void testCategory(){
        List<Category> categories = categoryService.queryCategoriesById(76L);
        categories.forEach(c -> System.out.println("name: "+c.getName()));
    }

}
