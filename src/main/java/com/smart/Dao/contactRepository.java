package com.smart.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface contactRepository extends JpaRepository<Contact, Integer> {

	@Query("SELECT c FROM Contact c WHERE c.uder.id =:userId")
    Page<Contact> findContactsByUserId(@Param("userId") int userId,Pageable pageable);
   
	//search contact
	public List<Contact> findByNameContainingAndUser(String name,User user);
}

