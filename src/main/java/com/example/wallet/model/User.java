package com.example.wallet.model;

import com.example.wallet.util.PasswordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String name;

    private String password;

    private String salt;

    @Enumerated(EnumType.STRING)
    private PasswordType passwordType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordItem> passwordItems;
}
