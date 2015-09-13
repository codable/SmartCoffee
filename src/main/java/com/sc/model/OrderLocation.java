package com.sc.model;
public class OrderLocation {
	private Order order;
	private Location location;
	
	public OrderLocation(){} 
	
	public OrderLocation(Order order, Location location) {
		super();
		this.order = order;
		this.location = location;
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
	@Override
	public String toString() {
		return "OrderLocation [order=" + order + ", location=" + location + "]";
	}
	
	
	
}
