package com.samh.common.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.samh.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfig {

    /**
     * 创建多个数据源 shence 和 ab
     */

    @Primary
    @Bean(name = "shence")
    @ConfigurationProperties(prefix = "spring.datasource.shence")
    public DataSource getDateSource1(Environment env) {
        return getDataSource(env, "spring.datasource.shence", "shence");
    }

    @Bean(name = "ab")
    @ConfigurationProperties(prefix = "spring.datasource.ab")
    public DataSource getDateSource2(Environment env) {
        return getDataSource(env, "spring.datasource.ab", "ab");
    }

    public DataSource getDataSource(Environment env, String prefix, String dataSourceName) {
        Properties prop = build(env, prefix);
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName(MysqlXADataSource.class.getName());
        ds.setUniqueResourceName(dataSourceName);
        ds.setXaProperties(prop);
        return ds;
    }

    protected Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + ".jdbc_url"));
        prop.put("user", env.getProperty(prefix + ".username"));
        prop.put("password", env.getProperty(prefix + ".password"));
        return prop;
    }

    @Bean(name = "sqlSessionFactory1")
    public SqlSessionFactory sqlSessionFactory1(@Qualifier("shence") DataSource dataSource) throws Exception {
        return createSqlSessionFactory(dataSource);
    }

    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory2(@Qualifier("ab") DataSource dataSource) throws Exception {
        return createSqlSessionFactory(dataSource);
    }

    /**
     * 将多个数据源注入到DynamicDataSource
     *
     * @param dataSource1
     * @param dataSource2
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource DataSource(@Qualifier("shence") DataSource dataSource1,
                                        @Qualifier("ab") DataSource dataSource2) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DynamicDataSource.DBEnum.shence, dataSource1);
        targetDataSource.put(DynamicDataSource.DBEnum.ab, dataSource2);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(dataSource1);
        return dataSource;
    }

    @Bean(name = "sqlSessionTemplate")
    public CustomSqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1") SqlSessionFactory factory1, @Qualifier("sqlSessionFactory2") SqlSessionFactory factory2) throws Exception {
        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        sqlSessionFactoryMap.put("shence", factory1);
        sqlSessionFactoryMap.put("ab", factory2);
        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(factory1);
        customSqlSessionTemplate.setTargetSqlSessionFactorys(sqlSessionFactoryMap);
        customSqlSessionTemplate.setDefaultTargetSqlSessionFactory(factory1);
        return customSqlSessionTemplate;
    }

    /**
     * 将动态数据源注入到SqlSessionFactory
     *
     * @param
     * @return
     * @throws Exception
     */
    /*@Bean(name = "SqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*.xml"));
        bean.setTypeAliasesPackage("cn.youyouxunyin.dynamic-db-tx.entity");
        return bean.getObject();
    }*/
    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setVfs(SpringBootVFS.class);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session
                .Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }
}
