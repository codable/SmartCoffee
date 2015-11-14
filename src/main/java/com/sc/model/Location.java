package com.sc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Location {
	@Id
	private String id;
	private String locationId;	//that is table id
	@Indexed(unique = true)
	private String cardId;

	private String locationDesc;

	public Location() {}

	public Location(String locationId, String cardId, String locationDesc) {
		this.locationId = locationId;
		this.cardId = cardId;
		this.locationDesc = locationDesc;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", locationId=" + locationId
				+ ", cardId=" + cardId + ", locationDesc=" + locationDesc + "]";
	}
	
		
	
	
}
