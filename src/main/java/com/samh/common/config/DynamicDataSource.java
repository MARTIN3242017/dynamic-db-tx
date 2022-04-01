package com.samh.common.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    public enum DBEnum {
        shence, ab
    }

    // 使用ThreadLocal保证线程安全
    private static final ThreadLocal<DBEnum> TYPE = new ThreadLocal<DBEnum>();

    // 往当前线程里设置数据源类型
    public static void setDataBaseType(DBEnum DBEnum) {
        if (DBEnum == null) {
            throw new NullPointerException();
        }
        TYPE.set(DBEnum);
    }

    // 获取数据源类型
    public static DBEnum getDBEnum() {
        return TYPE.get() == null ? DBEnum.shence : TYPE.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDBEnum();
    }
}
