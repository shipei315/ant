package com.pine.ant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pine.ant.dao.UserDao;
import com.pine.ant.model.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public List<User> findUserByName(String name){
		return userDao.findUserByName(name);
	}
}
