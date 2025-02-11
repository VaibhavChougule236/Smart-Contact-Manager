package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.smart.Dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController implements ErrorController {
	@Autowired
	private BCryptPasswordEncoder  passwordEncoder;
	@Autowired
	private UserRepository userRepository;

//	@GetMapping("/error")
//	@ResponseBody
//	public String handleError() {
//		return "Error occurred";
//	}

	
	@GetMapping("/base")

	public String base() {
		return "base";
	}
//Home Page 
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}
//About Page 
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}

	//Registration Page
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup-Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	//Login Page
	
	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "Login-Smart Contact Manager");
		return "login";
	}
	
	
	
	//Doing Registration
	
	@RequestMapping(value = "/do_Register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		
		
		try {
			if (!agreement) {
				throw new Exception("You have not accepted the terms and conditions");
			}
			if (result1.hasErrors()) {
			    System.out.println("Validation errors: " + result1.toString());
			    model.addAttribute("user", user);
			    return "signup";  // Return the same view to show errors without redirecting
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImgUrl("img.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			

			System.out.println("Agreement: " + agreement);
			System.out.println("User: " + user);

			User result = this.userRepository.save(user);

			// Clear the form and show success message on the same signup page
			model.addAttribute("user", new User());
			redirectAttributes.addFlashAttribute("message", new message("Successfully Registered !!", "alert-success"));

			return "redirect:/signup"; // Redirect only for success to clear the form
		} catch (Exception e) {
			e.printStackTrace();

			// On error, just return the same view without redirecting
			model.addAttribute("user", user);
			model.addAttribute("message", new message("Something went wrong !! " + e.getMessage(), "alert-danger"));

			return "signup"; // Return view name directly to stay on the same page
		}
	}
}
