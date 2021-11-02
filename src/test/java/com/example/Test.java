package com.example;

import com.example.common.until.BigHeap;
import com.example.controller.AccountController;
import com.example.controller.FileInfoController;
import com.example.controller.NxSystemFileController;
import com.example.controller.UserInfoController;
import com.example.dao.UserInfoDao;
import com.example.entity.SystemFileInfoHashCode;
import com.example.service.FileInfoService;
import com.example.service.NxSystemFileInfoService;
import com.example.vo.UserInfoVo;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Handler;

@ComponentScan
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private FileInfoService service;
    @Autowired
    private UserInfoController userInfoController;

    @Autowired
    private NxSystemFileController nxSystemFileController;
    @Autowired
    private FileInfoController controller;
    @Autowired
    private NxSystemFileInfoService nxSystemFileInfoService;

    @Autowired
    private UserInfoDao userInfoDao;

    @org.junit.Test
    public void test1(){
        SystemFileInfoHashCode hashCode = new SystemFileInfoHashCode();
        hashCode.setFileId(4L);
        hashCode.setFilehashCode("123435adsaqd");
        System.out.println(service.add(hashCode));
    }

    @org.junit.Test
    public void tets2(){
//        System.out.println(controller.findByHashCode("8b5dcaafbee71c5cd7db105a3ff0ce0f").getData());
//        System.out.println(service.isExistSameFileByHashCode("8b5dcaafbee71c5cd7db105a3ff0ce0f"));
        System.out.println(nxSystemFileInfoService.findNxFileInfoByMd5("8b5dcaafbee71c5cd7db105a3ff0ce0f"));
    }

    @org.junit.Test
    public void test3(){
        BigHeap heap = new BigHeap();

        HashMap<Integer,String> map = new HashMap<>();
        map.put(15,'a'+String.valueOf(15));
        map.put(55,'a'+String.valueOf(55));
        map.put(65,'a'+String.valueOf(65));
        map.put(185,'a'+String.valueOf(185));

        for (int i = 1; i < 500; i++) {
            map.put(i,"jian"+String.valueOf(i));
        }

        heap.buildHeapWithHashMap(map);
        heap.add(285,"江德鸿");
        heap.add(285,"江德鸿");
        heap.add(3456,"江德鸿");heap.add(4681,"江德鸿");heap.add(45852,"江德鸿");

        System.out.println(Arrays.toString(heap.getSortedList().toArray()));
    }
    @org.junit.Test
    public void test5(){
        for (int i = 347954; i < 1000000; i++) {
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setAge(18);
            userInfoVo.setAddress("四川省");
            userInfoVo.setLevel(2);
            userInfoVo.setPassword("123456");
            userInfoVo.setName("kasttt"+i);
            userInfoVo.setNickName("开心超人 ");
            userInfoVo.setPhone("15881883011");
            userInfoVo.setSex("Male");
            userInfoVo.setEmail("1714203542@qq.com");
            userInfoDao.insertSelective(userInfoVo);
//            userInfoController.add(userInfoVo);
        }
    }






    @org.junit.Test
    public void test4(){



                String[] arguments = new String[] {"python", "d://hello.py","销售"};
                try {
                    Process process = Runtime.getRuntime().exec(arguments);
                    System.out.println(Arrays.toString(Arrays.stream(arguments).toArray()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),
                            "GBK"));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    in.close();
                    //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
                    //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
                    int re = process.waitFor();
                    System.out.println(re);
                    byte  []value =new byte[process.getErrorStream().available()];
                    process.getErrorStream().read(value);
                    System.out.println(new String(value));

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }


}
