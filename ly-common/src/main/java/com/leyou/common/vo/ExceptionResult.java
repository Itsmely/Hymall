package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;

public class ExceptionResult {

    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum exceptionEnum){
        this.status = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
