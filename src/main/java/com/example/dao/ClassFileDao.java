package com.example.dao;

import com.example.entity.ClassFile;
import com.example.vo.ClassFileVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface ClassFileDao extends Mapper<ClassFile> {
    /**
     * 获得该分组下所有的文件，通过时间降序排序，形成了类似一个链表
     * @param typeId
     * @return
     */
    public List<ClassFileVo> getAllFileByTypeId(int typeId);

    /**
     * 获得该分组的最新文件（按时间）
     * @param typeId
     * @return
     */
    public ClassFile getLatestFileByTypeId(int typeId);

    /**
     * 回溯该分组下的文件，删除大于该时间节点的文件记录
     * @param typeId
     * @param date
     * @return
     */
    public int FileBack(@Param("typeId") int typeId ,@Param("date") String date);
}
