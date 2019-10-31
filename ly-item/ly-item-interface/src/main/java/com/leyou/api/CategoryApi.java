package com.leyou.api;

import com.leyou.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {

    @GetMapping("/names")
    List<String> queryNameByCategoryIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/list/ids")
    List<Category> queryByCategoryIds(@RequestParam("ids") List<Long> ids );

    @GetMapping("/cid3")
    List<Category> queryCategoriesByCid3(@RequestParam("cid3") Long id);
}
