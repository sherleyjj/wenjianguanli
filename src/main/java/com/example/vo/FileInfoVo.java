package com.example.vo;

import com.example.entity.FileInfo;

import java.io.Serializable;

public class FileInfoVo extends FileInfo implements Serializable {

	private String typeName;

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "FileInfoVo{" +
				"typeName='" + typeName + '\'' +
				'}'+super.toString();
	}
}