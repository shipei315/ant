package com.pine.ant.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pine.ant.model.Role;

public interface RoleDao extends JpaRepository<Role, Long> {

}
