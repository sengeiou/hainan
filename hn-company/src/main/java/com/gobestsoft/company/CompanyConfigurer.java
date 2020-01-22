package com.gobestsoft.company;

import com.gobestsoft.company.interceptor.AuthInterceptor;
import com.gobestsoft.company.meshsite.Meshsite3Filter;
import com.gobestsoft.mamage.config.ManageConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


/**
 * 项目配置
 * create by gutongwei
 * on 2018/7/27 下午4:17
 */
@Configuration
public class CompanyConfigurer extends ManageConfigurer {

    /**
     * 添加静态文件地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false);
    }

    /**
     * SiteMesh 配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean siteMeshFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        Meshsite3Filter siteMeshFilter = new Meshsite3Filter();
        filter.setFilter(siteMeshFilter);
        return filter;
    }

    @Bean
    AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).excludePathPatterns(authExcludePath).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
