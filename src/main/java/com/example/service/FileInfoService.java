package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.dao.FileInfoDao;
import com.example.entity.FileInfo;
import com.example.entity.SystemFileInfoHashCode;
import com.example.exception.CustomException;
import com.example.vo.FileInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileInfoService {

    @Resource
    private FileInfoDao fileInfoDao;

    public FileInfo add(FileInfo info) {
        int ref = fileInfoDao.selectFileRefernce(info.getFileId());
        fileInfoDao.updateRef(info.getFileId(),ref+1);
        fileInfoDao.insertSelective(info);
        return info;
    }

    /**
     * 删除引用后，剩下的引用
     * @param id
     * @return 0-没有文件引用 ，value > 0代表引用大于0
     */
    public int delete(Long id) {

        List<FileInfoVo> fileInfos = fileInfoDao.findByNameAndId(null,null,id);
        Long file_id = fileInfos.get(0).getFileId();
        int ref = fileInfoDao.selectFileRefernce(file_id);
        if (ref >1){
            fileInfoDao.updateRef(file_id,ref - 1);
        }else if (ref == 1) {
            fileInfoDao.deleteHashByFileIdOrId(file_id,null);
        }
        fileInfoDao.deleteByPrimaryKey(id);
        return ref - 1;
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
