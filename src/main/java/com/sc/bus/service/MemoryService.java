package com.sc.bus.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc.model.Location;
import com.sc.model.Order;

@Service
public class MemoryService {

	@Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;
	
    private Map<String, Order> newlyUpdatedLocations;
    
    private Map<String, Order> locationOrders = new HashMap<String, Order>();
    
	@PostConstruct 
    private void init(){ 
		//initLocationOrders();
    }
	
	private void initLocationOrders() {
		List<Location> locations = locationService.findAll();
    	List<Order> orders = orderService.findAll();
    	
    	for(Location location: locations) {
    		String cardId = location.getCardId();
    		Order order = orderService.getOrderByCardId(cardId, orders);
    		if(order == null)
    			continue;
			
    		locationOrders.put(location.getLocationId(), order);
    	}
	}
	
	public Map<String, Order> getNewlyUpdatedLocations() {
		return this.newlyUpdatedLocations;
	}

	public void setNewlyUpdatedLocations(Map<String, Order> newlyUpdatedLocations) {
		this.newlyUpdatedLocations = newlyUpdatedLocations;
	}
	
	
}