package com.sc.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.sc.bus.dao.MapDAO;
import com.sc.model.Maps;

@Service
public class MapService {

	@Autowired
	MapDAO mapDAO;
	
	public List<Maps> findAll() {
		return mapDAO.findAll();
	}
	
	public void add(Maps map) {
		mapDAO.save(map);
	}
	
	public void delete(Maps map) {
		mapDAO.delete(map);
	}
	
	public void update(Maps map) {
		mapDAO.delete(map);
		mapDAO.save(map);
	}
	
	public List<Maps> findByLocationId(String locationId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("locationId").is(locationId));
		return mapDAO.find(query);
	}
	
	public void drop() {
		mapDAO.drop();
	}
	
	public List<Maps> findBySort() {
		Query query = new Query();
		query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"locationId"))); 
		return mapDAO.find(query);
	}
}