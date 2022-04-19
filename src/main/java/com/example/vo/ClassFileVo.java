package com.example.vo;

import com.example.entity.ClassFile;
import lombok.Data;

@Data
public class ClassFileVo extends ClassFile {
    private String userName;
    private String fileName;
}
