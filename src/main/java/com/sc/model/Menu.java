package com.sc.model;

public class Menu {
	private String id;
	private String name;
	private Double price;
	private Integer copies;
	private Integer process;
	private String category;
	
	public Menu() {}

	public Menu(String id, String name, Double price, Integer copies,
			Integer process, String category) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.copies = copies;
		this.process = process;
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getCopies() {
		return copies;
	}

	public void setCopies(Integer copies) {
		this.copies = copies;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", price=" + price
				+ ", copies=" + copies + ", process=" + process + ", category="
				+ category + "]";
	}

	
	
	
	
	

}
