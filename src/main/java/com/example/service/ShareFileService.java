package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.dao.CommentDao;
import com.example.dao.FileInfoDao;
import com.example.dao.ShareFileDao;
import com.example.entity.Comment;
import com.example.entity.ShareFile;
import com.example.pojo.SharedOrLikes;
import com.example.to.ShareFileTo;
import com.example.vo.ShareFileVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

@Service
public class ShareFileService {
    @Resource
    private ShareFileDao shareFileDao;
    @Resource
    private CommentDao commentDao;
    @Resource
    private FileInfoDao fileInfoDao;
    public SharedOrLikes sharedOrLikesSimple(ShareFileVo shareFileVo){
        if (shareFileDao.isSharedByFileId(shareFileVo.getFileId().longValue()) == 0){
            ShareFileTo shareFileto = new ShareFileTo();
            BeanUtil.copyProperties(shareFileVo,shareFileto, CopyOptions.create().ignoreNullValue());

            shareFileDao.shareFile(shareFileto);
            return SharedOrLikes.Share;
        }else {
            ShareFileVo shareFileVo1=  shareFileDao.selectByFileIDWithUser(shareFileVo.getFileId());
            shareFileDao.updateSharelikes(shareFileVo1);

            return SharedOrLikes.Like;
        }
    }

    @Transactional
    public int delete(Integer shareFileId){
        Example example1= new Example(ShareFile.class);
        example1.createCriteria().andCondition("fileid="+shareFileId);
        int res =  shareFileDao.deleteByExample(example1);
        if (res != 0){
            Example example = new Example(Comment.class);
            example.createCriteria().andCondition("share_file_id="+shareFileId);
            res += commentDao.deleteByExample(example);
        }
        return res;
    }
}
