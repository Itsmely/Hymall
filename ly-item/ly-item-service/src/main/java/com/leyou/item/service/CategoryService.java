package com.leyou.item.service;

import com.leyou.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 据父节点查询商品类目
     * @param pid
     * @return
     */
    List<Category> queryListByParentId(Long pid);

    List<String> queryNameByIds(List<Long> asList);

    List<Category> queryByIds(List<Long> ids);

    List<Category> queryCategoriesById(Long id);
}
