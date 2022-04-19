package com.example;


import cn.hutool.core.collection.CollUtil;
import com.example.common.myAspect.LogAop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication
@MapperScan("com.example.dao")
public class Application {
    @Bean(name = "LogAop")
    public LogAop initAspect(){
        return new LogAop();
    }
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}