package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "CONTACTS")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String nickName;
    private String work;
    private String email;
    private String phone;
    private String imgUrl;
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
public Contact() {
	super();
	// TODO Auto-generated constructor stub
}
public Contact(int id, String name, String nickName, String work, String email, String phone, String imgUrl,
		String description) {
	super();
	id = id;
	this.name = name;
	this.nickName = nickName;
	this.work = work;
	this.email = email;
	this.phone = phone;
	this.imgUrl = imgUrl;
	this.description = description;
}

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public int getCId() {
	return id;
}
public void setCId(int cId) {
	id = cId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getNickName() {
	return nickName;
}
public void setNickName(String nickName) {
	this.nickName = nickName;
}
public String getWork() {
	return work;
}
public void setWork(String work) {
	this.work = work;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getImgUrl() {
	return imgUrl;
}
public void setImgUrl(String imgUrl) {
	this.imgUrl = imgUrl;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
@Override
public String toString() {
	return "Contacts [CId=" + id + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email=" + email
			+ ", phone=" + phone + ", imgUrl=" + imgUrl + ", description=" + description + "]";
}

}
