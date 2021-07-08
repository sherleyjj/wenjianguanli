package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.common.ResultCode;
import com.example.dao.NxSystemFileInfoDao;
import com.example.entity.NxSystemFileInfo;
import com.example.entity.SystemFileInfoHashCode;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NxSystemFileInfoService {

    @Value("${authority.info}")
    private String authorityInfo;

    @Autowired
    private NxSystemFileInfoDao nxSystemFileInfoDao;

    @Autowired
    private FileInfoService fileInfoService;

    @Transactional
    public NxSystemFileInfo add(@NonNull String hashcode, NxSystemFileInfo nxSystemFileInfo) {
        if(StrUtil.isBlank(hashcode)){
            throw new RuntimeException("HashCode Is Null");
        }
        if (fileInfoService.isExistSameFileByHashCode(hashcode)){
            throw new CustomException("失败","相同文件以及存在");
        }
        nxSystemFileInfoDao.insertSelective(nxSystemFileInfo);
        SystemFileInfoHashCode systemFileInfoHashCode = new SystemFileInfoHashCode();
        systemFileInfoHashCode.setFilehashCode(hashcode);
        systemFileInfoHashCode.setFileId(nxSystemFileInfo.getId());
        fileInfoService.add(systemFileInfoHashCode);
        return nxSystemFileInfo;
    }

    public void delete(Long id) {
        nxSystemFileInfoDao.deleteByPrimaryKey(id);
    }

    public void update(NxSystemFileInfo nxSystemFileInfo) {
        nxSystemFileInfoDao.updateByPrimaryKeySelective(nxSystemFileInfo);
    }

    public NxSystemFileInfo findById(Long id) {
        return nxSystemFileInfoDao.selectByPrimaryKey(id);
    }
    
    public NxSystemFileInfo findByFileName(String name) {
        return nxSystemFileInfoDao.findByFileName(name);
    }

    public List<NxSystemFileInfo> findAll() {
        return nxSystemFileInfoDao.findByName("all");
    }

    public PageInfo<NxSystemFileInfo> findPage(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NxSystemFileInfo> all = nxSystemFileInfoDao.findByName(name);
        return PageInfo.of(all);
    }
}
