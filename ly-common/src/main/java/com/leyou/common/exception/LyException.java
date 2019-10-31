package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;

/**
 * 定义异常
 */

public class LyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public int getCode(){
        return this.exceptionEnum.getCode();
    }

    public String getMsg(){
        return this.exceptionEnum.getMsg();
    }

    public ExceptionEnum getExceptionEnum(){
        return this.exceptionEnum;
    }

    public LyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
