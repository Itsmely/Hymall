package com.leyou.search.reposiory;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsReposiory extends ElasticsearchRepository<Goods,Long> {
}
