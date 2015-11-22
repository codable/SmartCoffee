package com.sc.bus.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.ini4j.Ini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.MapService;
import com.sc.model.Maps;
import com.sc.model.NFC;


@Controller
@RequestMapping(value = "/map")
public class MapController {
	
	@Autowired
    private MapService mapService;
	
	@Value("${floor}")
	private String floor;
	
	/*
     * Client will call it to get all location maps.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Maps> getAllLocationMap() {
    	return mapService.findAll();
    }
    
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
    
    @RequestMapping(value = "/picture2/{floorId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody String getMapPicture2(@PathVariable String floorId) throws IOException {
    	if(Integer.valueOf(floorId) > Integer.valueOf(this.floor))
    		return null;
    	
    	String imagePath = "/floor_images/" + floorId + ".png";
    	InputStream in = this.getClass().getResourceAsStream(imagePath);
    	ByteArrayOutputStream bos=new ByteArrayOutputStream();
    	int b;
    	byte[] buffer = new byte[1024];
    	while((b=in.read(buffer))!=-1){
    	   bos.write(buffer,0,b);
    	}
    	byte[] fileBytes=bos.toByteArray();
    	in.close();
    	bos.close();

    	
    	byte[] encoded=Base64.getEncoder().encode(fileBytes);
    	String encodedString = new String(encoded);

    	
    	return encodedString;
    }
    
    /*
     * Client will call it at startup, to get floors
     */
    @RequestMapping(value = "/floors", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody String getFloors() {
    	return this.floor;
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<Maps> updateMaps(@RequestBody Maps maps) throws IOException {
    	String locationId = maps.getLocationId();
    	if(locationId.equals("")) {
    		return new ResponseEntity<Maps>(maps, HttpStatus.BAD_REQUEST);
    	}
    	List<Maps> mapsList = mapService.findByLocationId(locationId);
    	if(mapsList.size() <=0) {
    		mapService.add(maps);
    	}
    	else {
    		Maps m = mapsList.get(0);
    		m.setxPos(maps.getxPos());
    		m.setyPos(maps.getyPos());
    		mapService.update(m);
    	}
		return new ResponseEntity<Maps>(maps, HttpStatus.OK);
	}
	
}
