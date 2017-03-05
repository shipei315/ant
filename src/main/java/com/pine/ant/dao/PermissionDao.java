package com.pine.ant.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pine.ant.model.Permission;

public interface PermissionDao extends JpaRepository<Permission, Long>{

}
