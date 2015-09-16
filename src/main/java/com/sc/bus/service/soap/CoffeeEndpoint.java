package com.sc.bus.service.soap;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.tempurl.Esalesitem;
import org.tempurl.Esalestotal;
import org.tempurl.Postesalescreate;
import org.tempurl.PostesalescreateResponse;
import org.tempurl.PostesalescreateResult;
import org.tempurl.ResponseHeader;

import com.sc.bus.service.MemoryService;
import com.sc.bus.service.OrderService;
import com.sc.model.Menu;
import com.sc.model.Order;

@Endpoint
public class CoffeeEndpoint {
	
	@Autowired
    private OrderService orderService;
	@Autowired
    private MemoryService memoryService;
	
	SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMDDHHMMSS");
	
	private static final String NAMESPACE_URI = "http://tempurl.org";
 
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "postesalescreate")
	@ResponsePayload
	public PostesalescreateResponse getPostSales(@RequestPayload Postesalescreate request) {
		System.out.println(request.toString());
		
		// 1) Get total sales info
		Esalestotal eSalesTotal = request.getAstrRequest().getEsalestotal();
		String orderId = eSalesTotal.getTxdocno();
		
		// TODO: how to get? could this value be passed from extend param?
		String cardId = "";
		
		String txDate = eSalesTotal.getTxdateYyyymmdd();
		String txTime = eSalesTotal.getTxtimeHhmmss();
		Long orderDate;
		try {
			orderDate = formatter.parse(txDate + txTime).getTime();
		} catch (ParseException e) {
			System.out.println("Parse sales time error, use current time instead. " + e.getMessage());
			orderDate = new Date().getTime();
		}
		
		BigDecimal netAmount = eSalesTotal.getNetamount();
		Double totalPrice = netAmount.doubleValue();
		
		Boolean finish = false;
		
		// 2) Get every item sales info
		List<Esalesitem> eSalesItems = request.getAstrRequest().getEsalesitems().getEsalesitem();
		List<Menu> menus = new ArrayList<Menu>();
		for(Esalesitem eSalesItem: eSalesItems) {
			String productId = eSalesItem.getItemcode();
			// TODO: how to get?
			String name = "";
			Double price = eSalesItem.getNetamount().doubleValue();
			Integer amount = eSalesItem.getQty().intValue();
			
			Menu menu = new Menu(productId, name, price, amount);
			menus.add(menu);
		}
		
		Order order = new Order(orderId, cardId, menus, orderDate, totalPrice, finish);
		orderService.add(order);
		memoryService.addOrder(order);
		
		// 3) Send response
		ResponseHeader header = new ResponseHeader();
		header.setResponsecode(0);
		header.setResponsemessage("Success");
		header.setVersion("1.0");
		
		PostesalescreateResult post = new PostesalescreateResult();
		post.setHeader(header);
		
		PostesalescreateResponse response = new PostesalescreateResponse();
		response.setPostesalescreateResult(post);

		return response;
	}
}
