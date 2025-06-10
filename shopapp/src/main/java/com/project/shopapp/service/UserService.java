package com.project.shopapp.service;

import com.project.shopapp.component.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public User handleCreateUser(UserDTO userDTO) throws InvalidException {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidException("Phone number already exists");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new InvalidException("Role Not Found"));

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(String.valueOf(userDTO.getPhoneNumber()))
                .passWord(userDTO.getPassWord())
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

    public String login(String phoneNumber, String password) throws InvalidException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new InvalidException("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new InvalidException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password,existingUser.getAuthorities());
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtUtil.generateToken(existingUser);
    }
}
