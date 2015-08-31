package com.sc.model;

import java.util.List;

public class LocationWrapper {
	private List<Location> locations;

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	@Override
	public String toString() {
		return "LocationWrapper [locations=" + locations + "]";
	}
	
	
	
	
}
