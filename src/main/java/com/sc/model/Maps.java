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

	public Maps() {}

	public Maps(String locationId, Double xPos, Double yPos) {
		this.locationId = locationId;
		this.xPos = xPos;
		this.yPos = yPos;
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
		return "Maps [id=" + id + ", locationId=" + locationId + ", xPos="
				+ xPos + ", yPos=" + yPos + "]";
	}
	
	
	
}
