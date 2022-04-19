package com.example.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "class_file_info")
public class ClassFile {
    @Id
    private Integer id;
    @Column(name = "file_id")
    private Integer fileId;
    @Column(name = "user_id")
    private Integer UserId;
    @Column(name = "type_id")
    private Integer typeId;
    @Column(name = "change_record")
    private String changeRecord;

    @Column(name = "upload_date",insertable = false)
    private String uploadDate;

}
