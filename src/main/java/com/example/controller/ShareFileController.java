package com.example.controller;

import com.example.common.Result;
import com.example.entity.ShareFile;
import com.example.service.FileInfoService;
import com.example.vo.ShareFileVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/share")
public class ShareFileController {
    @Resource
    private FileInfoService fileInfoService;

    @GetMapping("/all/page")
    public Result<PageInfo<ShareFileVo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "5") Integer pageSize){
        return Result.success(fileInfoService.findAllShareFilePage(pageNum,pageSize));

    }

}
