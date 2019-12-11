package com.sample.test;

import com.sample.test.dao.config.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.sample.test.dao.mapper")
@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
public class SpringbootHikariMoreDatasourceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHikariMoreDatasourceWebApplication.class, args);
    }

}
