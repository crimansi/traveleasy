package com.skysavvy.traveleasy.payload.request;

import com.skysavvy.traveleasy.model.user.Role;
import com.skysavvy.traveleasy.validation.PasswordMatching;
import com.skysavvy.traveleasy.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword",
        message = "Password and Confirm Password must be matched!"
)
@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(max = 45)
    private String username;
    @NotBlank
    @Size(max = 45)
    private String firstName;
    @NotBlank
    @Size(max = 45)
    private String lastName;
    @NotBlank
    @Size(max = 45)
    @Email
    private String email;
    @StrongPassword
    private String password;
    private String confirmPassword;
    private Role role = Role.USER;

}
