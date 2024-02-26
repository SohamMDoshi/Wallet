package com.swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swiggy.wallet.customValidation.ValidCountryValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Country is required")
    @ValidCountryValue
    private Country country;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Wallet> wallets = new HashSet<>();


    public Users(String username, String password,Country country) {
        this.username = username;
        this.password = password;
        this.country = country;
    }

}

