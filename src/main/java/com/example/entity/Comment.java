package com.example.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

@Table(name = "user_comment")
@Data
public class Comment {
    @Id
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "share_file_id")
    private Integer fileId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "is_ai_checked")
    private Integer isAiChecked;
    @Column(name = "ai_checked_score")
    private Integer aiCheckScore;
    @Column(name = "is_administrator_checked")
    private Integer isAdmChecked;
    @Column(name = "administrator_checked_score")
    private Integer admCheckScore;
    @Column(name = "upload_date")
    private String uploadDate;
    @Column(name = "likes")
    private Integer likes;

}
