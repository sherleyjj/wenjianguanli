package com.example.pojo;

public enum SharedOrLikes {
    Share("分享文件"),Like("点赞文件");
    private String value;
    private SharedOrLikes(String name){
        this.value =name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
