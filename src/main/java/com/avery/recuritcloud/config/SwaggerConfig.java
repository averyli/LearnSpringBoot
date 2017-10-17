package com.avery.recuritcloud.config;


import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configurable
@EnableSwagger2
public class SwaggerConfig {
    public final String releaseV1="release-v1.0";
    
    public final DocumentationType documentationType=new DocumentationType("swagger","1.0");
    @Bean
    public Docket createRestApi(){
        return new Docket(documentationType)
            .enable(true)
            .apiInfo(apiInfo())
            .select()
            .paths(createPaths())
            .build();
    }
    
    public Predicate<String> createPaths()
    {
       return or(
           regex("/api/user"),
           regex("/api/company")
       );
    }
    
    public ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
            .title("hello")
            .license("localhost:home")
            .version("1.0")
            .build();
    }
}
