package com.sc.bus.controller;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.model.Location;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

	
    @Autowired
    private LocationService locationService;
    
    @Value("${locationReceiver.port}")
	private String port;
    
    /*
     * Used for test
     */
	@RequestMapping(value = "/{data}", method = RequestMethod.POST)
	public @ResponseBody void sendSocketToAddLocation(@PathVariable String data) {
		
			try {
				String host = "127.0.0.1";
				Socket client = new Socket(host, Integer.valueOf(port));
				Writer writer = new OutputStreamWriter(client.getOutputStream());
				writer.write(data);
				writer.flush();
				writer.close();
				client.close();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/*
     * Used for test
     */
    @RequestMapping(value = "/{id}/{cardId}", method = RequestMethod.POST)
    public @ResponseBody void addLocation(@PathVariable String id, @PathVariable String cardId) {
    	Location location = new Location(id, cardId, "red");
		locationService.add(location);
    }
    
	/*
     * Used for test
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public @ResponseBody void deleteLocation() {
    	locationService.drop();
    }
}
