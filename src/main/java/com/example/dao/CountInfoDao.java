package com.example.dao;

import com.example.entity.CountInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CountInfoDao extends Mapper<CountInfo> {
    public void upDateFileCount(Integer count);
}
