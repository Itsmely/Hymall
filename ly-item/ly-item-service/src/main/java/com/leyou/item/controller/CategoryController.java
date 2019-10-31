package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        List<Category> list = categoryService.queryListByParentId(pid);
        if(list == null || list.size() < 1 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryByCategoryIds(@RequestParam("ids") List<Long> ids ){
        List<Category> list = categoryService.queryByIds(ids);

        return ResponseEntity.ok(list);
    }

    /**
     * 根据商品分类id查询名称
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> queryNameByCategoryIds(@RequestParam("ids") List<Long> ids){
        List<String> names = categoryService.queryNameByIds(ids);

        if(names == null || names.size() <1){
            throw new LyException(ExceptionEnum.CATEGORY_NAME_NOT_FOUND);
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 根据商品分类cid3 查询父Category
     */
    @GetMapping("/cid3")
    public ResponseEntity<List<Category>> queryCategoriesByCid3(@RequestParam("cid3") Long id){
        List<Category> result = categoryService.queryCategoriesById(id);
        if(result.size() < 1){
            throw new LyException(ExceptionEnum.CATEGORY_NAME_NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}
