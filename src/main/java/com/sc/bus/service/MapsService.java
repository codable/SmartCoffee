package com.sc.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc.bus.dao.MapsDAO;
import com.sc.model.Maps;

@Service
public class MapsService {

	@Autowired
	MapsDAO mapsDAO;
	
	public List<Maps> findAll() {
		return mapsDAO.findAll();
	}
	
	public void add(Maps maps) {
		mapsDAO.save(maps);
	}
	
	public void delete(Maps maps) {
		mapsDAO.delete(maps);
	}
}