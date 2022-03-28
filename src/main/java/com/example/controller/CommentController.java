package com.example.controller;

import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.example.to.CommentTo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

//TODO to complete comment modal
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody CommentTo commentTo){
         int code =commentService.addComment(commentTo);

        if (code== 0){
            return Result.error(Result.error().getCode(), "添加失败");
        }else {
            return Result.success(commentTo.getId());
        }
    }
    @GetMapping("/get/{fileId}")
    public Result<List<Comment>> get(@PathVariable("fileId") Integer id){
        CommentTo comment = new CommentTo();
        comment.setFileId(id);
        return Result.success( commentService.selectAllCommentByFileId(comment));
    }
    //TODO
    @GetMapping("/delete/{commentId}/{publishUserId}")
    public Result delete(@PathVariable("commentId") Integer commentId,@PathVariable("publishUserId")Integer userId
            , HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user.getLevel()==1||user.getId().intValue() == userId) {
            //管理员权限或者本人
            commentService.deleteCommentById(commentId);
            return Result.success("删除成功");
        }
        return Result.error(ResultCode.ERROR.code,"删除失败");

    }


    @GetMapping("/like/{likeId}/{likedId}/{commentId}")
    public Result like(@PathVariable Integer likeId,@PathVariable Integer likedId,@PathVariable Integer commentId){
        int res = commentService.likes(likeId,likedId,commentId);
        return Result.success(res);
    }
}
