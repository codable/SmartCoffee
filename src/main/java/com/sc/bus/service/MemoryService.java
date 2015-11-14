package com.sc.bus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc.model.Location;
import com.sc.model.Order;
import com.sc.model.OrderLocation;
import com.sc.util.Constants;
import com.sc.util.Constants.OrderUpdateStatus;

@Service
public class MemoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemoryService.class);

	@Autowired
    private LocationService locationService;
    @Autowired
    private OrderService orderService;
	
    //private List<OrderLocation> newlyUpdatedLocations = new ArrayList<OrderLocation>();
    private List<OrderLocation> newlyUpdatedLocations = new ArrayList<OrderLocation>();
    
	public List<OrderLocation> getNewlyUpdatedLocations() {
		return this.newlyUpdatedLocations;
	}
	
	public void clearUpdatedLocations() {
		newlyUpdatedLocations.clear();
	}
	
	/*
	 * Add:    First come order, then location
	 * Delete: Waiter has take the card to bar, delete the location
	 * Update: User change seat or waiter in the middle of the take card away
	 */
	public void updateLocation(Location location, OrderUpdateStatus status) {
		//Not finish and today's order
		List<Order> orders = orderService.findByCardIdAndFinishAndDate(location.getCardId(), false, new Date());
		int orderSize = orders.size();
		if(orderSize <= 0) {
			// In the add operation, if no order, it's not standard operation
			//logger.warn("No such order, ignore this location data.");
			//return;
			logger.warn("No such order, nothing to do here.");
		}
		/*
		else if(orderSize == 1) {
			Order order = orders.get(0);
			OrderLocation orderLocation = new OrderLocation(order, location, status);
			newlyUpdatedLocations.add(orderLocation);
			
			switch (status) {
	            case ADD: 
	            	locationService.add(location);
	                break;
	            case UPDATE: 
	            	locationService.update(location);
	                break;
	            case DELETE: 
	            	locationService.delete(location);
	                break;
	            default: 
	            	logger.warn("No operation for this status.");
	                break;
			}
			
		}
		else if(orderSize > 1) {
			// That's because receive an abnormal order, but could not lost the order.
			logger.warn("Abnormal status, One card ID, Not finish and today's order count should be only one!");
			return;
		}*/
		
		switch (status) {
	        case ADD: 
	        	locationService.add(location);
	            break;
	        case UPDATE: 
	        	locationService.update(location);
	            break;
	        case DELETE: 
	        	locationService.delete(location);
	            break;
	        default: 
	        	logger.warn("No operation for this status.");
	            break;
		}
		if(orderSize > 1) {
			// That's because receive an abnormal order, but could not lost the order.
			logger.warn("Abnormal status, This Card ID already used or received wrong Card ID!");
			status = OrderUpdateStatus.ABNORMAL;
		}
		if(location.getCardId().equals("0")) {
			logger.warn("Abnormal status, Recieved an order without card ID!");
			status = OrderUpdateStatus.ABNORMAL;
		}
		for(Order order: orders) {
			//add to newly list
			OrderLocation orderLocation = new OrderLocation(order, location, status);
			newlyUpdatedLocations.add(orderLocation);
		}
		
	}
	
	
	/*
	 * User place an order, then sit down, so first come order data, then location data
	 */
	public void receiveOrder(Order order) {
		logger.debug(order.toString());
		
		OrderUpdateStatus status = OrderUpdateStatus.ADD;
		String cardId = order.getCardId();
		if(cardId.equals("0")) {
			logger.warn("Recieved an order without card ID.");
			status = OrderUpdateStatus.ABNORMAL;
		}
		else {
			// Find today's unfinished orders, which contain specific card id.
			List<Order> orders = orderService.findByCardIdAndFinishAndDate(cardId, false, new Date());
			int size = orders.size();
			
			if(size > 0) {
				// Still save the order, but mark it as abnormal.
				status = OrderUpdateStatus.ABNORMAL;
				logger.warn("Not a standard operation: This Card ID already used or received wrong Card ID.");
			}
		}
		
		OrderLocation orderLocation = new OrderLocation(order, null, status);
		newlyUpdatedLocations.add(orderLocation);
		orderService.add(order);
	}
	
	/*
	 * User sit down, then location data will be sent.
	 */
	public void receiveLocaltion(Location location) {
		logger.info(location.toString());
		
		String locationId = location.getLocationId();
		String cardId = location.getCardId();
		List<Location> existLocations = locationService.findByCardId(cardId);

		int locationSize = existLocations.size();
		if (locationSize <= 0) {
			if (!locationId.equals(Constants.LocationDeleteFLag)) { // add
				updateLocation(location, OrderUpdateStatus.ADD);
			}
		} else if (locationSize == 1) {
			Location existLocation = existLocations.get(0);
			if (locationId.equals(Constants.LocationDeleteFLag)) { // delete
				/*
				List<Order> orders = orderService.findByCardId(cardId);
				// If location deleted, could be think as order has delivered.
				if (orders.size() > 0) {
					for (Order order : orders) {
						order.setFinish(true);
						orderService.update(order);
					}
				}
				*/
				updateLocation(existLocation, OrderUpdateStatus.DELETE);
			} else { // update
				// Change seat or in the middle of the take away
				existLocation.setLocationId(locationId);
				updateLocation(existLocation, OrderUpdateStatus.UPDATE);
			}
		} else {
			logger.warn("Abnormal status, One Card Id should not mapping to multi locations!");
		}
	}
}