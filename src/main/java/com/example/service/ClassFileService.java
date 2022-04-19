package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.ResultCode;
import com.example.controller.NxSystemFileController;
import com.example.dao.ClassFileDao;
import com.example.dao.FileInfoDao;
import com.example.entity.ClassFile;
import com.example.exception.CustomException;
import com.example.vo.ClassFileVo;
import com.example.vo.FileInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class ClassFileService {
    @Resource
    private ClassFileDao classFileDao;
    @Resource
    private TypeInfoService typeInfoService;
    @Resource
    private FileInfoDao fileInfoDao;
    @Resource
    private NxSystemFileController nxSystemFileController;

    public List<ClassFileVo> getAllFileByTypeId(int typeId){
        return classFileDao.getAllFileByTypeId(typeId);
    }

    /**
     * 获得该分组的最新文件（按时间）
     * @param typeId
     * @return
     */
    public ClassFile getLatestFileByTypeId(int typeId){
        return classFileDao.getLatestFileByTypeId(typeId);
    }

    /**
     * 回溯该分组下的文件，删除大于该时间节点的文件记录
     * @param typeId
     * @param date
     * @return 回退了几个数据
     */
    public int FileBack(int typeId , String date){
        return classFileDao.FileBack(typeId,date);
    }

    /**
     * 添加文件，TODO 有可能有恶意重复添加的可能性
     * @param classFile
     * @return
     */
    public int addFileByClassFile(ClassFile classFile){
        if (BeanUtil.isEmpty(classFile,"id","uploadDate")) {
            throw new CustomException("-1","必要属性为空");
        }
        return classFileDao.insert(classFile);
    }

    public void saveExcel(HttpServletResponse response ,Integer typeId) throws IOException {

        String fileName = typeInfoService.findById(typeId.longValue()).getName();
        if (StrUtil.isEmpty(fileName)){
            throw new CustomException("-1","保存的文件名为空");
        }
        List<ClassFileVo> list = getAllFileByTypeId(typeId);
        List<List<String>> lists = new LinkedList<>();
        List head = ListUtil.list(false,"姓名","修改记录","时间");
        lists.add(head);
        for(ClassFileVo index : list){
            lists.add(ListUtil.list(false,index.getUserName(),index.getChangeRecord(),index.getUploadDate()));
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(lists, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    public Integer deleteRef(Integer fileId){

        int ref = fileInfoDao.selectFileRefernce(fileId.longValue());
        if (ref >1){
            fileInfoDao.updateRef(fileId.longValue(),ref - 1);
        }else if (ref == 1) {
            fileInfoDao.deleteHashByFileIdOrId(fileId.longValue(),null);
        }
        return ref - 1;

    }
    public void addRef(Integer fileId){
        int ref = fileInfoDao.selectFileRefernce(fileId.longValue());
        fileInfoDao.updateRef(fileId.longValue(),ref+1);
    }
    @Transactional
    public int deleteAll(Integer typeId){
        List<ClassFileVo> preres = this.getAllFileByTypeId(typeId);
        int count = preres.size();
        Example example = new Example(ClassFile.class);
        example.createCriteria().andCondition("type_id=",typeId);
        int res =  classFileDao.deleteByExample(example);
        if (res != count){
            throw new CustomException("-1","删除错误，数量不匹配");
        }
        for (ClassFileVo index : preres){
            if(this.deleteRef(index.getFileId())==0){
                nxSystemFileController.deleteFile(typeId.toString());
            }
        }
        typeInfoService.delete(typeId.longValue());
        return res;
    }
}
