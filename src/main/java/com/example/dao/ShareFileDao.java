package com.example.dao;

import com.example.entity.ShareFile;
import com.example.to.ShareFileTo;
import com.example.vo.ShareFileVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ShareFileDao extends Mapper<ShareFile> {
    public ShareFileVo selectByFileIDWithUser(Integer id);
    public List<ShareFileVo > selectAllWithUser();


    /*  以下是分享文件api*/
    /**
     * 0代表该文件没有被分享
     * @param nxfile_id
     * @return
     */
    int isSharedByFileId(Long nxfile_id);

    /**
     *分享文件到共享大厅，如果已经被分享则是点赞
     *
     * @return 0 menas that this file didn`t share
     */
    int shareFile(@Param("param1") ShareFileTo shareFileto);


    /**
     * 后来的用户再分享只会给文件点赞
     * @return
     */
    int updateSharelikes(ShareFile shareFile);
}
