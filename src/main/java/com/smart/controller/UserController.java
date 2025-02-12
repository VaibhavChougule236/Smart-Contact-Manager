
package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.razorpay.*;

import com.smart.Dao.UserRepository;
import com.smart.Dao.contactRepository;
import com.smart.Service.ContactService;
import com.smart.Service.OrderService;
import com.smart.entities.Contact;
import com.smart.entities.Orders;
import com.smart.entities.User;
import com.smart.helper.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private contactRepository contactRepo;

	@Autowired
	private ContactService contactService;
	
	@Autowired
	private OrderService orderService;


	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.findByEmail(name);
		model.addAttribute("user", user);
	}

	// Dashboard home

	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashbord";
	}

	// Open add Contact Form

	@GetMapping("/add-contact")
	public String openFormToAddContact(Model model) {
		model.addAttribute("title", "Add New Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	// Adding Contact

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact,
			@RequestParam("Photo") MultipartFile Photo, BindingResult result, Model model, Principal principal,
			HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
		try {
			if (contact.getName() == null || contact.getName().isEmpty() || contact.getNickName() == null
					|| contact.getNickName().isEmpty() || contact.getWork() == null || contact.getWork().isEmpty()
					|| contact.getEmail() == null || contact.getEmail().isEmpty() || contact.getPhone() == null
					|| contact.getPhone().isEmpty() || contact.getDescription() == null
					|| contact.getDescription().isEmpty()) {

				throw new IllegalArgumentException("Fill all fields properly...");
			}
			// Handle validation errors
			if (result.hasErrors()) {
				System.out.println("Validation errors: " + result.toString());
				model.addAttribute("contact", contact);
				return "user/add_contact_form"; // Stay on the form for validation errors
			}

			// Get logged-in user
			String username = principal.getName();
			User user = userRepository.findByEmail(username);

			// upload photo
			if (Photo.isEmpty()) {
				System.out.println("File is empty.....");
				contact.setImgUrl("contact.png");
				// return "normal/add_contact_form";
			} else {

				contact.setImgUrl(Photo.getOriginalFilename());
				File saveFile = new ClassPathResource("static/IMG").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + Photo.getOriginalFilename());
				Files.copy(Photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("File uploaded successfully.....");

				if (Files.exists(path)) {
					System.out.println("File saved successfully!");
					long fileSize = Files.size(path);
					System.out.println("File size: " + fileSize + " bytes");
				} else {
					System.out.println("File not saved!");
				}
			}
			// Link contact to user and save
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepository.save(user);
			System.out.println(contact);
			System.out.println("Your contact has been saved successfully"); // Flash success message
			model.addAttribute("message", new message("Your contact has been saved successfully!", "alert-success"));
			return "normal/add_contact_form";
		} catch (Exception e) {
			e.printStackTrace();

			// Stay on the same form and show error message
			model.addAttribute("message",
					new message("Something went wrong! " + e.getMessage() + " Please try again.", "alert-danger"));
			return "normal/add_contact_form";
		}
	}

	// delete Contact

	@GetMapping("/deleteContact/{id}")
	public String deleteEmployeeBysId(@PathVariable(value = "id") Integer id, Model model, Principal principal) {
		Optional<Contact> contactOptional = this.contactRepo.findById(id);
		Contact contact = contactOptional.get();

//		contact.setUser(null);
//		this.contactRepo.delete(contact);

		User user = this.userRepository.findByEmail(principal.getName());
		// remove image of that contact too

		if (contact.getUser().getId() == user.getId()) {
			user.getContacts().remove(contact);
			this.userRepository.save(user);
			model.addAttribute("message", new message("Conatct delete successfully..", "alert-success"));
		} else {
			model.addAttribute("title", "Wrong request");
			model.addAttribute("message", new message("Conatct Not Found....", "alert-danger"));
		}

		// this.contactService.deleteById(id);
		model.addAttribute("message", new message("Contact deleted successfully..", "alert-success"));
		return showContacts(1, model, principal);
	}

	// update contact Form

	@PostMapping("/update_form/{id}")
	public String UpdateForm(@PathVariable(value = "id") Integer id, Model model, Principal principal) {
		model.addAttribute("title", "Update Contact");

		Contact contact = this.contactRepo.findById(id).get();

		model.addAttribute("contact", contact);
		// User user=this.userRepository.findByEmail(principal.getName());
		// if(contact.getUser().getId()==user.getId()) {
		// }
		return "normal/update_form";

	}

	// Update Contact Processing

	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("Photo") MultipartFile Photo,
			Principal principal, Model model, RedirectAttributes redirectAttributes) {
		try {
			Contact oldcontact = this.contactRepo.findById(contact.getCId()).get();

			if (!Photo.isEmpty()) {
				// delete old photo
				File oldFile = new ClassPathResource("static/IMG").getFile();
				File file = new File(oldFile, oldcontact.getImgUrl());
				file.delete();

				// update the photo
				contact.setImgUrl(Photo.getOriginalFilename());
				File saveFile = new ClassPathResource("static/IMG").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + Photo.getOriginalFilename());
				Files.copy(Photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImgUrl(Photo.getOriginalFilename());
			} else {
				contact.setImgUrl(oldcontact.getImgUrl());
			}

			User user = userRepository.findByEmail(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", new message("Something went wrong.....!!", "alert-denger"));

		}

		System.out.println(contact.getName());
		System.out.println(contact.getNickName());
		redirectAttributes.addFlashAttribute("message", new message("Contact Updated Successfully", "alert-success"));

		return "redirect:/user/" + contact.getCId() + "/contact_Detail";
	}

	// Contact List

	@GetMapping("/show_Contacts")
	public String show_contacts(Model model, Principal principal) {
		return showContacts(1, model, principal);
	}

	// show contacts

	@GetMapping("/show_Contacts/{PageNo}")
	public String showContacts(@PathVariable("PageNo") int PageNo, Model model, Principal principal) {
		model.addAttribute("title", "My Contacts List");
		// here is important to provide a contacts of loged in user to the screen
		String username = principal.getName();// with the help of principal we take the username which is entered at the
												// time of login
		// System.out.println(username);
		User user = userRepository.findByEmail(username);
		// System.out.println(user);
		// Contacts contacts=(Contacts) user.getContacts();
		int pageSize = 5;
		Pageable pagable = PageRequest.of(PageNo - 1, pageSize);
		Page<Contact> contactList = contactService.getContactsByUser(user.getId(), pagable);
		System.out.println(contactList);
		model.addAttribute("currPage", PageNo);
		model.addAttribute("totalPages", contactList.getTotalPages());
		model.addAttribute("totalElements", contactList.getTotalElements());
		model.addAttribute("contactList", contactList);
		return "normal/show_Contacts";
	}

//showing perticular contact details

	@GetMapping("/{cId}/contact_Detail")
	public String contactDetail(@PathVariable(value = "cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOptional = contactRepo.findById(cId);
		Contact contact = contactOptional.get();

		String username = principal.getName();
		User user = userRepository.findByEmail(username);
		int id = user.getId();
		if (id == contact.getUser().getId()) {
			model.addAttribute("title", contact.getName());
			model.addAttribute(contact);
		} else {
			model.addAttribute("title", "Not found");
		}
		return "normal/contact_Detail";
	}

	// User profile

	@GetMapping("/myProfile")
	public String viewProfile(Model model) {
		model.addAttribute("title", "My Profile");
		return "normal/Profile";
	}

	// open setting page

	@GetMapping("/open-setting")
	public String openSetting(Model model) {
		model.addAttribute("title", "Settings ");
		return "normal/settings";
	}

	// Changing User Password
	@PostMapping("/change_Password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Model model, RedirectAttributes redirectAttributes,
			Principal principal) {
		String username = principal.getName();
		User curr_user = this.userRepository.findByEmail(username);
		// now it is encrepted
		// oldPassword=curr_user.getPassword();
		if (this.bCryptPasswordEncoder.matches(oldPassword, curr_user.getPassword())) {
			curr_user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(curr_user);
			redirectAttributes.addFlashAttribute("message",
					new message("Password Updated successfully..", "alert-success"));
			System.out.println("Password Updated Successfully..");
		} else {
			redirectAttributes.addFlashAttribute("message",
					new message("Enter Correct Old Password..!!", "alert-danger"));
			return "redirect:/user/open-setting";
		}

//		System.out.println("Old Password :" + oldPassword);
//		System.out.println("New Password :" + newPassword);

		return "redirect:/user/index";
	}

//	Payment Integration
//	
//	@PostMapping("/createOrder")
//	@ResponseBody  //it is used when you want return simple String only not a html page
//	public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
//		System.out.println(data);
//		System.out.println("Order has been created.........");
//		
//		
//		//data is Hashmap 
//		int Amt=Integer.parseInt(data.get("amount").toString());
//		
//		var client= new RazorpayClient("rzp_test_3uqyjTXiuGQxxP", "NHjFulsQyPw6alAQ2hnczPSF");
//		
//		JSONObject Jsonobject=new JSONObject();
//		Jsonobject.put("amount", Amt*100);
//		Jsonobject.put("currency", "INR");
//		Jsonobject.put("receipt", "txn_235425");
//		
//		Order order = client.orders.create(Jsonobject);
//		System.out.println(order);
//		
//		return order.toString();
//	}

	
	@GetMapping("/orders")
	public String orderpage() {
		return "normal/orders";
	}

	@PostMapping(value = "/createOrder", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Orders> createorder(@RequestBody Orders orders) throws RazorpayException {
		Orders order = orderService.createOrders(orders);
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}
	@PostMapping("/paymentCallback")
	public String paymentCallback(@RequestParam Map<String, String> response) {
		 	orderService.updateStatus(response);
		 	return "success";
		
	}
}
