package com.leyou.api;

import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/brand")
public interface BrandApi {

    @GetMapping("/id")
    Brand findBrand(@RequestParam("id") Long id);

    @GetMapping("/list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);

}
