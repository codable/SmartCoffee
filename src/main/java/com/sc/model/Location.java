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

	public Location() {}
	
	public Location(String locationId, String cardId) {
		super();
		this.locationId = locationId;
		this.cardId = cardId;
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
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Override
	public String toString() {
		return "Location [locationId=" + locationId + ", cardId="
				+ cardId + "]";
	}
	
	
	
	
}
