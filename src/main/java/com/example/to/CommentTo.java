package com.example.to;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class CommentTo {

    private Integer id;
    private Integer userId;
    private Integer fileId;
    private String comment;

}
