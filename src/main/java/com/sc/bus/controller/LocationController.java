package com.sc.bus.controller;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.MemoryService;
import com.sc.bus.service.OrderService;
import com.sc.model.Location;
import com.sc.model.LocationWrapper;
import com.sc.model.Order;
import com.sc.util.Constants;
import com.sc.util.Constants.OrderUpdateStatus;


@Controller
@RequestMapping(value = "/location")
public class LocationController {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
    @Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemoryService memoryService;
    
    @Value("${locationReceiver.port}")
	private String port;
    
    /*
     * Gateway will push data to backend(me).
     */
    @RequestMapping(value="", method=RequestMethod.POST,consumes="application/json",produces="application/json")
    @ResponseBody
    public  void receiveLocaltions(@RequestBody LocationWrapper wrapper) {
		for (Location location : wrapper.getLocations()) {
			logger.info(location.toString());
			String locationId = location.getLocationId();
			String cardId = location.getCardId();
			List<Location> existLocations = locationService.findByCardId(cardId);
			
			int size = existLocations.size();
			if(size <= 0) {
				if(!locationId.equals(Constants.LocationDeleteFLag)) {	//add
					locationService.add(location);
					memoryService.updateLocation(location, OrderUpdateStatus.UPDATE);
				}
			}
			else if(size == 1) {
				Location existLocation = existLocations.get(0);
				if(locationId.equals(Constants.LocationDeleteFLag)) {	//delete
					List<Order> orders = orderService.findByCardId(cardId);
					// If location deleted, could be think as order has delivered.
					if(orders.size() > 0) {
						for(Order order: orders) {
							order.setFinish(true);
							orderService.update(order);
						}
					}
					
					locationService.delete(existLocation);
					memoryService.updateLocation(existLocation, OrderUpdateStatus.DELETE);
				}
				else {													//update
					// Change seat or in the middle of the take away
					locationService.update(existLocation);
					memoryService.updateLocation(existLocation, OrderUpdateStatus.UPDATE);
				}
			}
			else {
				logger.warn("Abnormal status, One Card Id mapping to multi locations!");
			}
			
		}
	}
    
    /*
     * Used for test
     */
	@RequestMapping(value = "/{data}", method = RequestMethod.POST)
	public @ResponseBody void sendSocketToAddLocation(@PathVariable String data)
			throws UnknownHostException, IOException {
		String host = "127.0.0.1";
		Socket client = new Socket(host, Integer.valueOf(port));
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		writer.write(data);
		writer.flush();
		writer.close();
		client.close();
	}
}
