package com.example.wallet.repository;

import com.example.wallet.model.PasswordItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordItemRepository extends JpaRepository<PasswordItem, String> {

    List<PasswordItem> findByUserName(String name);
}
