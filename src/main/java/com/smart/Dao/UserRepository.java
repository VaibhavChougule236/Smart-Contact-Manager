package com.smart.Dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository <User,Integer>{
//@Query("select u from User u.email= :email")
//public User getUserByName(@Param("email") String email);
//	@Query("SELECT u FROM User u WHERE u.email = :email")
//	public User getUserByName(@Param("email") String email);

	
	public User findByEmail(String email);

}