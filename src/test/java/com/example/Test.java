package com.example;

import com.example.controller.AccountController;
import com.example.controller.FileInfoController;
import com.example.entity.SystemFileInfoHashCode;
import com.example.service.FileInfoService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;

@ComponentScan
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private FileInfoService service;

    @Autowired
    private FileInfoController controller;
    @org.junit.Test
    public void test1(){
        SystemFileInfoHashCode hashCode = new SystemFileInfoHashCode();
        hashCode.setFileId(4L);
        hashCode.setFilehashCode("123435adsaqd");
        System.out.println(service.add(hashCode));
    }

    @org.junit.Test
    public void tets2(){
        System.out.println(controller.findByHashCode("8b5dcaafbee71c5cd7db105a3ff0ce0f").getData());
        System.out.println(service.isExistSameFileByHashCode("8b5dcaafbee71c5cd7db105a3ff0ce0f"));
    }

}
