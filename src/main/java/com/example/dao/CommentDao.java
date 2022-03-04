package com.example.dao;

import com.example.entity.Comment;
import com.example.to.CommentTo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CommentDao extends Mapper<Comment> {
    public int addComment(CommentTo commentTo);
}
