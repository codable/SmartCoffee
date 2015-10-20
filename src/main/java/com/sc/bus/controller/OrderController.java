package com.sc.bus.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.sc.model.Menu;
import com.sc.model.MenuWrapper;
import com.sc.model.Order;
import com.sc.model.OrderLocation;
import com.sc.util.Constants.OrderUpdateStatus;


@Controller
@RequestMapping(value = "/order")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
    @Autowired
    private OrderService orderService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MemoryService memoryService;
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String lastModified = formatter.format(new Date());

    /*
     * Client will call it at startup to get all orders' location
     * Different Card Id could map to the same location ID, Client should warn waiter to the Card.
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<OrderLocation> getAllOrdersLocation(final HttpServletResponse response) {
    	
    	response.setHeader("Last-Modified", lastModified);
    	
    	List<OrderLocation> locationOrderList = new ArrayList<OrderLocation>();
    	List<Location> locations = locationService.findAll();
    	// TODO: should change to find not finish and today's orders.
    	//List<Order> orders = orderService.findByFinishAndDate(false, new Date());
    	List<Order> orders = orderService.findAll();
    	
    	for(Location location: locations) {
    		String cardId = location.getCardId();
    		List<Order> orderList = orderService.getOrderByCardId(cardId, orders);
    		if(orderList.size() <= 0)
    			continue;
    		if(orderList.size() > 1) {
    			//Still add to list, client should warn waiter to check this abnormal status.
    			logger.warn("Abnormal status, Not finish and today's order count should be only one!");
    		}
    		for(Order order: orderList) {
				OrderLocation orderLocation = new OrderLocation(order, location, OrderUpdateStatus.NOTUSED);
	    		locationOrderList.add(orderLocation);
			}
    	}
    	return locationOrderList;
    }
    
    /*
     * Client will call it every x seconds, just return modified data.
     */
    @RequestMapping(value = "/newly", method = RequestMethod.GET)
    public @ResponseBody List<OrderLocation> getNewlyOrdersLocation(
    		final HttpServletRequest request, 
    		final HttpServletResponse response) {
    	
    	List<OrderLocation> newlyOrderLocations = memoryService.getNewlyUpdatedLocations();
    	if(newlyOrderLocations.size() > 0) {
    		lastModified = formatter.format(new Date());
    	}
    	
    	String ifModifiedSince = request.getHeader("If-Modified-Since");
    	if(ifModifiedSince != null && ifModifiedSince.equals(lastModified)) {
    		System.out.println(ifModifiedSince);
        	response.setStatus(304);
        	return null;
    	}
    	
    	response.setHeader("Last-Modified", lastModified);
    	
    	List<OrderLocation> orderLocations = new ArrayList<OrderLocation>(newlyOrderLocations);
    	memoryService.clearUpdatedLocations();
    	return orderLocations;
    }
    
    /*
     * Update order
     * Precondition, order status is not finished
     */
    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public @ResponseBody Map<String, String> updateOrder(@PathVariable String orderId, @RequestBody List<Menu> menus) {
    	
    	Map<String, String> res = new HashMap<String, String>();
    	res.put("code", "0");
    	res.put("msg", "Success");
    	
    	List<Order> orders = orderService.findByOrderId(orderId);
    	if(orders.size() <= 0) {
    		res.put("code", "1");
        	res.put("msg", "Order is not exist!");
    		return res;
    	}
    	if(orders.size() > 1) {
    		logger.error("Must be ensure unique order ID");
    		res.put("code", "2");
        	res.put("msg", "Multi order ID!");
        	return res;
    	}
    	
    	Order order = orders.get(0);
    	List<Menu> existMenuList = order.getMenus();
    	for(Menu existMenu: existMenuList) {
    		// if exist menu not in requested update menus, continue 
    		List<Menu> updateMenuList = orderService.getMenuByMenuId(existMenu.getProductId(), menus);
    		if(updateMenuList.size() <= 0)
    			continue;
    		if(updateMenuList.size() > 1) {
    			res.put("code", "3");
            	res.put("msg", "Abnormal status, One updated order should not exist multi same product ID!");
            	return res;
    		}
    		int currentAmount = updateMenuList.get(0).getCurrentAmount();
    		int amount = updateMenuList.get(0).getAmount();
    		if(currentAmount < 0 || currentAmount > amount) {
    			res.put("code", "4");
            	res.put("msg", "Update amount is not correct, should not less than 0 or greater than " + amount);
    			return res;
    		}
    		// update exist menu's amount
    		existMenu.setCurrentAmount(currentAmount);
    		logger.info(existMenu.toString());
    	}
    	logger.info(existMenuList.toString());
    	order.setMenus(existMenuList);
    	// if all exist menus' amount are 0, then mark it as finish
    	if(orderService.checkMenuFinish(existMenuList)) {
    		order.setFinish(true);
    	}
    	else {
    		order.setFinish(false);
    	}
    	orderService.update(order);
    	
    	return res;
    }
	
    /*
     * Used for test
     */
    @RequestMapping(value = "/{id}/{isFinish}", method = RequestMethod.POST)
    public @ResponseBody void addOrder(@PathVariable String id, @PathVariable Boolean isFinish) {
    	List<Menu> list = new ArrayList<Menu>();
		
		Menu menu1 = new Menu("11", "Coffee", 22.0, 1, 1);
		list.add(menu1);
		
		Order order = new Order(UUID.randomUUID().toString(), id, list, new Date().getTime(), 58.0, isFinish);
		orderService.add(order);
    	Location location = new Location(id, id);
		locationService.add(location);
    }
    
    /*
     * Used for test
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public @ResponseBody void deleteOrder() {
    	orderService.drop();
    }
}
