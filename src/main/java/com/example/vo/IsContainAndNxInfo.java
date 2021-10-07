package com.example.vo;

import com.example.entity.NxSystemFileInfo;

public class IsContainAndNxInfo extends NxSystemFileInfo {
    private Boolean isExist;

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getExist() {
        return isExist;
    }
}
