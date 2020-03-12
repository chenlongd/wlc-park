package com.perenc.xh.lsp.entity.operLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/23 10:44
 **/

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLog {

    /** 要执行的操作类型比如：表名 **/
    public String operationType() default "";

    /** 要执行的具体操作如：添加 **/
    public String operationName() default "";
}
