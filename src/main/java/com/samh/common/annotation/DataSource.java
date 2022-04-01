package com.samh.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 动态数据源-自定义注解
 * @Author WANKAI
 * @Date 2021/11/26 15:28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    //mysql或clickhouse
    String value() default "mysql";
}