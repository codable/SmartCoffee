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
		
		Menu menu1 = new Menu("11", "Coffee", 22.0, 1, 1);
		Menu menu2 = new Menu("12", "America Coffee", 18.0, 1, 1);
		Menu menu3 = new Menu("13", "Capuchino", 12.0, 1, 1);
		Menu menu4 = new Menu("14", "Compresso", 20.0, 1, 1);
		Menu menu5 = new Menu("15", "Red Tea", 50.0, 1, 1);
		Menu menu6 = new Menu("16", "Green Tea", 40.0, 1, 1);
		list1.add(menu1);
		list1.add(menu2);
		
		list2.add(menu3);
		list2.add(menu5);
		list2.add(menu6);
		
		list3.add(menu4);
		
		list4.add(menu1);
		list4.add(menu2);
		list4.add(menu3);
		list4.add(menu4);
		list4.add(menu5);
		list4.add(menu6);
		
		Order order1 = new Order("111", "1", list1, new Date().getTime(), 58.0, false);
		Order order2 = new Order("222", "2", list2, new Date().getTime(), 632.2, false);
		Order order3 = new Order("333", "3", list3, new Date().getTime(), 12.2, false);
		Order order4 = new Order("444", "4", list4, new Date().getTime(), 66.3, false);
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
