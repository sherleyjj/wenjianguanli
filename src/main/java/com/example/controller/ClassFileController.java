package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.ClassFile;
import com.example.entity.FileInfo;
import com.example.entity.NxSystemFileInfo;
import com.example.exception.CustomException;
import com.example.service.ClassFileService;
import com.example.service.FileInfoService;
import com.example.service.TypeInfoService;
import com.example.vo.ClassFileVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/class")
public class ClassFileController {
    @Resource
    private ClassFileService classFileService;
    @Resource
    private NxSystemFileController nxSystemFileController;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private TypeInfoService typeInfoService;

    @GetMapping("/getExcel/{typeId}")
    public void downloadExcel(@PathVariable("typeId") Integer typeId, HttpServletResponse response) throws IOException {
        classFileService.saveExcel(response,typeId);
    }

    @GetMapping("/getAll/{typeId}")
    public Result<List<ClassFileVo>> getAll(@PathVariable("typeId") Integer typeId){
        return Result.success(classFileService.getAllFileByTypeId(typeId));
    }

    /**
     * TODO 管理员上传时会因为外键约束失败
     * 显然需要先真实上传文件再add信息到这个地方，因为真实上传后才会有fileId
     * @param classFile
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ClassFile classFile){
        if (BeanUtil.isEmpty(classFile,"id","uploadDate")){
            throw new CustomException("-1","必要属性为空");
        }
        int count =classFileService.addFileByClassFile(classFile);
        classFileService.addRef(classFile.getFileId());
        return Result.success(count);
    }

    /**
     * 一定要返回一个fileId给前端
     * @param file
     * @param request
     * @param code
     * @return
     * @throws IOException
     */
//    TODO Bug 管理员上传会遇到外键绑定的bug user_info
    @PostMapping("/upload/{hashcode}")
    @Transactional
    public Result upload(MultipartFile file,
                         HttpServletRequest request,
                         @PathVariable("hashcode")String code) throws IOException {

        if (fileInfoService.isExistSameFileByHashCode(code)) {
            //应该由hashcode得到fileId而不是得到fileInfo
            FileInfo fileInfo =fileInfoService.findByHash(code);
            return Result.success(fileInfo.getFileId().intValue());
        }else{
            NxSystemFileInfo nxSystemFileInfo= ((NxSystemFileInfo) nxSystemFileController.upload(file, code, request).getData());
            return Result.success(nxSystemFileInfo.getId());
        }
    }


    /**
     * 回退应该删除 文件
     * @param typeId
     * @param date
     * @return
     */
    @GetMapping("/back/{typeId}/{date}")
    public Result back(@PathVariable("typeId")Integer typeId,@PathVariable("date")String date){
        List temp = classFileService.getAllFileByTypeId(typeId);
        int count = classFileService.FileBack(typeId,date);
        if(count == 0){
            throw new CustomException("-1","回退异常");
        }
        List<ClassFileVo> deleted = temp.subList(0,count);
        for(ClassFileVo index : deleted){
            if (classFileService.deleteRef(index.getFileId())==0){
                nxSystemFileController.deleteFile(index.getFileId().toString());
            }
        }

        return Result.success("回退成功");

    }

    @DeleteMapping("/delete/all/{typeId}")
    public Result deleteAll(@PathVariable("typeId") Integer typeId){
        return Result.success(classFileService.deleteAll(typeId));
    }


    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response,HttpServletRequest request) throws IOException {
        if ("null".equals(id)) {
            throw new CustomException("1001", "您未上传文件");
        }
        nxSystemFileController.download(id,response,request);
    }

    @GetMapping("/saveExcel/{typeId}")
    public void getExcel(HttpServletResponse response,@PathVariable("typeId") Integer typeId) throws IOException {
        List<List<String >> lists = new ArrayList<List<String>>();

        List<String> head = ListUtil.list(false,"姓名","修改记录","上传时间");
        lists.add(head);

        for (ClassFileVo index : classFileService.getAllFileByTypeId(typeId)){
            lists.add(ListUtil.list(false,
                    index.getUserName(),
                    index.getChangeRecord(),
                    index.getUploadDate()));
        }
        //实验成功一个list为一行
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(lists, true);
        String base64 = URLEncoder.encode("类别-"+typeInfoService.findById(typeId.longValue()).getName()+".xlsx","UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename="+base64);

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
}
