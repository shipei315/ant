package com.pine.ant.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pine.ant.model.User;
import com.pine.ant.service.UserService;

@Controller
public class GeneralController {
	
	Logger logger = LoggerFactory.getLogger(GeneralController.class);
	
	@Autowired
	UserService userService;
	
	@RequestMapping(path = "/test")
	@ResponseBody
	public String controllerTest(){
		logger.info("running in controllerTest*******");
		return "This is from spring boot!";
	}
	
	@RequestMapping(path = "/user")
	@ResponseBody
	public List<User> findUserByName(String name){
		return userService.findUserByName(name);
	}

}
