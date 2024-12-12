package com.smart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.Helper.Message;
import com.smart.dao.UserRepository;
import com.smart.entities.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {
@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manger");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manger");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manger");
		model.addAttribute("user", new User());
		return "signup";
	}
			//handler for do_register
			@RequestMapping(  value="/do_register",method=RequestMethod.POST)
			public String registerUser( @Valid @ModelAttribute("user") User user, BindingResult result1,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,HttpSession session){
				try{
					if (!agreement) {
						System.out.println("You have not agreed the terms and conditions");
						throw new Exception("You have not agreed the terms and conditions");
					}
						
			if(result1.hasErrors())
			{
				System.out.println("ERROR "+ result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
					user.setRole("ROLE_USER");
					user.setEnable(true);
					user.setImageUrl("default.png");
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					System.out.println("Agreement" +agreement);
					User result = this.userRepository.save(user);
					model.addAttribute("user", new User());
					model.addAttribute("message",new Message("Sucessfully Registerd !!","alert-success"));
					System.out.println("User" +user);

				}catch(Exception e){
					e.printStackTrace();
			model.addAttribute("message",new Message("Something Went Wrong !!"+e.getMessage(),"alert-danger"));

				}
				return "signup";
			} 
			
    //handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login.html";
	}
}
