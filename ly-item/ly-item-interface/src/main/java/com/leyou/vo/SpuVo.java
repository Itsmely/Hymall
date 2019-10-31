package com.leyou.vo;

import com.leyou.pojo.Spu;

import java.util.Date;

public class SpuVo extends Spu {
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    private String cname;
    private String bname;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getBrandId() {
        return brandId;
    }

    @Override
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public Long getCid1() {
        return cid1;
    }

    @Override
    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    @Override
    public Long getCid2() {
        return cid2;
    }

    @Override
    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    @Override
    public Long getCid3() {
        return cid3;
    }

    @Override
    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public Boolean getSaleable() {
        return saleable;
    }

    @Override
    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    @Override
    public Boolean getValid() {
        return valid;
    }

    @Override
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
