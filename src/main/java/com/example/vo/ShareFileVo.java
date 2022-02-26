package com.example.vo;

import com.example.entity.ShareFile;
import lombok.Data;

@Data
public class ShareFileVo extends ShareFile {
    private String userName;
    private String typeName;
}
