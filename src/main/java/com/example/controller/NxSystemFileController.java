package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.HashUtil;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dao.FileInfoDao;
import com.example.entity.Account;
import com.example.entity.NxSystemFileInfo;
import com.example.entity.SystemFileInfoHashCode;
import com.example.exception.CustomException;
import com.example.service.FileInfoService;
import com.example.service.NxSystemFileInfoService;
import com.example.service.downlad.IDownloadStrategy;
import com.example.service.downlad.impl.DefaultDownload;
import com.example.vo.IsContainAndNxInfo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/files")
public class NxSystemFileController {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/resources/static/file/";

    @Resource
    private NxSystemFileInfoService nxSystemFileInfoService;

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private IDownloadStrategy downloadStrategy;

    /**
     * 前端上传文件，包括文件的hash码，应该先检验hash码再上传，如果调用该方法代表hash是不存在的
     * 目前出现了Request Entity Too Large->bug，这是来自于nginx的配置问题
     * @param file
     * @param hashcode
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/{hashcode}")
    public Result upload(MultipartFile file,@PathVariable("hashcode") String hashcode,HttpServletRequest request) throws IOException {
        String originName = file.getOriginalFilename();
        //杜绝文件名可能一样的情况
        String fileName = FileUtil.mainName(originName) + System.currentTimeMillis() + "." + FileUtil.extName(originName);
        byte [] source = file.getBytes();
        /**
         * 测试计算hash值，通过byte数组
         */
//        String md5 =  Md5Crypt.md5Crypt(source);

        // 3. 信息入库，获取文件id
        NxSystemFileInfo info = new NxSystemFileInfo();
        info.setOriginName(originName);
        info.setFileName(fileName);

        //4.将文件hash和文件记录都写入表中
        NxSystemFileInfo addInfo = nxSystemFileInfoService.add(hashcode,info);
        // 2. 文件上传

        FileUtil.writeBytes(source, BASE_PATH + fileName);


        if (addInfo != null) {
            return Result.success(addInfo);
        } else {
            return Result.error("4001", "上传失败");
        }
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<NxSystemFileInfo>> filePage(@PathVariable String name,
                                                       @RequestParam(defaultValue = "1") Integer pageNum,
                                                       @RequestParam(defaultValue = "10") Integer pageSize) {

        PageInfo<NxSystemFileInfo> pageInfo = nxSystemFileInfoService.findPage(name, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 限制下载数有bug，下载会有中断的情况，然后导致数据对不上，是否能采用多线程的东西,尝试用aop试一试
     * @param id
     * @param response
     * @param request
     * @throws IOException
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response,HttpServletRequest request) throws IOException {
        if ("null".equals(id)) {
            throw new CustomException("1001", "您未上传文件");
        }
//        Integer downloadCount =(Integer) request.getSession().getAttribute("downloadCount");
//        downloadCount = downloadCount==null?0:downloadCount;
//        log.info("{}:当前下载数{}",((Account)request.getSession().getAttribute("user")).getName()
//                ,downloadCount);
//        if (downloadCount>2){
//            throw new CustomException("失败","只能同时下载一个文件");
//        }
//        request.getSession().setAttribute("downloadCount",++downloadCount);
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "未查询到该文件");
        }
        byte[] bytes = FileUtil.readBytes(BASE_PATH + nxSystemFileInfo.getFileName());
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(nxSystemFileInfo.getOriginName(), "UTF-8"));
        downloadStrategy.download(response,bytes);
        //        response.addHeader("Content-Length", "" + bytes.length);
//        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//        response.setContentType("application/octet-stream");
////        toClient.write(bytes);
//        int i = 0;
//        int count = 1000;
//        //限速成功
//        for (; i < bytes.length-count; i+=count) {
//            toClient.write(bytes,i,count);
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                log.info("sleep出错");
//            }
//        }
//        i-=count;
//
//        toClient.write(bytes,i,bytes.length-i);
//        toClient.flush();
//        toClient.close();
//        request.getSession().setAttribute("downloadCount",--downloadCount);
    }

    /**
     * restful风格
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteFile(@PathVariable String id) {
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "未查询到该文件");
        }
        String name = nxSystemFileInfo.getFileName();
        // 先删除文件
        FileUtil.del(new File(BASE_PATH + name));
        // 再删除表记录
        nxSystemFileInfoService.delete(Long.parseLong(id));

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<NxSystemFileInfo> getById(@PathVariable String id) {
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "数据库未查到此文件");
        }
        try {
            FileUtil.readBytes(BASE_PATH + nxSystemFileInfo.getFileName());
        } catch (Exception e) {
            throw new CustomException("1001", "此文件已被您删除");
        }
        return Result.success(nxSystemFileInfo);
    }

    @GetMapping("/isexist/{hash}")
    public Result isExistSameFileAndGetNxFileInfo(@PathVariable String hash){
        Result result = new Result();
        IsContainAndNxInfo info = new IsContainAndNxInfo();
        NxSystemFileInfo nxSystemFileInfo =  nxSystemFileInfoService.findNxFileInfoByMd5(hash);
        if (nxSystemFileInfo != null){
            info.setExist(true);
            info.setFileName(nxSystemFileInfo.getFileName());
            info.setId(nxSystemFileInfo.getId());
            info.setOriginName(nxSystemFileInfo.getOriginName());
            result.setCode("0");
        }else{
            info.setExist(false);
            result.setCode("1");
        }
        result.setData(info);
        return result;
    }
}
