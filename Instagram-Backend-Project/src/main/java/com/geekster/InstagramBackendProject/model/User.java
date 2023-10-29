package com.geekster.InstagramBackendProject.model;

import com.geekster.InstagramBackendProject.model.enums.AccountType;
import com.geekster.InstagramBackendProject.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String userName;
    private String userHandle;
    private String userBio;
    @Pattern(regexp = "^.+@(?![Ii][Nn][Ss][Tt][Aa][Aa][Dd][Mm][Ii][Nn]\\[Cc][Oo][Mm]$).+$")
    @Column(unique = true) //should have unique values in the columns!!
    private String userEmail;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*])(?!.*\\s).{8,16}$\n")
    private String userPassword; //regex for strong password
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private boolean blueTick;
}
