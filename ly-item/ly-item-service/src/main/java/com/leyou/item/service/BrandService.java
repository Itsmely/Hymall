package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Brand;

import java.util.List;

public interface BrandService {

    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    Brand findBrand(Long id);

    List<Brand> queryBrandByIds(List<Long> ids);
}
