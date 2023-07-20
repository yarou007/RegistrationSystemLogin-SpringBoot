package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.model.User;

public interface UserService {
	    User saveUser(UserDto userDto);

	    User findUserByEmail(String email);

	    List<UserDto> findAllUsers();
}
