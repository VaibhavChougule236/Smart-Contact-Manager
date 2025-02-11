package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotBlank(message="Name should not be empty")
	private String name;
	@Column(unique=true)
	private String email;
	private String password;
	private String role;
	private String imgUrl;
	private boolean enabled;
	@Column(length=500)
	private String about;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id, String name, String email, String password, String role, String imgUrl, boolean enabled,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.imgUrl = imgUrl;
		this.enabled = enabled;
		this.about = about;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	@Override
	public String toString() {
		return "User [Id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", imgUrl=" + imgUrl + ", enabled=" + enabled + ", about=" + about + "]";
	}

}
/*
 * Matching an Email Address:
 * \b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b Matching a Phone Number:
 * \d{3}-\d{3}-\d{4} (e.g., 123-456-7890) Matching a URL:
 * https?://(www\.)?[A-Za-z0-9.-]+\.[A-Za-z]{2,6}
 */