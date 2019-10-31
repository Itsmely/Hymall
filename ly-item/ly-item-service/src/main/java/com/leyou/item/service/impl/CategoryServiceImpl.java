package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.apache.commons.lang3.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryListByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public List<String> queryNameByIds(List<Long> ids) {
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
        // return list.stream().map(category -> category.getName()).collect(Collectors.toList());
    }

    @Override
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        return list;
    }

    @Override
    public List<Category> queryCategoriesById(Long id) {
        List<Category> result = new ArrayList<>();
        while (true){
            Long pid = categoryMapper.selectByPrimaryKey(id).getParentId();
            System.out.println(pid);
            if (pid != 0) {
                Category category = categoryMapper.selectByPrimaryKey(id);
                result.add(category);
                id = pid;
                System.out.println(id);
            } else {
                result.add(categoryMapper.selectByPrimaryKey(id));
                break;
            }
        }
        Collections.reverse(result);
        return result;
    }

}
