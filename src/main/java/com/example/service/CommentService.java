package com.example.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConverter;
import cn.hutool.json.JSONObject;
import com.example.common.until.RedisUtil;
import com.example.dao.CommentDao;
import com.example.entity.Comment;
import com.example.to.CommentTo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class CommentService {
    @Resource
    private CommentDao commentDao;
    @Resource
    private RedisUtil redisUtil;
    
    private final String LIKE_SET_NAME = "liked_set";

    public int addComment(CommentTo commentTo){
        return commentDao.addComment(commentTo);
    }
    public List<Comment> selectAllCommentByFileId(CommentTo commentTo){
        Example example = new Example(Comment.class);
        example.createCriteria().andCondition("share_file_id=",commentTo.getFileId());
        example.orderBy("likes").desc();
        return commentDao.selectByExample(example);
    }

    /**
     * 似乎是成功的，删除评论需要先删除掉redis里面的点赞数据，再删除MySql里面的评论信息
     * @param commentId
     * @return
     */
    @Transactional
    public int deleteCommentById(Integer commentId){
        Example example = new Example(Comment.class);
        example.createCriteria().andCondition("id=",commentId);
        Set like = redisUtil.sGet(LIKE_SET_NAME);
        Stream<String> res = like.stream().filter(new Predicate() {
            @Override
            public boolean test(Object o) { //包含commentId的都没被删除
                String value = ((String) o);
                if (value.contains("\"commentId\":"+String.valueOf(commentId)))
                    return true;
                else
                    return false;
            }
        });

        Iterator index = res.iterator();
        while (index.hasNext()){
            redisUtil.setRemove(LIKE_SET_NAME,index.next());
        }

//        return 0;
        return commentDao.deleteByExample(example);
    }

    /**
     * 点赞或者取消点赞
     * @param likeId
     * @param likedId
     * @param commentId
     * @return 0 代表点赞失败了,1为点赞，-1为取消点赞
     */
    @Transactional
    public int likes(Integer likeId,Integer likedId,Integer commentId){
        int flag;
        JSONObject jsonObject = new JSONObject().set("likeId",likeId);
        jsonObject.set("likedId",likedId);
        jsonObject.set("commentId",commentId);

        Example example = new Example(Comment.class);
        example.createCriteria().andCondition("id=",commentId).andCondition("user_id = ",likedId);
        Comment comment =  commentDao.selectOneByExample(example);
        if (comment == null){
            return 0;
        }

        Set like = redisUtil.sGet(LIKE_SET_NAME);
        if (like.contains(jsonObject.toString())) {
            //已经点赞，取消点赞
              if (redisUtil.setRemove(LIKE_SET_NAME, jsonObject.toString())==0){
                  return 0;
              }
            comment.setLikes(comment.getLikes()-1);
              flag = -1;
        }else {
            comment.setLikes(comment.getLikes()+1);
//            like.addHash(jsonObject.toString());
            redisUtil.sSet(LIKE_SET_NAME,jsonObject.toString());
            flag = 1;
        }
        Example example2 = new Example(Comment.class);
        example2.createCriteria().andCondition("id="+commentId);
        commentDao.updateByExample(comment, example2);
        return flag;
    }
}
