package com.sc.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Order {
	@Id
	private String id;
	@Indexed(unique = true)
	private String orderId;
	private String cardId;
	private List<Menu> menus;
	private Long orderDate;
	private Double totalPrice;
	private Boolean finish;
	private String bearName;

	public Order() {}

	public Order(String orderId, String cardId, List<Menu> menus,
			Long orderDate, Double totalPrice, Boolean finish, String bearName) {
		this.orderId = orderId;
		this.cardId = cardId;
		this.menus = menus;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.finish = finish;
		this.bearName = bearName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Boolean isFinish() {
		return finish;
	}

	public void setFinish(Boolean finish) {
		this.finish = finish;
	}


	public String getBearName() {
		return bearName;
	}

	public void setBearName(String bearName) {
		this.bearName = bearName;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderId=" + orderId + ", cardId=" + cardId + ", menus=" + menus + ", orderDate="
				+ orderDate + ", totalPrice=" + totalPrice + ", finish=" + finish + ", bearName=" + bearName + "]";
	}

	
	
	
}
