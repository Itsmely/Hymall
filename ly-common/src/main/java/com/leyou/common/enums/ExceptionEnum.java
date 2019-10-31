package com.leyou.common.enums;

/**
 * 定义异常code和msg
 */

public enum ExceptionEnum {
    CATEGORY_NAME_NOT_FOUND(404,"未找到相关分类"),
    SPU_DETAIL_NOT_FOUND(404,"SPU_DETAIL未找到"),
    SPU_NOT_FOUND(404,"SPU未找到"),
    SKU_NOT_FOUND(404,"SKU未找到"),
    BRAND_NOT_FOUND(404,"品牌未找到"),
    SPEC_GROUP_NOT_FOUND(404,"SPEC_GROUP未找到"),
    SPEC_PARAM_NOT_FOUND(404,"SPEC_PARAM未找到")
    ;

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
