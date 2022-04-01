package com.samh.common.annotation;

import com.samh.common.config.DynamicDataSource;
import com.samh.common.utils.AnalyseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description 动态数据源切面类
 * @Author WANKAI
 * @Date 2021/11/26 15:32
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class DataSourceAspect {
    final static String DEFAULT_DB = "mysql";
    @Autowired
    private AnalyseUtil analyseUtil;

    @Pointcut("@annotation(com.samh.common.annotation.DataSource)||execution(* com.samh..*.mapper.*.*(..))")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String value = DEFAULT_DB;

        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource != null)
            value = dataSource.value();
        if (value.contains(".")) {
            DynamicDataSource.setDataBaseType(DynamicDataSource.DBEnum.shence);
        } else {
            Integer business_type = analyseUtil.getBusinessType();
            if (business_type == null || business_type.equals(1)) {
                DynamicDataSource.setDataBaseType(DynamicDataSource.DBEnum.shence);
            } else if (business_type.equals(2)) {
                DynamicDataSource.setDataBaseType(DynamicDataSource.DBEnum.ab);
            } else if (business_type.equals(3)) {
                DynamicDataSource.setDataBaseType(DynamicDataSource.DBEnum.shence);
            }
            // log.info("DBSource:{}, business_type:{} ", value, business_type);
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.setDataBaseType(DynamicDataSource.DBEnum.shence);
        }
    }
}
