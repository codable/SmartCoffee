package com.sc.bus.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.MapsService;
import com.sc.bus.service.OrderService;
import com.sc.model.Location;
import com.sc.model.LocationWrapper;
import com.sc.model.Maps;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MapsService mapsService;
    
    /*
     * Client will call it to get all locations' maps.
     */
    @RequestMapping(value = "/maps", method = RequestMethod.GET)
    public @ResponseBody List<Maps> getAllLocationMap() {
    	return mapsService.findAll();
    }
    
    /*
     * Gateway will push data to backend(me).
     */
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
    
}
