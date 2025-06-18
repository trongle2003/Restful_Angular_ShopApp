package com.project.shopapp.controller;

import com.project.shopapp.domain.dtos.UserDTO;
import com.project.shopapp.domain.dtos.UserLoginDTO;
import com.project.shopapp.domain.entity.User;
import com.project.shopapp.service.UserService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ApiMessage("Create User")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getFieldError().getDefaultMessage().toString());
        }
        if (!userDTO.getPassWord().equals(userDTO.getRetypePassWord())) {
            throw new InvalidException("Password does not match");
        }
        User newUser = this.userService.handleCreateUser(userDTO);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    @ApiMessage("Login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) throws InvalidException {
        if (bindingResult.hasErrors()) {
            throw new InvalidException(bindingResult.getGlobalError().getDefaultMessage());
        }
        String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassWord());
        return ResponseEntity.ok("user.login.login_successfully" + token);
    }
}
