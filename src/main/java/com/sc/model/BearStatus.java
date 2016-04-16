package com.sc.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BearStatus {
	@Id
	private String id;
	private String bearName;
	private String status;
	
	public BearStatus() {}
	public BearStatus(String id, String bearName, String status) {
		super();
		this.id = id;
		this.bearName = bearName;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBearName() {
		return bearName;
	}
	public void setBearName(String bearName) {
		this.bearName = bearName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "BearStatus [id=" + id + ", bearName=" + bearName + ", status=" + status + "]";
	}
	
	
}
