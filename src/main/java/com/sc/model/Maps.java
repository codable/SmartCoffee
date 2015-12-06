package com.sc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Maps {
	@Id
	private String id;
	@Indexed(unique = true)
	private String locationId;
	private Double xPos;
	private Double yPos;
	private String area;
	private String floor;

	public Maps() {}

	public Maps(String floor, String area, String locationId, Double xPos, Double yPos) {
		this.floor = floor;
		this.area = area;
		this.locationId = locationId;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public String getFloor() {
		return this.floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Double getxPos() {
		return xPos;
	}

	public void setxPos(Double xPos) {
		this.xPos = xPos;
	}

	public Double getyPos() {
		return yPos;
	}

	public void setyPos(Double yPos) {
		this.yPos = yPos;
	}

	@Override
	public String toString() {
		return "Maps [id=" + id + ", floor=" + floor + ", area=" + area + ", locationId=" + locationId + ", xPos="
				+ xPos + ", yPos=" + yPos + "]";
	}
	
	
	
}
