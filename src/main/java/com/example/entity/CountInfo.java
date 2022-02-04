package com.example.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "count_info")
public class CountInfo {
    @Column(name = "userCount")
    private Integer userCount;
    @Column(name = "fileCount")
    private Integer fileCount;
    @Column(name = "adminCount")
    private Integer adminCount;
    @Column(name = "typeCount")
    private Integer typeCount;
}
