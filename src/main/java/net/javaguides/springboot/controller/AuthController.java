package net.javaguides.springboot.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.UserService;


@Controller
public class AuthController {

	private UserService userService;

	public AuthController(UserService userService) {
		super();
		this.userService = userService;
	}
    // handler method to handle home page request
	@GetMapping("/")
	public String home() {
		return "index";
	}
	 // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }
	
    // handler method to handle user registration form request
	 @GetMapping("/register")
	 public String showRegistrationForm(Model model){
	        // create model object to store form data
	        UserDto user = new UserDto();
	        model.addAttribute("user", user);
	        return "register";
	    }

	    // handler method to handle user registration form submit request
	@PostMapping("/register/save")
	public String RegisterUserAccount(@Validated @ModelAttribute("user") UserDto userDto, BindingResult result,
			Model model) {
		
		// validated selon constraint li mahtoutin fi data base lezmou yabda ( priamry key w constraints ken fama )
		// @ModelAttribute("user") UserDto userDto behs torber el model attribute lil userDto w transferihom 
		// BindingResult result ==> objet fih data binding validation process quelque soit success wala failed ( errors ) 
		
		User existingUser = userService.findUserByEmail(userDto.getEmail());

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null, "There is already an account registered with the same email");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "/register";
		}

		userService.saveUser(userDto);
		return "redirect:/register?success";
	}
	// handler method to handle list of users
    @GetMapping("/users")
    public String users(Principal principal,Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
	    model.addAttribute("identifier", principal);
        return "users";
    }
}
