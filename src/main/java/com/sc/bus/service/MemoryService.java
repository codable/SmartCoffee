package com.sc.bus.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc.model.Location;
import com.sc.model.Order;
import com.sc.model.OrderLocation;
import com.sc.util.Constants.OrderUpdateStatus;

@Service
public class MemoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemoryService.class);

	@Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;
	
    private List<OrderLocation> newlyUpdatedLocations = new ArrayList<OrderLocation>();
    
	public List<OrderLocation> getNewlyUpdatedLocations() {
		return this.newlyUpdatedLocations;
	}
	
	public void clearUpdatedLocations() {
		newlyUpdatedLocations.clear();
	}
	
	/*
	 * Add:    First come order, then location, so OrderUpdateStatus for add is 'UPDATE'
	 * Delete: Waiter has take the card to bar, delete the location, could be think as order has delivered
	 * Update: User change seat or waiter in the middle of the take card away
	 */
	public void updateLocation(Location location, OrderUpdateStatus status) {
		List<Order> orders = orderService.findByCardId(location.getCardId());
		int size = orders.size();
		if(size <= 0) {
			// In the add operation, if no order, it's not standard operation
			logger.warn("No such order, nothing to update");
			return;
		}
		else if(size == 1) {
			Order order = orders.get(0);
			OrderLocation orderLocation = new OrderLocation(order, location, status);
			newlyUpdatedLocations.add(orderLocation);
		}
		else if(size > 1) {
			// TODO: how to deal with this.
			logger.warn("Abnormal status, Not finish and today's order count should be only one!");
		}
	}
	
	
	/*
	 * User place an order, then sit down, so first come order data, then location data
	 */
	public void addOrder(Order order) {
		List<Location> locations = locationService.findByCardId(order.getCardId());
		int size = locations.size();
		
		Location location = null;
		if(size <= 0) {
			logger.info("User has not sit down, add to memory to dispaly first");
		}
		/*
		else if(size == 1) {
			logger.warn("Not a standard operation, first come order data, then location data");
			location = locations.get(0);
		}
		else if(size > 1) {
			logger.warn("Multi Card Id mapping found");
			return;
		}
		*/
		else {
			logger.warn("Not a standard operation, first come order data, then location data");
			return;
		}
		OrderLocation orderLocation = new OrderLocation(order, location, OrderUpdateStatus.ADD);
		newlyUpdatedLocations.add(orderLocation);
		
	}
}