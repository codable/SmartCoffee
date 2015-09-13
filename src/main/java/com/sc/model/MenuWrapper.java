package com.sc.model;

import java.util.List;

public class MenuWrapper {
	private List<Menu> menus;

	@Override
	public String toString() {
		return "MenuWrapper [menus=" + menus + "]";
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	
	
}
