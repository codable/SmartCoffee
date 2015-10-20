package com.sc.bus.service;

import hirondelle.date4j.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.sc.bus.dao.OrderDAO;
import com.sc.model.Menu;
import com.sc.model.Order;
import com.sc.util.DateUtil;

@Service
public class OrderService {

	@Autowired
	OrderDAO orderDAO;
	
	public List<Order> findAll() {
		Query query = new Query();
		query.with(new Sort(new Sort.Order(Direction.DESC, "orderId")));
		return orderDAO.find(query);
	}
	
	public void add(Order order) {
		orderDAO.save(order);
	}
	
	public void delete(Order order) {
		orderDAO.delete(order);
	}
	
	public void update(Order order) {
		orderDAO.delete(order);
		orderDAO.save(order);
	}
	
	public void drop() {
		orderDAO.drop();
	}
	
	public List<Order> findByOrderId(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderId").is(orderId));
		return orderDAO.find(query);
	}
	
	public List<Order> findByCardId(String cardId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cardId").is(cardId));
		return orderDAO.find(query);
	}
	
	public List<Order> getOrderByCardId(String cardId, List<Order> orders) {
		List<Order> orderList = new ArrayList<Order>();
    	for(Order order: orders) {
    		if(order.getCardId().equals(cardId)) {
    			orderList.add(order);
    		}
    	}
    	return orderList;
    }
	
	public List<Menu> getMenuByMenuId(String menuId, List<Menu> menus) {
		List<Menu> menuList = new ArrayList<Menu>();
    	for(Menu menu: menus) {
    		if(menu.getProductId().equals(menuId)) {
    			menuList.add(menu);
    		}
    	}
    	return menuList;
    }
	
	public boolean checkMenuFinish(List<Menu> menus) {
		int count = 0;
		int size = menus.size();
		for(Menu menu: menus) {
    		if(menu.getCurrentAmount() == 0) {
    			count++;
    		}
    	}
		if(size == count)
			return true;
		return false;
	}

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
	public List<Order> findByFinishAndDate(Boolean isFinish, Date date) {
		Query query = new Query();
		
        String dateStr = df.format(date); 
		long beginDateSecs = DateUtil.getMilliseconds(dateStr);
		DateTime theDate = DateUtil.getDaysAfter(beginDateSecs, 1);
		long endDateSecs = DateUtil.getMilliseconds(theDate);
		
		query.addCriteria(Criteria.where("finish").is(isFinish));
		query.addCriteria(Criteria.where("date").gte(beginDateSecs));
		query.addCriteria(Criteria.where("date").lte(endDateSecs));
		return orderDAO.find(query);
	}
	
	public List<Order> findByCardIdAndFinishAndDate(String cardId, Boolean isFinish, Date date) {
		Query query = new Query();
		
        String dateStr = df.format(date); 
		long beginDateSecs = DateUtil.getMilliseconds(dateStr);
		DateTime theDate = DateUtil.getDaysAfter(beginDateSecs, 1);
		long endDateSecs = DateUtil.getMilliseconds(theDate);
		
		query.addCriteria(Criteria.where("cardId").is(cardId));
		query.addCriteria(Criteria.where("finish").is(isFinish));
		query.addCriteria(Criteria.where("date").gte(beginDateSecs));
		query.addCriteria(Criteria.where("date").lte(endDateSecs));
		return orderDAO.find(query);
	}
	
	
}