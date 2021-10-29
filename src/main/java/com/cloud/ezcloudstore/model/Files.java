package com.cloud.ezcloudstore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Files {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int fileId;
	private String emailid;
	private String filename;
	private String description;
	private Long fileSize;
	private Timestamp createdtime;
	private Timestamp updatedtime;
	private String fileurl;
	
	public int getFileID() {
		return fileId;
	}
	
	public void setFileID(Integer fileID) {
		this.fileId = fileID;
	}

	public String getEmailId() {
		return emailid;
	}
	public void setEmailId(String emailid) {
		this.emailid = emailid;
	}
	public String getFileName() {
        return filename;
    }
	public void setFileName(String filename) {
        this.filename = filename;
    }
	
	public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
        this.description = description;
    }
	
	public Long getFileSize() {
        return fileSize;
    }
	public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
	
	public Timestamp getCreatedTime() {
        return createdtime;
    }
	public void setCreatedTime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }
	
	public Timestamp getUpdatedTime() {
        return updatedtime;
    }
	public void setUpdatedTime(Timestamp updatedtime) {
        this.updatedtime = updatedtime;
    }
	
	public String getFileUrl() {
		return fileurl;
	}
	
	public void setFileUrl(String fileurl) {
		this.fileurl = fileurl;
	}
	

}
