package com.samh.common.utils;

import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author WANKAI
 * @Date 2022/4/1 19:43
 */
@Service
public class AnalyseUtil {
    static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public Integer getBusinessType() {
        Integer business_type = threadLocal.get();
        return business_type == null ? 1 : business_type;
    }

    public void setBusinessType(Integer business_type) {
        threadLocal.set(business_type);
    }

}
