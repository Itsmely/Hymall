package com.leyou.search.client;


import com.leyou.LySearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = LySearchApplication.class)
@RunWith(SpringRunner.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Test
    public void testCategory(){
        List<String> names = categoryClient.queryNameByCategoryIds(Arrays.asList(1L, 2L, 3L));

        names.forEach(System.out::println);
        while (true){

        }
    }




}
