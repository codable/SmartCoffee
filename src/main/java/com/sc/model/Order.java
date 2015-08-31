package com.sc.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Order {
	@Id
	private String id;
	@Indexed(unique = true)
	private String orderId;
	private String cardId;
	private List<Menu> menus;
	private Long date;
	private boolean finish;

	public Order() {}

	public Order(String orderId, String cardId, List<Menu> menus, Long date,
			boolean finish) {
		super();
		this.orderId = orderId;
		this.cardId = cardId;
		this.menus = menus;
		this.date = date;
		this.finish = finish;
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

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", cardId=" + cardId + ", menus="
				+ menus + ", date=" + date + ", finish=" + finish + "]";
	}

	
	
	
}
