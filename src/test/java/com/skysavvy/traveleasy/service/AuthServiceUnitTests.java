package com.skysavvy.traveleasy.service;

import com.skysavvy.traveleasy.TraveleasyApplication;
import com.skysavvy.traveleasy.configuration.security.jwt.JwtUtils;
import com.skysavvy.traveleasy.database.repository.UserRepository;
import com.skysavvy.traveleasy.database.service.AuthService;
import com.skysavvy.traveleasy.model.user.CostumUserDetails;
import com.skysavvy.traveleasy.model.user.Role;
import com.skysavvy.traveleasy.model.user.User;
import com.skysavvy.traveleasy.payload.request.LoginRequest;
import com.skysavvy.traveleasy.payload.request.SignUpRequest;
import com.skysavvy.traveleasy.payload.response.AuthResponse;
import com.skysavvy.traveleasy.payload.response.MessageResponse;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveleasyApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceUnitTests {

    @Mock // crea un'istanza simulata
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks // inietta le simulazioni nella classe
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test 1: la registrazione ha successo")
    public void testSignUpSuccess(){
        SignUpRequest signUpRequest = new SignUpRequest("newUser", "new", "user", "newuser@gmail.com", "StrongPassword123!", "StrongPassword123!", Role.USER);

        //deve ritornare false in entrambe le condizioni affinchè la registrazione abbia successo
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);

        //la password deve essere codificata prima di registrare l'utente nell db
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        //registro la risposta ottenuta del metodo signUp del servizio
        ResponseEntity<?> responseEntity = authService.signUp(signUpRequest);

        //verifico l'uguaglianza dei codici, se ha avuto successo deve restituirmi 200
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals("User saved successfully", ((MessageResponse) responseEntity.getBody()).getMessage());

        //verifico che l'utente sia stato salvato
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    @DisplayName("Test 2: verifico l'esistenza della email")
    public void testSignUpEmailExists(){
        SignUpRequest signUpRequest = new SignUpRequest("newUser1", "new", "user", "existingemail@gmail.com", "StrongPassword123!", "StrongPassword123!", Role.USER);

        //deve ritornare true perchè l'email esiste già, ma false per l'username
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        //registro la risposta ottenuta del metodo signUp del servizio
        ResponseEntity<?> responseEntity = authService.signUp(signUpRequest);

        //verifico l'uguaglianza dei codici, se ha avuto successo deve restituirmi 200
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals("Error: Email already exists",  ((MessageResponse) responseEntity.getBody()).getMessage());

        //verifico esistenza dell'utente
        verify(userRepository, times(1)).existsByEmail(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("Test 3: verifico l'esistenza dell'username")
    public void testSignUpUsernameExists(){
        SignUpRequest signUpRequest = new SignUpRequest("existUsername", "new", "user", "newuser@gmail.com", "StrongPassword123!", "StrongPassword123!", Role.USER);

        //deve ritornare true perchè l'username esiste già
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        //registro la risposta ottenuta del metodo signUp del servizio
        ResponseEntity<?> responseEntity = authService.signUp(signUpRequest);

        //verifico l'uguaglianza dei codici, se ha avuto successo deve restituirmi 200
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals("Error: Username already exists",  ((MessageResponse) responseEntity.getBody()).getMessage());

        //verifico esistenza dell'utente
        verify(userRepository, times(1)).existsByUsername(signUpRequest.getUsername());
    }

    @Test
    @DisplayName("Test 4: l'autenticazione ha avuto successo")
    public void testLoginSuccess(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        Authentication auth = mock(Authentication.class);
        CostumUserDetails costumUserDetails = mock(CostumUserDetails.class);

        //creo un user su cui testare l'autenticazione
        User user = new User("user", "user", "username", "user@gmail.com", "password", Role.USER);
        user.setId(8L);

        //setto i vari ritorni per i metodi
        when(auth.getPrincipal()).thenReturn(costumUserDetails);
        when(costumUserDetails.getUsername()).thenReturn("username");
        when(jwtUtils.generateToken(auth)).thenReturn("jwtTokenUser");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = authService.login(loginRequest, auth);
        AuthResponse authResponse = (AuthResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("jwtTokenUser", authResponse.getToken());
        assertEquals(user.getId(), authResponse.getId());
        assertEquals(user.getUsername(), authResponse.getUsername());

        verify(jwtUtils, times(1)).generateToken(auth);
        verify(userRepository, times(2)).findByUsername("username");
    }

    @Test
    public void testLoginFail(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrongUsername");
        loginRequest.setPassword("wrongPassword");

        Authentication auth = mock(Authentication.class);
        CostumUserDetails costumUserDetails = mock(CostumUserDetails.class);

        when(auth.getPrincipal()).thenReturn(costumUserDetails);
        when(costumUserDetails.getUsername()).thenReturn("wrongUsername");
        when(userRepository.findByUsername("wrongUsername")).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = authService.login(loginRequest, auth);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Username or password is incorrect", responseEntity.getBody());

        verify(userRepository, times(1)).findByUsername("wrongUsername");
    }
}
