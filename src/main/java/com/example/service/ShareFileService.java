package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.dao.FileInfoDao;
import com.example.dao.ShareFileDao;
import com.example.pojo.SharedOrLikes;
import com.example.to.ShareFileTo;
import com.example.vo.ShareFileVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShareFileService {
    @Resource
    private ShareFileDao shareFileDao;
    @Resource
    private FileInfoDao fileInfoDao;
    public SharedOrLikes sharedOrLikesSimple(ShareFileVo shareFileVo){
        if (shareFileDao.isSharedByFileId(shareFileVo.getFileid().longValue()) == 0){
            ShareFileTo shareFileto = new ShareFileTo();
            BeanUtil.copyProperties(shareFileVo,shareFileto, CopyOptions.create().ignoreNullValue());

            shareFileDao.shareFile(shareFileto);
            return SharedOrLikes.Share;
        }else {
            ShareFileVo shareFileVo1=  shareFileDao.selectByFileIDWithUser(shareFileVo.getFileid());
            shareFileDao.updateSharelikes(shareFileVo1);

            return SharedOrLikes.Like;
        }
    }
}
