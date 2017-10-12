package com.avery.recuritcloud.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableAutoConfiguration
public class DataSourceConfig implements EnvironmentAware {
    
    Logger log= LoggerFactory.getLogger(DataSourceConfig.class);
    
    private RelaxedPropertyResolver relaxedPropertyResolver;
    
    private Environment env;
    
    
    @Override
    public void setEnvironment(Environment environment) {
        
        this.env=environment;
        this.relaxedPropertyResolver=new RelaxedPropertyResolver(env,"spring.datasource.");
    }
    
    @Bean
    public DataSource dataSource()
    {
        HikariConfig hikariConfig=new HikariConfig();
        log.info("dataSourceClassName:{}",relaxedPropertyResolver.getProperty("dataSourceClassName"));
        hikariConfig.setDataSourceClassName(relaxedPropertyResolver.getProperty("dataSourceClassName"));
        log.info("username:{}",relaxedPropertyResolver.getProperty("username"));
        hikariConfig.addDataSourceProperty("user",relaxedPropertyResolver.getProperty("username"));
        hikariConfig.addDataSourceProperty("password",relaxedPropertyResolver.getProperty("password"));
        hikariConfig.addDataSourceProperty("url",relaxedPropertyResolver.getProperty("url"));
        
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setPoolName("pool");
        hikariConfig.setConnectionTimeout(30*1000);
        
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(relaxedPropertyResolver.getProperty("dataSourceClassName"))) {
            hikariConfig.addDataSourceProperty("cachePrepStmts", relaxedPropertyResolver.getProperty("cachePrepStmts", "true"));
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", relaxedPropertyResolver.getProperty("prepStmtCacheSize", "250"));
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", relaxedPropertyResolver.getProperty("prepStmtCacheSqlLimit", "2048"));
            hikariConfig.addDataSourceProperty("useServerPrepStmts", relaxedPropertyResolver.getProperty("useServerPrepStmts", "true"));
        }
        
        return new HikariDataSource(hikariConfig);
    }
    
    // @Bean
    // public SpringLiquibase Liquibase(DataSource dataSource)
    // {
    //     SpringLiquibase springLiquibase=new SpringLiquibase();
    //     springLiquibase.setDataSource(dataSource);
    //     springLiquibase.setChangeLog("classpath:lisquibase/master.xml");
    //     springLiquibase.setShouldRun(true);
    //     springLiquibase.setDropFirst(true);
    //     return springLiquibase;
    // }
    
}
