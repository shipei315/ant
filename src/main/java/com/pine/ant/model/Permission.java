package com.pine.ant.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ant_permission")
public class Permission {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String permission;
	
    @ManyToMany
    @JoinTable(name = "ant_permission_role", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = {
        @JoinColumn(name = "role_id") })
    private List<Role> roleList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
