package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.TypeInfo;
import com.example.service.*;
import com.example.vo.TypeInfoVo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/typeInfo")
public class TypeInfoController {
    @Resource
    private TypeInfoService typeInfoService;
    @Resource
    private ClassFileService classFileService;

    @PostMapping
    public Result<TypeInfo> add(@RequestBody TypeInfoVo typeInfo) {
        typeInfoService.add(typeInfo);
        return Result.success(typeInfo);
    }

    /**
     * 删除分类之后，就会删除分组下的所有文件（新功能）
     * @param id
     * @return
     */
    @Deprecated
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        typeInfoService.delete(id);
        classFileService.deleteAll(id.intValue());
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody TypeInfoVo typeInfo) {
        typeInfoService.update(typeInfo);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TypeInfo> detail(@PathVariable Long id) {
        TypeInfo typeInfo = typeInfoService.findById(id);
        return Result.success(typeInfo);
    }

    @GetMapping
    public Result<List<TypeInfoVo>> all() {
        return Result.success(typeInfoService.findAll());
    }

    @GetMapping("/page/{name}")
    public Result<PageInfo<TypeInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(typeInfoService.findPage(name, pageNum, pageSize, request));
    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        List<TypeInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(TypeInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<TypeInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getName())).collect(Collectors.toList());
            for (TypeInfo info : resultList) {
                typeInfoService.add(info);
            }
        }
        return Result.success();
    }

    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        List<List<String >> lists = new ArrayList<List<String>>();
        List list1 = new ArrayList<>();
        list1.add("name");
        list1.add("time");
        list1.add("record");
        List list2 = new ArrayList();
        list2.add("江德鸿");
        list2.add("18");
        list2.add("hello world");

        lists.add(list1);
        lists.add(list2);
        lists.add(list2);
        lists.add(list2);
        lists.add(list2);
        //实验成功一个list为一行
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(lists, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=typeInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
}
