package com.skysavvy.traveleasy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skysavvy.traveleasy.ObjectToJsonUtil;
import com.skysavvy.traveleasy.TraveleasyApplication;
import com.skysavvy.traveleasy.database.service.AuthService;
import com.skysavvy.traveleasy.model.user.Role;
import com.skysavvy.traveleasy.model.user.User;
import com.skysavvy.traveleasy.payload.request.LoginRequest;
import com.skysavvy.traveleasy.payload.request.SignUpRequest;
import com.skysavvy.traveleasy.payload.response.AuthResponse;
import com.skysavvy.traveleasy.payload.response.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveleasyApplication.class)
public class AuthControllerUnitTests {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;

    private SignUpRequest signUpRequest;

    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        signUpRequest = new SignUpRequest("username", "firstname", "lastname", "email@example.com", "StrongPassword123!", "StrongPassword123!", Role.USER);
        loginRequest = new LoginRequest();
        loginRequest.setPassword("StrongPassword123!");
        loginRequest.setUsername("username");
    }

    @Test
    public void testSignUpSuccess() throws Exception {
        when(authService.signUp(any(SignUpRequest.class))).thenReturn(ResponseEntity.ok(new MessageResponse("User saved successfully")));
        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonUtil.convertObjectToJsonBytes(signUpRequest)))
                .andExpect(status().isOk());

    }
    @Test
    public void testSignUpFail1() throws Exception {
        given(authService.signUp(any(SignUpRequest.class))).willReturn(ResponseEntity.badRequest().body(new MessageResponse("Error: Username already exists")));
        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonUtil.convertObjectToJsonBytes(signUpRequest)))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testSignUpFail2() throws Exception {
        given(authService.signUp(any(SignUpRequest.class))).willReturn(ResponseEntity.badRequest().body(new MessageResponse("Error: Email already exists")));

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonUtil.convertObjectToJsonBytes(signUpRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testLoginSuccess() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(auth);

        AuthResponse authResponse = new AuthResponse("jwtTokenUser", 8L, "username", "user@example.com", "pippo", "pluto");
        ResponseEntity<AuthResponse> responseEntity = ResponseEntity.ok(authResponse);
        when(authService.login(any(LoginRequest.class), any(Authentication.class))).thenReturn(responseEntity);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonUtil.convertObjectToJsonBytes(loginRequest)))
                .andExpect(status().isOk());
    }
    @Test
    public void testLoginFail() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(auth);

        AuthResponse authResponse = new AuthResponse("jwtTokenUser", 8L, "username", "user@example.com", "pippo", "pluto");
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.badRequest().body( new MessageResponse("Username or password is incorrect"));
        when(authService.login(any(LoginRequest.class), any(Authentication.class))).thenReturn(responseEntity);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonUtil.convertObjectToJsonBytes(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
