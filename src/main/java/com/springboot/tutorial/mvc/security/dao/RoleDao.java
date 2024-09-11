package com.springboot.tutorial.mvc.security.dao;

import com.springboot.tutorial.mvc.security.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
