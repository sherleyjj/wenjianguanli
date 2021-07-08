package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dao.FileInfoDao;
import com.example.entity.FileInfo;
import com.example.entity.SystemFileInfoHashCode;
import com.example.exception.CustomException;
import com.example.vo.FileInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileInfoService {

    @Resource
    private FileInfoDao fileInfoDao;

    public FileInfo add(FileInfo info) {
        fileInfoDao.insertSelective(info);
        return info;
    }

    public void delete(Long id) {
        fileInfoDao.deleteByPrimaryKey(id);
    }

    public void update(FileInfo info) {
        fileInfoDao.updateByPrimaryKeySelective(info);
    }

    public FileInfoVo findById(Long id) {
        List<FileInfoVo> list = fileInfoDao.findByNameAndId(null, null,  id);
        if (!CollectionUtil.isEmpty(list)) {
            return list.get(0);
        }
        return new FileInfoVo();
    }

    public List<FileInfoVo> findAll() {
        return fileInfoDao.findByNameAndId("all", null, null);
    }

    public PageInfo<FileInfoVo> findPage(String name, Long typeId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfoVo> info = fileInfoDao.findByNameAndId(name, typeId, null);
        return PageInfo.of(info);
    }

    public Boolean isExistSameFileByHashCode(String hashcode)  {
        if(StrUtil.isBlank(hashcode)){
            return false;
        }
        return fileInfoDao.isExistFileByHashCode(hashcode) !=null;
    }

    public SystemFileInfoHashCode add(SystemFileInfoHashCode hashCode){

         fileInfoDao.addHashCode(hashCode);
         return hashCode;
    }
    public FileInfoVo findByHash(String hashCode){
        if (StrUtil.isBlank(hashCode)){
            throw new CustomException("失败","HashCode Is");
        }
        return fileInfoDao.findByHash(hashCode);
    }

}
