package com.sample.test.web.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MyBatisConfiguration {
	
	/**
	 * Github上有位开发者写了一个分页插件PageHelper
	 * Github项目地址： https://github.com/pagehelper/Mybatis-PageHelper
	 * 更多参数使用方法，详见PageHelper说明文档（上面的Github地址）
	 * 
	 * 注册MyBatis分页插件PageHelper
	 * @return
	 */
	@Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
