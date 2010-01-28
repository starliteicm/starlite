package com.itao.starlite.auth;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User {
	@Id
	private String username;
	private String password;
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>();
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Permission> permissions = new HashSet<Permission>();
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void addRole(Role role) {
		this.roles.add(role);
	}
	public void removeRole(Role role) {
		this.roles.remove(role);
	}
	public boolean hasPermission(Permission p) {
		for (Role r: roles) {
			if (r.hasPermission(p))
				return true;
		}
		return false;
	}
	public boolean hasPermission(String p) {
		Permission pObj = new Permission();
		pObj.setName(p);
		return hasPermission(pObj);
	}
	
	
	public Set<Permission> getPermissions(){return permissions;}
	public void setPermissions(Set<Permission> permissions){this.permissions = permissions;}
	
	public void copyPermissions(User u) {
		// TODO Auto-generated method stub
		this.permissions = u.permissions;
	}
	
	public void clearPermissions(){
		permissions = new HashSet<Permission>();
	}
	
	
	public void addPermission(String permissionName){
		Permission p = new Permission();
		p.setName(permissionName);
		p.setDescription(permissionName);
		boolean add = true;
		for(Permission perm : permissions){
		  if(permissionName.equals( perm.getName() )){
			  add = false;
		  }
		}
		if(add){
		   permissions.add(p);
		}
	}
	
	public void remPermission(String permissionName){
		Permission rem = null;
		for(Permission perm : permissions){
		  if(permissionName.equals( perm.getName() )){
			  rem = perm;
		  }
		}
		
		if(rem != null){
			permissions.remove(rem);
		}
	}
	
	public Boolean findPermission(String permissionName){
		for(Permission perm : permissions){
			if(perm.getName().equals(permissionName)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasRead(String permissionName){
		return findPermission(permissionName+"Read");
	}
	public boolean hasWrite(String permissionName){
		return findPermission(permissionName+"Write");
	}
	
}
