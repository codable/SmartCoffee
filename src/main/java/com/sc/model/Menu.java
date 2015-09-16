package com.sc.model;

public class Menu {
	private String productId;
	private String name;
	private Double price;
	private Integer amount;
	
	public Menu() {}

	public Menu(String productId, String name, Double price,
			Integer amount) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.amount = amount;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Menu [productId=" + productId + ", name=" + name
				+ ", price=" + price + ", amount=" + amount + "]";
	}

	
	

}
