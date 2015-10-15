package com.sc.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sc.db.IBaseDB;
import com.sc.model.Location;
import com.sc.util.Constants;

@Repository
public class LocationDAO {
	
	@Autowired
	IBaseDB baseDB;
	
	public List<Location> findAll() {
		return baseDB.findAll(Location.class, Constants.LocationCollectionName);
	}
	
	public void delete(Location location) {
		baseDB.delete(location, Constants.LocationCollectionName);
	}
	
	public int count(Query query) {
		return (int) baseDB.count(query, Constants.LocationCollectionName);
	}
	
	public void save(Location location) {
		baseDB.save(location, Constants.LocationCollectionName);
	}

	public Location findOne(Query query) {
		return baseDB.findOne(query, Location.class, Constants.LocationCollectionName);
	}

	public List<Location> find(Query query) {
		return baseDB.find(query, Location.class, Constants.LocationCollectionName);

	}
	
	public void drop() {
		baseDB.drop(Constants.LocationCollectionName);
	}

}
