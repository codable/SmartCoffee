package com.sc.bus.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/map")
public class MapController {
    
	@Value("${floor}")
	private String floor;
    /*
     * Client will call it at startup, to get each floor's picture
     */
    @RequestMapping(value = "/picture/{floorId}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<InputStreamResource> getMapPicture(@PathVariable String floorId) {
    	if(Integer.valueOf(floorId) > Integer.valueOf(this.floor))
    		return null;
    	
    	String imagePath = "/floor_images/" + floorId + ".png";
    	InputStream in = this.getClass().getResourceAsStream(imagePath);
    	
    	final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<InputStreamResource>(new InputStreamResource(in), headers, HttpStatus.OK);
    }
    
    /*
     * Client will call it at startup, to get floors
     */
    @RequestMapping(value = "/floors", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody String getFloors() {
    	return this.floor;
    }
}
