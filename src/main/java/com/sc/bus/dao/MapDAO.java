package com.sc.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sc.db.IBaseDB;
import com.sc.model.Maps;
import com.sc.util.Constants;

@Repository
public class MapDAO {
	
	@Autowired
	IBaseDB baseDB;
	
	public List<Maps> findAll() {
		return baseDB.findAll(Maps.class, Constants.MapsCollectionName);
	}
	
	public void delete(Maps maps) {
		baseDB.delete(maps, Constants.MapsCollectionName);
	}
	
	public int count(Query query) {
		return (int) baseDB.count(query, Constants.MapsCollectionName);
	}
	
	public void save(Maps location) {
		baseDB.save(location, Constants.MapsCollectionName);
	}

	public Maps findOne(Query query) {
		return baseDB.findOne(query, Maps.class, Constants.MapsCollectionName);
	}

	public List<Maps> find(Query query) {
		return baseDB.find(query, Maps.class, Constants.MapsCollectionName);

	}
	
	public void drop() {
		baseDB.drop(Constants.MapsCollectionName);
	}

}
