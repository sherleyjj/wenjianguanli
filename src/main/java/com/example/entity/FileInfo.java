package com.example.entity;

import javax.persistence.*;

@Table(name = "file_info")
public class FileInfo  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "name")
	private String name;
	@Column(name = "time")
	private String time;
	@Column(name = "typeId")
	private Long typeId;
	@Column(name = "fileName")
	private String fileName;
	@Column(name = "fileId")
	private Long fileId;
	@Column(name = "userName")
	private String userName;
	@Column(name = "level")
	private Integer level;
	@Column(name = "uploadUserId")
	private Long uploadUserId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getUploadUserId() {
		return uploadUserId;
	}
	public void setUploadUserId(Long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }

	@Override
	public String toString() {
		return "FileInfo{" +
				"id=" + id +
				", name='" + name + '\'' +
				", time='" + time + '\'' +
				", typeId=" + typeId +
				", fileName='" + fileName + '\'' +
				", fileId=" + fileId +
				", userName='" + userName + '\'' +
				", level=" + level +
				", uploadUserId=" + uploadUserId +
				'}';
	}
}
