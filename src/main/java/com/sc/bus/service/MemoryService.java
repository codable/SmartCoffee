package com.sc.bus.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

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
    
    private static Map<String, String> cardMapping = new HashMap<String, String>();
    
    @PostConstruct
    private void initCardMappingInfo () throws UnsupportedEncodingException, IOException {
    	Properties prop = new Properties();
    	prop.load(new InputStreamReader(MemoryService.class.getResourceAsStream("/bear.properties"), "UTF-8"));         
    	for(Object key: prop.keySet()) {
    		Object val = prop.getProperty((String) key);
    		cardMapping.put((String)key, (String)val);
    	}
    }
    
    /*
	private static Map<String, String> cardMapping = new HashMap<String, String>() {
		
		private static final long serialVersionUID = 1L;

		{
			put("紫色熊", "4");
			put("黄色熊", "5");
			put("红色熊", "6");
			put("绿色熊", "7");
			put("蓝色熊", "8");
			put("白色熊", "9");
			put("灰色熊", "10");
			put("黑色熊", "11");
			put("咖啡色熊", "12");
			put("银色熊", "13");
		}
	};*/
	
	public static String getMappingCardId(String bearColor) {
		return cardMapping.get(bearColor);
	}
	
	public static String getMappingColor(String cardId) {
		for (String key : cardMapping.keySet()) {
		    String value = cardMapping.get(key);
		    if(value.equals(cardId)) {
		    	return key;
		    }
		}
		return null;
	}
	
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
	        	logger.warn("3 - No operation for this status.");
	            break;
		}
		/*
		List<Location> dupLocations = locationService.findByLocationId(location.getLocationId());
		if(dupLocations.size() > 1) {
			logger.warn("3 - Abnormal status(Update Location), This Card ID with multi same Location ID, Maybe multi user stay in one place!");
			status = OrderUpdateStatus.CARD_WITH_MULTI_LOCATION;
		}
		
		//Not finish and today's order
		List<Order> orders = orderService.findByCardIdAndFinishAndDate(location.getCardId(), false, new Date());
		int orderSize = orders.size();
		if(orderSize > 1) {
			// That's because receive an abnormal order, but could not lost the order.
			logger.warn("3 - Abnormal status(Update Location): This Card ID already used or received wrong Card ID!");
			status = OrderUpdateStatus.ORDER_WITH_SAME_CARD;
		}
		if(orderSize <= 0) {
			logger.warn("3 - No such order(Update Location): still save the location and notify newly update.");
			OrderLocation orderLocation = new OrderLocation(null, location, status);
			newlyUpdatedLocations.add(orderLocation);
		}
		else {
			for(Order order: orders) {
				//add to newly list
				OrderLocation orderLocation = new OrderLocation(order, location, status);
				newlyUpdatedLocations.add(orderLocation);
			}
		}*/
	}
	
	
	/*
	 * User place an order, then sit down, so first come order data, then location data
	 */
	public void receiveOrder(Order order) {
		logger.debug(order.toString());
		
		OrderUpdateStatus status = OrderUpdateStatus.ADD;
		String cardId = order.getCardId();
		if(cardId.equals(Constants.EmptyCardFlag)) {
			logger.warn("1 - Abnormal status(Receive Order): Recieved an order without card ID.");
			status = OrderUpdateStatus.ORDER_WITH_NO_CARD;
		}
		else {
			// Find today's unfinished orders, which contain specific card id.
			List<Order> orders = orderService.findByCardIdAndFinishAndDate(cardId, false, new Date());
			int size = orders.size();
			
			if(size > 0) {
				// Still save the order, but mark it as abnormal.
				status = OrderUpdateStatus.ORDER_WITH_SAME_CARD;
				logger.warn("1 - Abnormal status(Receive Order): This Card ID already used or received wrong Card ID.");
			}
		}
		
		//OrderLocation orderLocation = new OrderLocation(order, null, status);
		//newlyUpdatedLocations.add(orderLocation);
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
				updateLocation(existLocation, OrderUpdateStatus.DELETE);
			} else { // update
				// Change seat or in the middle of the take away
				existLocation.setLocationId(locationId);
				updateLocation(existLocation, OrderUpdateStatus.UPDATE);
			}
		} else {
			//This could not be happen.
			logger.warn("2 - Abnormal status(Receive Location), One Card Id should not mapping to multi locations!");
		}
	}
}