package com.example.dao;

import com.example.entity.FileInfo;
import com.example.entity.SystemFileInfoHashCode;
import com.example.vo.FileInfoVo;
import com.example.vo.HashFileInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface FileInfoDao extends Mapper<FileInfo> {
    List<FileInfoVo> findByNameAndId(@Param("name") String name,
                                     @Param("typeId") Long typeId,
                                     @Param("id") Long id);
    HashFileInfoVo isExistFileByHashCode(@Param("hashcode")String hashCode);

    int addHashCode(SystemFileInfoHashCode hashCode);

    FileInfoVo findByHash(String hash);
}
