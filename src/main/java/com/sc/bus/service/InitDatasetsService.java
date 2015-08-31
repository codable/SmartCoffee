package com.sc.bus.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.sc.db.MongoDB;
import com.sc.model.Location;
import com.sc.model.Menu;
import com.sc.model.Order;
import com.sc.util.Constants;

public class InitDatasetsService {
	private MongoDB mongodb;
	
	public InitDatasetsService(MongoDB mongodb) {
		this.mongodb = mongodb;
	}
	
	private void saveLocation() {
		int maxL = 4;
		int maxC = 4;
        int min = 1;
        Random random = new Random();

		for(int i = 1; i < 10; i++) {
			int lid = random.nextInt(maxL) % (maxL - min + 1) + min;
			int cid = random.nextInt(maxC) % (maxC - min + 1) + min;
			Location location = new Location(String.valueOf(lid), String.valueOf(cid));
			mongodb.save(location, Constants.LocationCollectionName);
		}
	}
	
	private void saveOrder() {
		List<Menu> list1 = new ArrayList<Menu>();
		List<Menu> list2 = new ArrayList<Menu>();
		List<Menu> list3 = new ArrayList<Menu>();
		List<Menu> list4 = new ArrayList<Menu>();
		Menu menu1 = new Menu("1", "香草拿铁", 22.0, 1, 1, "1");
		Menu menu2 = new Menu("2", "美式咖啡", 18.0, 1, 2, "1");
		Menu menu3 = new Menu("3", "卡布奇诺", 12.0, 1, 3, "1");
		Menu menu4 = new Menu("4", "浓缩咖啡", 20.0, 1, 2, "1");
		Menu menu5 = new Menu("5", "伯爵红茶", 50.0, 1, 2, "2");
		Menu menu6 = new Menu("6", "草莓绿茶", 40.0, 1, 3, "2");
		Menu menu7 = new Menu("7", "恋恋抹茶", 36.0, 1, 3, "2");
		list1.add(menu1);
		list1.add(menu2);
		
		list2.add(menu3);
		list2.add(menu5);
		list2.add(menu6);
		
		list3.add(menu4);
		list3.add(menu7);
		
		list4.add(menu1);
		list4.add(menu2);
		list4.add(menu3);
		list4.add(menu4);
		list4.add(menu5);
		list4.add(menu6);
		list4.add(menu7);
		
		Order order1 = new Order("111", "1", list1, new Date().getTime(), false);
		Order order2 = new Order("222", "2", list2, new Date().getTime(), false);
		Order order3 = new Order("333", "3", list3, new Date().getTime(), false);
		Order order4 = new Order("444", "4", list4, new Date().getTime(), false);
		mongodb.save(order1, Constants.OrderCollectionName);
		mongodb.save(order2, Constants.OrderCollectionName);
		mongodb.save(order3, Constants.OrderCollectionName);
		mongodb.save(order4, Constants.OrderCollectionName);
	}
	
	public static void main(String[] args) throws Exception  {
		String confFile = Constants.DefaultConfigFile;
		Properties props = new Properties();
		props.load(new FileInputStream(confFile));
		MongoDB mongodb = new MongoDB(props);
		
		InitDatasetsService mk = new InitDatasetsService(mongodb);
		mk.saveLocation();
		mk.saveOrder();
	}
}
