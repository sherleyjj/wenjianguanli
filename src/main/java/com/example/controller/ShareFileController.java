package com.example.controller;

import com.example.common.Result;
import com.example.pojo.SharedOrLikes;
import com.example.service.FileInfoService;
import com.example.service.ShareFileService;
import com.example.vo.ShareFileVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/share")
public class ShareFileController {
    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private ShareFileService shareFileService;

    @Resource
    private NxSystemFileController nxSystemFileController;
    @GetMapping("/all/page")
    public Result<PageInfo<ShareFileVo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize){
        return Result.success(fileInfoService.findAllShareFilePage(pageNum,pageSize));

    }

    @PostMapping("/up")
    public Result<SharedOrLikes> share(@RequestBody ShareFileVo shareFileVo){
        if (shareFileVo.getFileId() == null || shareFileVo.getFilename() == null || shareFileVo.getFirstUserId() == null)
            return Result.error("502","重要属性不存在");
        return Result.success(shareFileService.sharedOrLikesSimple(shareFileVo));
    }

    //TODO nx的记录没有删除
    @DeleteMapping("/delete/{fileId}")
    public Result delete(@PathVariable Integer fileId){
        int res = shareFileService.delete(fileId);
        //删除真实文件
        if (res ==0){
            nxSystemFileController.deleteFile(String.valueOf(fileId));
        }
        return Result.success(res);
    }

}
