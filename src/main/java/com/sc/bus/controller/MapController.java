package com.sc.bus.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
    		m.setFloor(maps.getFloor());
    		m.setxPos(maps.getxPos());
    		m.setyPos(maps.getyPos());
    		mapService.update(m);
    	}
		return new ResponseEntity<Maps>(maps, HttpStatus.OK);
	}
    
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public @ResponseBody void deleteMap() {
    	mapService.drop();
    }
    
    @RequestMapping(value = "/revoke", method = RequestMethod.DELETE)
    public @ResponseBody void revokeLatest() {
    	List<Maps> res = mapService.findBySort();
    	Maps maps = res.get(0);
    	mapService.delete(maps);
    }
    
    @RequestMapping(value = "/mapping", method = RequestMethod.POST)
	public @ResponseBody String updateMaps2(HttpServletRequest request) throws IOException {
    	String floors = request.getParameter("floors");
    	String total_area = request.getParameter("total_area");
    	String[] floor_begin = request.getParameterValues("floor_begin[]");
    	String[] floor_end = request.getParameterValues("floor_end[]");
    	String[] area = request.getParameterValues("area[]");
    	String[] area_begin = request.getParameterValues("area_begin[]");
    	String[] area_end = request.getParameterValues("area_end[]");
    	
    	int floorLength = Integer.parseInt(floors);
    	int areaLength = Integer.parseInt(total_area);
    	
    	if(floor_begin.length != floorLength || floor_end.length != floorLength
    			|| area.length != areaLength || area_begin.length != areaLength || area_end.length != areaLength) {
    		return "{\"res\": false}";
    	}
    	if((floor_begin.length <= 1 && floor_begin[0].equals("")) ||
    			(floor_end.length <= 1 && floor_end[0].equals("")) ||
    			(area.length <= 1 && area[0].equals("")) || 
    			(area_begin.length <= 1 && area_begin[0].equals("")) ||
    			(area_end.length <= 1 && area_end[0].equals(""))) {
    		return "{\"res\": false}";
    	}
    	for(int i = 0; i < floorLength; i++) {
    		int begin = Integer.parseInt(floor_begin[i]);
    		int end = Integer.parseInt(floor_end[i]);
    		for(int j = begin; j <= end; j++) {
	    		String locationId = j + "";
    			List<Maps> mapsList = mapService.findByLocationId(locationId);
    			String floor = String.valueOf((i+1));
    			if(mapsList.size() <=0) {
    				Maps maps = new Maps(floor, "", locationId, 0.0, 0.0);
    	    		mapService.add(maps);
    	    	}
    	    	else {
    	    		Maps m = mapsList.get(0);
    	    		
    	    		m.setFloor(floor);
    	    		mapService.update(m);
    	    	}
    		}
    	}
    	
    	for(int i = 0; i < areaLength; i++) {
    		String areaStr = area[i];
    		int begin = Integer.parseInt(area_begin[i]);
    		int end = Integer.parseInt(area_end[i]);
    		for(int j = begin; j <= end; j++) {
    			String locationId = j + "";
    			List<Maps> mapsList = mapService.findByLocationId(locationId);
    			if(mapsList.size() <=0) {
    				Maps maps = new Maps("", areaStr, locationId, 0.0, 0.0);
    	    		mapService.add(maps);
    	    	}
    	    	else {
    	    		Maps m = mapsList.get(0);
    	    		m.setArea(areaStr);
    	    		mapService.update(m);
    	    	}
    		}
    	}
    	return "{\"res\": true}";
	}
	
}
