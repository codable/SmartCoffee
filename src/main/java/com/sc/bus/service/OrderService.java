package com.sc.bus.service;

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
	
	public Order getOrderByCardId(String cardId, List<Order> orders) {
    	for(Order order: orders) {
    		if(order.getCardId().equals(cardId)) {
    			return order;
    		}
    	}
    	return null;
    }
	
	public Menu getMenuByMenuId(String menuId, List<Menu> menus) {
    	for(Menu menu: menus) {
    		if(menu.getProductId().equals(menuId)) {
    			return menu;
    		}
    	}
    	return null;
    }
}