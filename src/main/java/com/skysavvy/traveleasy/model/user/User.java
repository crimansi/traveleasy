package com.skysavvy.traveleasy.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
@UniqueConstraint(columnNames = "email")})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(example = "1")
    private Long id;
    @Schema(name = "first name", example = "Pippo")
    @Column(name="first-name", nullable=false, length=45)
    private String firstName;
    @Schema(name = "last name", example = "Pluto")
    @Column(name="last-name", nullable=false, length=45)
    private String lastName;
    @Schema(example = "pippoDB")
    @Column(nullable=false, length=45)
    private String username;
    @Schema(example = "pippoDB@gmail.com")
    @Column(nullable=false, length=45)
    @Email
    private String email;
    @Schema(example = "StrongPassword123!")
    @Column(nullable=false, length=300)
    private String password;
    private boolean enabled = false;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public User(String firstName, String lastName, String username, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
