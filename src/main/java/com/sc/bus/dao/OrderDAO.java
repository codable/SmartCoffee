package com.sc.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sc.db.IBaseDB;
import com.sc.model.Order;
import com.sc.util.Constants;

@Repository
public class OrderDAO {
	
	@Autowired
	IBaseDB baseDB;
	
	public List<Order> findAll() {
		return baseDB.findAll(Order.class, Constants.OrderCollectionName);
	}
	
	public void delete(Order order) {
		baseDB.delete(order, Constants.OrderCollectionName);
	}
	
	public int count(Query query) {
		return (int) baseDB.count(query, Constants.OrderCollectionName);
	}
	
	public void save(Order order) {
		baseDB.save(order, Constants.OrderCollectionName);
	}

	public Order findOne(Query query) {
		return baseDB.findOne(query, Order.class, Constants.OrderCollectionName);
	}

	public List<Order> find(Query query) {
		return baseDB.find(query, Order.class, Constants.OrderCollectionName);

	}

}
