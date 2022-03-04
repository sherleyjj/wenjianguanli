package com.example.controller;

import com.example.common.Result;
import com.example.entity.ShareFile;
import com.example.pojo.SharedOrLikes;
import com.example.service.FileInfoService;
import com.example.service.ShareFileService;
import com.example.vo.ShareFileVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/all/page")
    public Result<PageInfo<ShareFileVo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize){
        return Result.success(fileInfoService.findAllShareFilePage(pageNum,pageSize));

    }

    @PostMapping("/up")
    public Result<SharedOrLikes> share(@RequestBody ShareFileVo shareFileVo){
        if (shareFileVo.getFileid() == null || shareFileVo.getFilename() == null || shareFileVo.getFirstUserId() == null)
            return Result.error("502","重要属性不存在");
        return Result.success(shareFileService.sharedOrLikesSimple(shareFileVo));
    }

}
