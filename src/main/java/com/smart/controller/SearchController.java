package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.Dao.UserRepository;
import com.smart.Dao.contactRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
//restcontroller is used to return the body not a view ..
public class SearchController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private contactRepository contactRepo;
//search handler
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query ,Principal principal){
		System.out.println("query :"+query);
		User user =this.userRepo.findByEmail(principal.getName());
		List<Contact> contacts=this.contactRepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
