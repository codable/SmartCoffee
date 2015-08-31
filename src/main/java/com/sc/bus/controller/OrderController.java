package com.sc.bus.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.OrderService;
import com.sc.model.Location;
import com.sc.model.Menu;
import com.sc.model.Order;
import com.sc.model.OrderWrapper;


@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private LocationService locationService;
    
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Order> getAllOrder() {
    	return orderService.findAll();
    }
    
    @RequestMapping(value = "/{category}", method = RequestMethod.GET)
    public @ResponseBody List<Order> getCategoryMenu(@PathVariable String category) {
    	List<Order> orders = orderService.findAll();
    	
    	for(Order order: orders) {
    		List<Menu> menus = new ArrayList<Menu>();
    		
    		boolean finish = true;
    		for(Menu menu: order.getMenus()) {
    			if(menu.getCategory().equals(category)) {
    				menus.add(menu);
    				
    				if(menu.getProcess() != 3) {
        				finish = false;
        			}
    			}
    		}
    		order.setFinish(finish);
    		order.setMenus(menus);
    	}
    	List<Order> res = new ArrayList<Order>();
    	for(Order order: orders) {
    		if(!order.getMenus().isEmpty()) {
    			res.add(order);
    		}
    	}
    	return res;
    }
    
    
    @RequestMapping(value="", method=RequestMethod.POST,consumes="application/json",produces="application/json")
    @ResponseBody
    public void receiveOrders(@RequestBody OrderWrapper wrapper) {
		for (Order order : wrapper.getOrders()) {
			System.out.println(order);
			orderService.add(order);
		}
	}
    
    //set order or menu to finish
    @RequestMapping(value = "/{orderId}/cook", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String setFinish(@PathVariable String orderId,
    		@RequestParam(value = "isReset", required = true) Integer isReset,
    		@RequestParam(value = "cids", required = true) String cids) {
    	
    	Random random = new Random();
    	List<Order> orders = orderService.findByOrderId(orderId);
    	for(Order order: orders) {
    		orderService.delete(order);
    		List<Menu> menus = order.getMenus();
    		int status = random.nextInt(2) % (2 - 1 + 1) + 1;
    		int process = (isReset == 1) ? status : 3;
    		for(Menu menu: menus) {
    			if(cids.contains(menu.getId())) {
    				menu.setProcess(process);
    			}
    		}
    		boolean isFinish = true;
    		for(Menu menu: menus) {
    			if(menu.getProcess() != 3) {
    				isFinish = false;
    				break;
    			}
    		}
    		if((isReset == 1)) {
    			isFinish = false;
    		}
    		
    		order.setFinish(isFinish);
    		orderService.add(order);
    	}
    	
    	return "{\"success\": \"ok\"}";
    }
    
    //get order's location
    @RequestMapping(value = "/{orderId}/location", method = RequestMethod.GET)
    public @ResponseBody String getLocation(@PathVariable String orderId) {
    	List<Order> orders = orderService.findByOrderId(orderId);
    	for(Order order: orders) {
    		//if(!order.isFinish()) {
    			List<Location> locations = locationService.findByCardId(order.getCardId());
    			if(!locations.isEmpty()) {
    				String res = "{\"location\": \"" + locationTransfer(locations.get(0)).getLocationId() + "\"}";
    				return res;
    			}
    		//}
    	}
    	
    	return "{\"location\": \"\"}";
    	
    }
    
    //get cardId's order
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public @ResponseBody List<Menu> getCardMenus(@RequestParam(value = "cardId", required = true) String cardId) {
    	List<Order> orders = orderService.findByCardId(cardId);
    	if(!orders.isEmpty()) {
    		return orders.get(0).getMenus();
    	}
    	return null;
    }
    
    private Location locationTransfer(Location location) {
    	String seatId = location.getLocationId() + "_" + location.getCardId();
    	return new Location(seatId, location.getCardId());
    }
}
