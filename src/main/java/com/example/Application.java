package com.example;


import com.example.common.myAspect.LogAop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


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