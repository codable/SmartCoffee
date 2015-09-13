package com.sc.bus.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.LocationService;
import com.sc.bus.service.OrderService;
import com.sc.model.Location;
import com.sc.model.Menu;
import com.sc.model.MenuWrapper;
import com.sc.model.Order;
import com.sc.model.OrderLocation;


@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private LocationService locationService;
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String lastModified = formatter.format(new Date());

    
    /*
     * Client will call it at startup.
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody List<OrderLocation> getAllOrdersLocation() {
    	
    	List<OrderLocation> locationOrderList = new ArrayList<OrderLocation>();
    	List<Location> locations = locationService.findAll();
    	List<Order> orders = orderService.findAll();
    	
    	for(Location location: locations) {
    		String cardId = location.getCardId();
    		Order order = orderService.getOrderByCardId(cardId, orders);
    		if(order == null)
    			continue;
			
    		OrderLocation locationOrder = new OrderLocation(order, location);
    		locationOrderList.add(locationOrder);
    	}
    	return locationOrderList;
    }
    
    /*
     * Client will call it every x seconds, just return modified data.
     * TODO: should add a memory collection to store lately changes.
     */
    @RequestMapping(value = "/newly", method = RequestMethod.GET)
    public @ResponseBody List<OrderLocation> getNewlyOrdersLocation(
    		final HttpServletRequest request, 
    		final HttpServletResponse response) {
    	response.setHeader("Last-Modified", lastModified);
    	
    	String ifModifiedSince = request.getHeader("If-Modified-Since");
    	if(ifModifiedSince != null && ifModifiedSince.equals(lastModified)) {
    		System.out.println(ifModifiedSince);
        	response.setStatus(304);
        	return null;
    	}
    	
    	List<OrderLocation> locations = new ArrayList<OrderLocation>();
    	return locations;
    }
    
    /*
     * Update order
     */
    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public @ResponseBody String updateOrder(@PathVariable String orderId, @RequestBody MenuWrapper wrapper) {
    	
    	String result = "{\"result\": \"success\"}";
    	List<Order> orders = orderService.findByOrderId(orderId);
    	if(orders.size() <= 0) {
    		return result;
    	}
    	
    	Order order = orders.get(0);
    	List<Menu> menus = order.getMenus();
    	for(Menu menu: menus) {
    		Menu m = orderService.getMenuByMenuId(menu.getProductId(), wrapper.getMenus());
    		menu.setAmount(m.getAmount());
    	}
    	order.setMenus(menus);
    	orderService.update(order);
    	
    	return result;
    }
}
