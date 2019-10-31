package com.leyou.search.pojo;

import java.util.Map;

public class SearchRequest {

    private String key; //搜索条件
    private Integer page; //当前页
    private String sortBy; //排序字段
    private Boolean descending; //是否降序
    private Map<String,String> filter; //过滤选项

    private static final Integer DEFAULT_PAGE = 1; //默认当前页
    private static final Integer DEFAULT_SIZE = 20; //默认显示商品数量

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(page,DEFAULT_PAGE);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }
}
