package com.skysavvy.traveleasy.controller;

import com.skysavvy.traveleasy.model.user.User;
import com.skysavvy.traveleasy.payload.request.LoginRequest;
import com.skysavvy.traveleasy.payload.request.SignUpRequest;
import com.skysavvy.traveleasy.database.service.AuthService;
import com.skysavvy.traveleasy.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Authentication API")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "User is registered", description = "Return a message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Username already exists", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "Email already exists", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "402", description = "Must be 8 characters long, but no more than 20, and combination of uppercase letters, lowercase letters, numbers, special characters.", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403",  description = "Password and Confirm Password must be matched!", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Email not valid", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return authService.login(loginRequest, auth);
    }
    /*@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return authService.confirmEmail(confirmationToken);
    }*/
}
