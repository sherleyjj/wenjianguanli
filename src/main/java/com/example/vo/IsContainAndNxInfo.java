package com.example.vo;

import com.example.entity.NxSystemFileInfo;

import java.io.Serializable;

public class IsContainAndNxInfo extends NxSystemFileInfo implements Serializable {
    private Boolean isExist;

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getExist() {
        return isExist;
    }
}
