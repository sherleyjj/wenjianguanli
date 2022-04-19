package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.dao.FileInfoDao;
import com.example.dao.ShareFileDao;
import com.example.entity.FileInfo;
import com.example.entity.ShareFile;
import com.example.entity.SystemFileInfoHashCode;
import com.example.exception.CustomException;
import com.example.pojo.SharedOrLikes;
import com.example.to.ShareFileTo;
import com.example.vo.FileInfoVo;
import com.example.vo.ShareFileVo;
import com.example.vo.UserInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileInfoService {

    @Resource
    private FileInfoDao fileInfoDao;
    @Resource
    private ShareFileDao shareFileDao;

    public FileInfo add(FileInfo info) {
        int ref = fileInfoDao.selectFileRefernce(info.getFileId());
        fileInfoDao.updateRef(info.getFileId(),ref+1);
        fileInfoDao.insertSelective(info);
        return info;
    }

    /**
     * 删除引用后，剩下的引用
     * @param id
     * @return 0-没有文件引用 ，value > 0代表引用大于0
     */
    public int delete(Long id) {

        List<FileInfoVo> fileInfos = fileInfoDao.findByNameAndId(null,null,id,null);
        Long file_id = fileInfos.get(0).getFileId();
        int ref = fileInfoDao.selectFileRefernce(file_id);
        if (ref >1){
            fileInfoDao.updateRef(file_id,ref - 1);
        }else if (ref == 1) {
            fileInfoDao.deleteHashByFileIdOrId(file_id,null);
        }
        fileInfoDao.deleteByPrimaryKey(id);
        return ref - 1;
    }

    public void update(FileInfo info) {
        fileInfoDao.updateByPrimaryKeySelective(info);
    }

    public FileInfoVo findById(Long id) {
        List<FileInfoVo> list = fileInfoDao.findByNameAndId(null, null,  id,null);
        if (!CollectionUtil.isEmpty(list)) {
            return list.get(0);
        }
        return new FileInfoVo();
    }

    public List<FileInfoVo> findAll() {
        return fileInfoDao.findByNameAndId("all", null, null,null);
    }

    public PageInfo<FileInfoVo> findPage(String name, Long typeId, Integer pageNum, Integer pageSize,Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfoVo> info = fileInfoDao.findByNameAndId(name, typeId, null,userId);
        return PageInfo.of(info);
    }

    public Boolean isExistSameFileByHashCode(String hashcode)  {
        if(StrUtil.isBlank(hashcode)){
            return false;
        }
        return fileInfoDao.isExistFileByHashCode(hashcode) !=null;
    }

    public SystemFileInfoHashCode addHash(SystemFileInfoHashCode hashCode){

         fileInfoDao.addHashCode(hashCode);
         return hashCode;
    }
    public FileInfoVo findByHash(String hashCode){
        if (StrUtil.isBlank(hashCode)){
            throw new CustomException("失败","HashCode Is");
        }
        return fileInfoDao.findByHash(hashCode);
    }
    //TODO
    public Integer findFileIdByHash(String code){
        return 0;
    }

    //TODO 需要重复验证
    @Deprecated
    public SharedOrLikes sharedAndLikes(UserInfoVo userInfoVo , FileInfoVo fileInfoVo){
        if (fileInfoDao.isSharedByFileId(fileInfoVo.getFileId()) == 0 ){
            //该文件没有被分享，那么直接分享
            ShareFileTo shareFileTo = new ShareFileTo(fileInfoVo,userInfoVo);
            shareFileTo.setFilename(fileInfoVo.getName());
            shareFileTo.setUserInfo(userInfoVo);
            shareFileTo.setNxfileInfo(fileInfoVo);
            fileInfoDao.shareFile(shareFileTo);
            int ref = fileInfoDao.selectFileRefernce(fileInfoVo.getFileId());
            fileInfoDao.updateRef(fileInfoVo.getFileId(),ref + 1);
            return SharedOrLikes.Share;
        }else {
            //TODO 可能会产生重复刷热度的情况
            ShareFile shareFile = shareFileDao.selectByFileIDWithUser(fileInfoVo.getFileId().intValue());
            fileInfoDao.updateSharelikes(shareFile);
            return SharedOrLikes.Like;
        }
    }

    //TODO 验证成功
    public PageInfo<ShareFileVo> findAllShareFilePage(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<ShareFileVo> info = shareFileDao.selectAllWithUser();
        return PageInfo.of(info);
    }

}
