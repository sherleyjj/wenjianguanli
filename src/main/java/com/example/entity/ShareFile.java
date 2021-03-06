package com.example.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "shared_file")
@Data
public class ShareFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "filename")
    private String filename;
    @Column(name = "first_share_user_id")
    private Integer firstUserId;
    @Column(name = "likes")
    private Integer likes;
    @Column(name = "share_date")
    private String shareDate;
    @Column(name = "fileid")
    private Integer fileId;

    //可行
    public void setShareDate(String shareDate) {
        this.shareDate = shareDate.trim();
    }
}
