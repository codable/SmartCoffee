package com.sc.model;

import com.sc.util.Constants.OrderUpdateStatus;

public class OrderLocation {
	private Order order;
	private Location location;
	private OrderUpdateStatus status;
	
	public OrderLocation(){}

	public OrderLocation(Order order, Location location,
			OrderUpdateStatus status) {
		super();
		this.order = order;
		this.location = location;
		this.status = status;
	}

	@Override
	public String toString() {
		return "OrderLocation [order=" + order + ", location=" + location
				+ ", status=" + status + "]";
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public OrderUpdateStatus getStatus() {
		return status;
	}

	public void setStatus(OrderUpdateStatus status) {
		this.status = status;
	}

	
}
