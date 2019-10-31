package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Category;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){

        PageResult<Goods> result = searchService.search(request);
        if(result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据cid3查询出分类列表
     */
    @GetMapping("/all/level")
    public ResponseEntity<List<Category>> findCategoryByCid3(@RequestParam("cid3") Long cid3){
        List<Category> categories = searchService.findCategoryByCid3(cid3);
        return ResponseEntity.ok(categories);
    }

}
