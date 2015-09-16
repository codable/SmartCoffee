package com.sc.bus.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.MemoryService;
import com.sc.model.Location;
import com.sc.model.LocationWrapper;
import com.sc.util.Constants;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
    @Autowired
    private LocationService locationService;
    @Autowired
    private MemoryService memoryService;
    
    /*
     * Gateway will push data to backend(me).
     */
    @RequestMapping(value="", method=RequestMethod.POST,consumes="application/json",produces="application/json")
    @ResponseBody
    public  void receiveLocaltions(@RequestBody LocationWrapper wrapper) {
		for (Location location : wrapper.getLocations()) {
			logger.info(location.toString());
			List<Location> existLocations = locationService.findByCardId(location.getCardId());
			
			String locationId = location.getLocationId();
			int size = existLocations.size();
			if(size <= 0) {
				if(!locationId.equals(Constants.LocationDeleteFLag)) {	//add
					locationService.add(location);
					memoryService.addLocation(location);
				}
			}
			else if(size == 1) {
				// TODO: should order be marked as finish here?
				Location existLocation = existLocations.get(0);
				if(locationId.equals(Constants.LocationDeleteFLag)) {	//delete
					locationService.delete(existLocation);
					memoryService.deleteLocation(location);
				}
				else {													//update
					locationService.update(existLocation);
					memoryService.updateLocation(location);
				}
			}
			else {
				logger.warn("Abnormal status, One Card Id mapping to multi locations!");
			}
			
		}
	}
    
}
