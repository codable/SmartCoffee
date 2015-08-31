package com.sc.bus.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.model.Location;
import com.sc.model.LocationWrapper;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

    @Autowired
    private LocationService locationService;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Location> getAllLocaltion() {
    	List<Location> locations = new ArrayList<Location>();
    	for(Location location: locationService.findAll()) {
    		
    		locations.add(locationTransfer(location));
    	}
    	return locations;
    }
    
    @RequestMapping(value="", method=RequestMethod.POST,consumes="application/json",produces="application/json")
    @ResponseBody
    public  void receiveLocaltions(@RequestBody LocationWrapper wrapper) {
		for (Location location : wrapper.getLocations()) {
			System.out.println(location);
			List<Location> locations = locationService.findByCardId(location.getCardId());
			if(locations.isEmpty()) {
				if(!location.getLocationId().equals("0")) {
					locationService.add(location);
				}
			}
			else {
				System.out.println("update");
				if(location.getLocationId().equals("0")) {
					locationService.delete(location);
				}
				else {
					locationService.delete(locations.get(0));
					locationService.add(location);
				}
				
			}
			
		}
	}
    
    private Location locationTransfer(Location location) {
    	String seatId = location.getLocationId() + "_" + location.getCardId();
    	return new Location(seatId, location.getCardId());
    }
    
}
