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
}
