package com.example.wallet.model;

import com.example.wallet.util.PasswordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String service;

    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private PasswordType passwordType;

    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;
}
