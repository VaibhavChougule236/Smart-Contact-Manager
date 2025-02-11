package com.smart.controller;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.Dao.UserRepository;
import com.smart.Service.EmailService;
import com.smart.entities.User;
import com.smart.helper.message;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotController {
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	// forgot password
	@GetMapping("/forgot_password_form")
	public String forgotPassword(Model model) {
		model.addAttribute("title", "forgot password");
		return "forgotPassword_form";
	}
	
	// genrate 4 digit random otp
		//private	Random random = new Random(100000);//this generates same random numbers thats why i removed this
	private SecureRandom random = new SecureRandom();//this generates well secured and random numbers every time


	// send Otp
	// open otp verification form
	@PostMapping("/otpSend_Open_verification_form")
	public String SendverifyOtp(@RequestParam("email") String email, Model model, HttpServletRequest request) {
		// System.out.println(email);
		

		int otp = random.nextInt(999999);

		String subject = "OTP Fom Smart Contact Manager";
		String message = "<h1> OTP = " + otp + "</h1>";
		String to = email;

		boolean flag = emailService.sendEmail(to, subject, message);

		if (flag) {
			// instead of HttpSeesion in new Version of Spring boot the HttpServletRequest
			// is used....
			request.getSession().setAttribute("myotp", otp);
			request.getSession().setAttribute("email", to);
			return "otp_verification";
		} else {
			// System.out.println(otp);
			model.addAttribute("message", new message("Check Email id...!!", "alert-danger"));
			return "forgotPassword_form";
		}
	}

	// otp-verified
	@PostMapping("/otp-verified")
	public String verifiedOtp(@RequestParam("otp") int otp, Model model, HttpServletRequest request) {
		int myOtp = (int) request.getSession().getAttribute("myotp");
		String email = (String) request.getSession().getAttribute("email");
		System.out.println(email);
		if (myOtp == otp) {
			User user = this.userRepository.findByEmail(email);
			if (user == null) {
				model.addAttribute("message", new message("User does not exists with this Email... !!", "alert-danger"));
				return "forgotPassword_form";
			} else {
				return "change_password_form";
			}
		} else {
			model.addAttribute("message", new message("Please Enter Correct OTP..!!", "alert-danger"));
			return "otp_verification";
		}
	}
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("new_password") String new_password,Model model,HttpServletRequest request) {
		String email=(String)request.getSession().getAttribute("email");
		User user=this.userRepository.findByEmail(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(new_password));
		this.userRepository.save(user);
		return "redirect:/signin?change=password changed successfully...";
	}
}
