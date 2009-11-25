package com.itao.starlite.auth;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String description;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Permission> permissions = new HashSet<Permission>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void grantPermission(Permission permission) {
		permissions.add(permission);
	}
	
	public void revokePermission(Permission permission) {
		permissions.remove(permission);
	}
	public boolean hasPermission(Permission permission) {
		return permissions.contains(Permission.ALL) || permissions.contains(permission);
	}
}
