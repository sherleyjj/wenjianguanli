package com.example.to;

import com.example.entity.FileInfo;
import com.example.entity.NxSystemFileInfo;
import com.example.entity.ShareFile;
import com.example.entity.UserInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareFileTo extends ShareFile {
    private FileInfo nxfileInfo;
    private UserInfo userInfo;

    public ShareFileTo(){}
    public ShareFileTo(FileInfo nxfileInfo , UserInfo userInfo){
        this.setUserInfo(userInfo);
        this.setNxfileInfo(nxfileInfo);

        //父类
        this.setFilename(nxfileInfo.getName());
        this.setFirstUserId( userInfo.getId().intValue());
        this.setFileid(nxfileInfo.getFileId().intValue());
    }
}
