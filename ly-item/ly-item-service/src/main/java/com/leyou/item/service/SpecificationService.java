package com.leyou.item.service;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {
    List<SpecGroup> queryGroupByCid(Long cid);
    List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic);
    List<SpecGroup> queryListByCid(Long cid);
    List<SpecGroup> querySpecsByCid(Long cid);
}
