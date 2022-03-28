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

    /**
     * 共享或者点赞文件，共享时会添加对文件的引用
     * @param shareFileVo
     * @return
     */
    public SharedOrLikes sharedOrLikesSimple(ShareFileVo shareFileVo){
        if (!isSharedByFileId(shareFileVo.getFileId().longValue())){
            ShareFileTo shareFileto = new ShareFileTo();
            BeanUtil.copyProperties(shareFileVo,shareFileto, CopyOptions.create().ignoreNullValue());
            int ref = fileInfoDao.selectFileRefernce(shareFileVo.getFileId().longValue());
            fileInfoDao.updateRef(shareFileVo.getFileId().longValue(),ref + 1);

            shareFileDao.shareFile(shareFileto);
            return SharedOrLikes.Share;
        }else {
            ShareFileVo shareFileVo1=  shareFileDao.selectByFileIDWithUser(shareFileVo.getFileId());
            shareFileDao.updateSharelikes(shareFileVo1);

            return SharedOrLikes.Like;
        }
    }

    /**
     * 删除共享文件时，会减少引用
     * @param shareFileId
     * @return
     */
    @Transactional
    public int delete(Integer shareFileId){
        Example example1= new Example(ShareFile.class);
        example1.createCriteria().andCondition("fileid="+shareFileId);
        int res =  shareFileDao.deleteByExample(example1);
        int ref = fileInfoDao.selectFileRefernce(shareFileId.longValue());
        if (res == 0){
            throw new RuntimeException("删除数为0");
        }
        if (ref > 1){
            Example example = new Example(Comment.class);
            example.createCriteria().andCondition("share_file_id="+shareFileId);
            res += commentDao.deleteByExample(example);
            fileInfoDao.updateRef(shareFileId.longValue(),ref - 1);
            return ref-1;
        }else if(ref == 1){
            fileInfoDao.deleteHashByFileIdOrId(shareFileId.longValue(),null);
        }
        return ref;
    }
    public boolean isSharedByFileId(Long nxfileId){
        return shareFileDao.isSharedByFileId(nxfileId)>0;
    }
}
