package com.sc.bus.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sc.bus.service.MemoryService;
import com.sc.bus.service.OrderService;
import com.sc.model.Order;

@Controller
@RequestMapping(value = "/bear")
public class BearController {

	private static final Logger logger = LoggerFactory.getLogger(BearController.class);
	
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemoryService memoryService;

	private static String url = "http://cloud.51hchc.com/api/bear/recycle";

    /*
     * Used for test
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addOrder(@RequestBody Order order) {
    	logger.info("receive order: " + order.toString());
    	try {
    		Assert.notNull(order, "Order is null");
        	Assert.hasLength(order.getBearName(), "BearName is null");
        	Assert.notEmpty(order.getMenus(), "Menu is null");
    	} catch(Exception e) {
    		return new JsonResponse("101", e.getMessage()); 
    	}
    	String cardId = MemoryService.getMappingCardId(order.getBearName());
    	if(cardId == null) {
    		return new JsonResponse("101", "No such bearName"); 
    	}
    	order.setFinish(false);
    	order.setCardId(cardId);
    	
		memoryService.receiveOrder(order);

    	return new JsonResponse("0", null);
    }
    
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrder() {
    	return orderService.findAll();
    }
    
    //bear/recycle
    @RequestMapping(value = "/recycle/trigger", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse triggerOrderFinish(@RequestBody Order order) {
    	logger.info("trigger recycle: " + order.toString());
    	
    	try {
    		Assert.notNull(order, "Trigger data is null");
        	Assert.hasLength(order.getOrderId(), "OrderId is null");
        	Assert.hasLength(order.getBearName(), "BearName is null");
        	Assert.notNull(order.isFinish(), "Finish status is null");
    	} catch(Exception e) {
    		return new JsonResponse("101", e.getMessage()); 
    	}
    	
    	List<Order> orders = orderService.findByOrderId(order.getOrderId());
    	if(orders.size() <= 0) {
    		logger.error("Order ID " + order.getOrderId() + " is not exist!");
    		return new JsonResponse("101", "Order is not exist"); 
    	}
    	Order existOrder = orders.get(0);
    	if(!existOrder.getBearName().equals(order.getBearName())) {
        	return new JsonResponse("101", order.getBearName() + " is not exist in order " + order.getOrderId());
    	}
    	
    	existOrder.setFinish(order.isFinish());
    	orderService.update(existOrder);
    	
    	//trigger bear recycle 
		String data = "{\"bearName\": \"" + order.getBearName() + "\", \"status\": \"0\"}";
		logger.info("Begin to trigger bear recycle");
		String res = BearController.excutePost(url, data);
		logger.info("End of trigger bear recycle, result: " + res);
    	
    	return new JsonResponse("0", null);
    }
    
	public static String excutePost(String targetURL, String data) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
		    StringEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
		    
		    HttpPost request = new HttpPost(targetURL);
		    request.setEntity(entity);
		    HttpResponse response = httpClient.execute(request);

		    System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
		    try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
    
	
	public static void main(String[] as) {
		String url = "http://cloud.51hchc.com/api/bear/recycle";
		String data = "{\"bearName\": \"红色熊1\", \"status\": \"0\"}";
		
		/*List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		paramList.add(new BasicNameValuePair("bearName", "红色熊1"));
		paramList.add(new BasicNameValuePair("status", "0"));*/

		String res = BearController.excutePost(url, data);
		System.out.println(res);
	}
	
	
    
}


