package com.example.dao;

import com.example.entity.FileInfo;
import com.example.entity.ShareFile;
import com.example.entity.SystemFileInfoHashCode;
import com.example.to.ShareFileTo;
import com.example.vo.FileInfoVo;
import com.example.vo.HashFileInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface FileInfoDao extends Mapper<FileInfo> {
    List<FileInfoVo> findByNameAndId(@Param("name") String name,
                                     @Param("typeId") Long typeId,
                                     @Param("id") Long id);
    HashFileInfoVo isExistFileByHashCode(@Param("hashcode")String hashCode);

   /*
   *  以下与hash表有关
   *
   *
   * */

    void deleteHashByFileIdOrId(@Param("file_id")Long file_id , @Param("id")Integer id);

    int addHashCode(SystemFileInfoHashCode hashCode);

    FileInfoVo findByHash(String hash);

    int updateRef(@Param("file_id") Long file_id, @Param("nextValue") Integer nextValue);

    int selectFileRefernce(Long file_id);


    /*  以下是分享文件api*/
    /**
     * 0代表该文件没有被分享
     * @param nxfile_id
     * @return
     */
    int isSharedByFileId(Long nxfile_id);

    /**
     *分享文件到共享大厅，如果已经被分享则是点赞
     * @param shareFileTo
     * @return 0 menas that this file didn`t share
     */
    int shareFile(@Param("param1") ShareFileTo shareFileTo);


    /**
     * 后来的用户再分享只会给文件点赞
     * @return
     */
    int updateSharelikes(ShareFile shareFile);
}
