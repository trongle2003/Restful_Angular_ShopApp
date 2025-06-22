package com.project.shopapp.service;

import com.project.shopapp.component.JwtTokenUtil;
import com.project.shopapp.domain.dtos.UserDTO;
import com.project.shopapp.domain.entity.Role;
import com.project.shopapp.domain.entity.User;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.ultil.error.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public User handleCreateUser(UserDTO userDTO) throws InvalidException {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidException("Phone number already exists");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new InvalidException("Role Not Found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new InvalidException("You cannot register an admin account");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(String.valueOf(userDTO.getPhoneNumber()))
                .passWord(userDTO.getPassWord())
                .retypePassword(userDTO.getRetypePassWord())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .createAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        newUser.setRole(role);
        //check user login used Oauth co account_id ? yeu cau pass
        if (userDTO.getFacebookAccountId() == 0 || userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassWord();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassWord(encodedPassword);
        }
        return this.userRepository.save(newUser);
    }

    public Optional<User> handleCheckUser(long id) {
        return this.userRepository.findById(id);
    }

    public String login(String phoneNumber, String passWord, Long roleId) throws InvalidException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new InvalidException("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(passWord, existingUser.getPassword())) {
                throw new InvalidException("Wrong phone number or password");
            }
        }
        Optional<Role> optionalRole =
                roleRepository.findById(roleId);
        if (optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new InvalidException("Role does not exist");
        }

        UsernamePasswordAuthenticationToken
                authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, passWord, existingUser.getAuthorities());
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
