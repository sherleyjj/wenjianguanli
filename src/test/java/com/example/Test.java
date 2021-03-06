package com.example;

import cn.hutool.core.collection.CollUtil;
import com.example.common.until.BigHeap;
import com.example.controller.*;
import com.example.dao.FileInfoDao;
import com.example.dao.UserInfoDao;
import com.example.entity.SystemFileInfoHashCode;
import com.example.service.CommentService;
import com.example.service.FileInfoService;
import com.example.service.NxSystemFileInfoService;
import com.example.to.CommentTo;
import com.example.vo.FileInfoVo;
import com.example.vo.UserInfoVo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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

    @Autowired
    private ShareFileController shareFileController;

    @Autowired
    private FileInfoDao dao;
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentController commentController;
    @Autowired
    private HttpSession httpSession;

    @org.junit.Test
    public void test1(){
        SystemFileInfoHashCode hashCode = new SystemFileInfoHashCode();
        hashCode.setFileId(4L);
        hashCode.setFilehashCode("123435adsaqd");
        System.out.println(service.addHash(hashCode));
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
        heap.add(285,"?????????");
        heap.add(285,"?????????");
        heap.add(3456,"?????????");heap.add(4681,"?????????");heap.add(45852,"?????????");

        System.out.println(Arrays.toString(heap.getSortedList().toArray()));
    }
    @org.junit.Test
    public void test5(){
        for (int i = 347954; i < 1000000; i++) {
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setAge(18);
            userInfoVo.setAddress("?????????");
            userInfoVo.setLevel(2);
            userInfoVo.setPassword("123456");
            userInfoVo.setName("kasttt"+i);
            userInfoVo.setNickName("???????????? ");
            userInfoVo.setPhone("15881883011");
            userInfoVo.setSex("Male");
            userInfoVo.setEmail("1714203542@qq.com");
            userInfoDao.insertSelective(userInfoVo);
//            userInfoController.addHash(userInfoVo);
        }
    }






    @org.junit.Test
    public void test4(){



                String[] arguments = new String[] {"python", "d://hello.py","??????"};
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
                    //java????????????process.waitFor()????????????0??????????????????python???????????????
                    //????????????1????????????python???????????????????????????????????????????????????0???1??????????????????
                    int re = process.waitFor();
                    System.out.println(re);
                    byte  []value =new byte[process.getErrorStream().available()];
                    process.getErrorStream().read(value);
                    System.out.println(new String(value));

                } catch (Exception e) {

                    e.printStackTrace();
                }
    }


    //????????????
    @org.junit.Test
    public void test6() {
        UserInfoVo userInfoVo =userInfoDao.findByUsername("?????????");
        FileInfoVo fileInfoVo = service.findAll().get(0);
        service.sharedAndLikes(userInfoVo,fileInfoVo);
    }

    @org.junit.Test
    public void test7() {
        UserInfoVo userInfoVo =userInfoDao.findByUsername("?????????");
        FileInfoVo fileInfoVo = service.findAll().get(1);
        service.sharedAndLikes(userInfoVo,fileInfoVo);
    }
    @org.junit.Test
    public void test8() {
        System.out.println(service.findAllShareFilePage(1, 3));
    }
    public void test9() {
        System.out.println(shareFileController.page(1,3));
    }
    @org.junit.Test
    public void test10() {
        CommentTo commentTo = new CommentTo();
        commentTo.setComment("Hello");
        commentTo.setFileId(88);
        commentTo.setUserId(11);
        for (int i = 0; i < 2; i++) {
            commentService.addComment(commentTo);
        }
    }
    @org.junit.Test
    public void test11() {
        CommentTo commentTo = new CommentTo();
        commentTo.setComment("Hello");
        commentTo.setFileId(87);
        commentTo.setUserId(11);
        commentService.deleteCommentById(1);
    }
    @org.junit.Test
    public void test12() {

        commentService.likes(13,11,2);
    }
    //????????????????????????,??????????????????
    @org.junit.Test
    public void test13() {
        shareFileController.delete(88);
    }

    @org.junit.Test
    public void test14() {
       int a = 1;
       int b = ++a*a++;
       System.out.println(b);
    }

    //??????delete????????????
    @org.junit.Test
    public void test15() {
       commentService.deleteCommentById(6);
    }

    @org.junit.Test
    public void test16(){
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", "?????????");
        row.put("time","future");
        List<Map<String, Object>> lists = CollUtil.newArrayList(row);

    }
}
