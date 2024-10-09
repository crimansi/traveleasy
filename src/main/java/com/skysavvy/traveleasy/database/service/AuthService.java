package com.skysavvy.traveleasy.database.service;

import com.skysavvy.traveleasy.model.user.CostumUserDetails;
import com.skysavvy.traveleasy.model.user.User;
import com.skysavvy.traveleasy.payload.request.LoginRequest;
import com.skysavvy.traveleasy.payload.request.SignUpRequest;
import com.skysavvy.traveleasy.payload.response.AuthResponse;
import com.skysavvy.traveleasy.payload.response.MessageResponse;
import com.skysavvy.traveleasy.database.repository.UserRepository;
import com.skysavvy.traveleasy.configuration.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;


    public ResponseEntity<MessageResponse> signUp(SignUpRequest user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already exists"));
        }if(userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already exists"));
        }
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(user.getRole());
        newUser.setEnabled(true);

        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User saved successfully"));
    }

    public ResponseEntity login(LoginRequest loginRequest, Authentication auth) {
        String token = jwtUtils.generateToken(auth);
        CostumUserDetails userDetails = (CostumUserDetails) auth.getPrincipal();
        if(userRepository.findByUsername(userDetails.getUsername()).isPresent()) {
            User user = userRepository.findByUsername(userDetails.getUsername()).get();
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName()));
        }
        return ResponseEntity.badRequest().body( new MessageResponse("Username or password is incorrect"));
    }

    /*public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmToken token = confirmTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null) {
            if(userRepository.findByEmail(token.getUser().getEmail()).isPresent()) {
                User user = userRepository.findByEmail(token.getUser().getEmail()).get();
                user.setEnabled(true);
                userRepository.save(user);
                return ResponseEntity.ok("Email verified successfully!");
            }
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }*/


}
