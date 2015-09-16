package com.sc.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}