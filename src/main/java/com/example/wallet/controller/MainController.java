package com.example.wallet.controller;

import com.example.wallet.model.PasswordItem;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final WalletService walletService;

    @GetMapping("/main")
    public String mainView(Model model) {
        model.addAttribute("passwordItems", walletService.getPasswords());
        return "main";
    }

    @GetMapping("/add-password/form")
    public String addPasswordForm(Model model) {
        model.addAttribute("passwordItem", new PasswordItem());
        return "add-password-form";
    }

    @PostMapping("/add-password")
    public String addPassword(@ModelAttribute PasswordItem passwordItem, BindingResult bindingResult, Model model) {
        walletService.savePasswordInfo(passwordItem);
        return "redirect:/main";
    }

}
