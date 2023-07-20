package net.javaguides.springboot.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	
	private UserRepository userRepository;
    private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;

	}
	

	@Override
	public User saveUser(UserDto userDto) {
		 User user = new User();
	        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
	        user.setEmail(userDto.getEmail());
	        // encrypt the password using spring security
	        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

	        Role role = roleRepository.findByName("ROLE_ADMIN");
	        if(role == null){
	            role = checkRoleExist();
	        }
	        user.setRoles(Arrays.asList(role));
	      return  userRepository.save(user);
	}
	
	private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

	@Override
	public User findUserByEmail(String email) {
		 return userRepository.findByEmail(email); 
	}

	@Override
	public List<UserDto> findAllUsers() {
		 List<User> users = userRepository.findAll();
	        return users.stream()
	        .map((user) -> mapToUserDto(user))
	         .collect(Collectors.toList());	
	        }
	
	private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" "); // win talka espace t'hot li kablou fi case 
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
