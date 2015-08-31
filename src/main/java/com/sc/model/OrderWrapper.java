package com.sc.model;

import java.util.List;

public class OrderWrapper {
	private List<Order> orders;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderWrapper [orders=" + orders + "]";
	}
	
	
	
	
}
