package com.pine.ant.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pine.ant.model.User;


public interface UserDao extends JpaRepository<User, Long>{

	@Query("from User where name=?1 ")
	List<User> findUserByName(String name);
}
