package com.example.entity;

import javax.persistence.*;

@Table(name = "system_file_info_hashcode")
public class SystemFileInfoHashCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hash_code")
    private String filehashCode;

    @Column(name = "system_file_info_id")
    private Long fileId;

    public Long getFileId() {
        return fileId;
    }

    public Long getId() {
        return id;
    }

    public String getFileHashCode() {
        return filehashCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilehashCode(String filehashCode) {
        this.filehashCode = filehashCode;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}