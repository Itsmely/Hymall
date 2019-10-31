package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.service.SpecParamService;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;
    @Override
    public List<SpecParam> querySpecParam(Long gid) {
        SpecParam s = new SpecParam();
        s.setGroupId(gid);
        return specParamMapper.select(s);
    }
}
