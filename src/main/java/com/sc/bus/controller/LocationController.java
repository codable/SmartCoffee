package com.sc.bus.controller;


import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.MemoryService;
import com.sc.model.Location;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

	
    @Autowired
    private LocationService locationService;
    @Autowired
    private MemoryService memoryService;
    
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
    	List<Location> locations = locationService.findByCardId(cardId);
    	locationService.delete(locations.get(0));
    	Location location = new Location(id, cardId, MemoryService.getMappingColor(cardId));
		locationService.add(location);
    }
    
	/*
     * Used for test
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public @ResponseBody void deleteLocation() {
    	locationService.drop();
    }
    
    /*
     * Used for test
     */
    @RequestMapping(value = "/blue", method = RequestMethod.POST)
    public @ResponseBody String blueFrequencyTest(@RequestParam(value="pdata", required=true) String pdata) {
    	String[] data = pdata.split("\n");
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for(String records: data) {

			List<Location> locations = parseMessage2(records);

			for(Location location: locations) {
				String tableId = location.getCardId();
				String frequency = location.getLocationId();
				//System.out.println(tableId + ": " + frequency);
				List<String> list = result.get(tableId);
				if(list == null) {
					list = new ArrayList<String>();
				}
				
				list.add(frequency);
				result.put(tableId, list);
			}
		}
		int max = 0;
		for(String key: result.keySet()) {
			int size = result.get(key).size();
			if(size > max) {
				max = size;
			}
		}
		StringBuffer sb =new StringBuffer();
		for(int i=max; i> 0; i--) {
			sb.append(i*5 + ",");
		}
		sb.append("<br>");
		for(String key: result.keySet()) {
			sb.append(key + ",");
			for(String item: result.get(key)) {
				sb.append(item + ",");
			}
			sb.append("<br>");
		}
		return sb.toString();
    }
    
    private static List<Location> parseMessage2(String message) {		
		int count = message.length() / 20;
		List<Location> list = new ArrayList<Location>();
		for(int i = 0; i < count; i++) {
			int cardBegin = 12;
			int cardEnd = 16;
			int tableBegin = 16;
			int tableEnd = 20;
			String cardId = message.substring(cardBegin, cardEnd);
			cardId = String.valueOf(Integer.parseInt(cardId, 16));
			String tableId = message.substring(tableBegin, tableEnd);
			tableId = String.valueOf(Integer.parseInt(tableId, 16));
			//String locationDesc = MemoryService.getMappingColor(cardId);
			Location location = new Location(tableId, cardId, null);
			list.add(location);
			message = message.substring(20, message.length());
		}
		return list;
	}
}
