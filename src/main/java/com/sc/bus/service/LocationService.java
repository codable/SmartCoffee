package com.sc.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.sc.bus.dao.LocationDAO;
import com.sc.model.Location;

@Service
public class LocationService {

	@Autowired
	LocationDAO locationDAO;
	
	public List<Location> findAll() {
		return locationDAO.findAll();
	}
	
	public void add(Location location) {
		locationDAO.save(location);
	}
	
	public void delete(Location location) {
		locationDAO.delete(location);
	}
	
	public void update(Location location) {
		locationDAO.delete(location);
		locationDAO.save(location);
	}
	
	public void drop() {
		locationDAO.drop();
	}
	
	public List<Location> findByCardId(String cardId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cardId").is(cardId));
		return locationDAO.find(query);
	}
}