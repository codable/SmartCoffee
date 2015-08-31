package com.sc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Location {
	@Id
	private String id;
	private String locationId;
	@Indexed(unique = true)
	private String cardId;

	public Location() {}
	
	public Location(String locationId, String customerId) {
		super();
		this.locationId = locationId;
		this.cardId = customerId;
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
