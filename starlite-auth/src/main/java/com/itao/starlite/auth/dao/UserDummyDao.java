package com.itao.starlite.auth.dao;

import java.util.List;

import com.itao.starlite.auth.User;

public class UserDummyDao implements UserDao {
	private User u = new User();
	private User u2 = new User();
	
	public UserDummyDao() {
		u.setUsername("jason");
		u.setPassword("123");
		
		u2.setUsername("starlite");
		u2.setPassword("5t4rlite");
	}
	
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public User findById(String username) {
		if (username.equals("jason"))
			return u;
		if (username.equals("starlite"))
			return u2;
		return null;
	}

	public User makePersistent(User arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeTransient(User arg0) {
		// TODO Auto-generated method stub
		
	}

}
