package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.vo.SpuVo;

import java.util.List;

public interface GoodsService {
    PageResult<SpuVo> querySpuByPage(Integer page, Integer rows, String key, Boolean saleable);


    SpuDetail querySpuDetailById(Long id);

    Spu querySpuByid(Long id);

    List<Sku> querySkuBySpuId(Long spuId);
}
