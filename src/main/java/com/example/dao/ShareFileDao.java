package com.example.dao;

import com.example.entity.ShareFile;
import com.example.vo.ShareFileVo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ShareFileDao extends Mapper<ShareFile> {
    public ShareFileVo selectByFileIDWithUser(Integer id);
    public List<ShareFileVo > selectAllWithUser();
}
