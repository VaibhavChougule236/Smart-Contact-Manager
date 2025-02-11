package com.smart.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smart.Dao.UserRepository;
import com.smart.entities.User;

public class CustomUserDetails implements UserDetails {

	private User user;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	//private UserRepository userRepository;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 SimpleGrantedAuthority simplGrantedAuthority=new SimpleGrantedAuthority(user.getRole()); 
		return List.of(simplGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	public boolean isAccountNonExpired() {
		return true;
	}
	
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public boolean isEnabled() {
		return true;
	}
}
