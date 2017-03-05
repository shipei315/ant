package com.pine.ant.model;

import java.util.ArrayList;
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
import javax.persistence.Transient;

@Entity
@Table(name = "ant_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String roleName;

    @ManyToMany
    @JoinTable(name = "ant_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
        @JoinColumn(name = "user_id") })
    private List<User> userList;
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "ant_permission_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
        @JoinColumn(name = "permission_id") })
    private List<Permission> permissionList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    @Transient
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        List<Permission> perlist = getPermissionList();
        for (Permission per : perlist) {
            list.add(per.getPermission());
        }
        return list;
    }
}
