package com.example.wallet.controller;

import com.example.wallet.model.User;
import com.example.wallet.service.AuthorizationService;
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
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final WalletService walletService;

    @GetMapping("/")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login-form";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, BindingResult bindingResult, Model model) {
        if(authorizationService.loginUser(user)) {
            model.addAttribute("login", user.getName());
            model.addAttribute("passwordItems", walletService.getPasswords());
            return "main";
        }
        return "redirect:/";
    }

    @GetMapping("/register/form")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult bindingResult, Model model) {
        if(!authorizationService.registerUser(user)) {
            return "redirect:/bad-register";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        authorizationService.logout();
        return "redirect:/";
    }

    @GetMapping("/change-password/form")
    public String changePasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "change-password-form";
    }

    @PostMapping("/change-password")
    public String addPassword(@ModelAttribute User user, BindingResult bindingResult, Model model) {
        authorizationService.changePassword(user);
        return "redirect:/main";
    }

    @GetMapping("/password-confirm/form")
    public String passwordConfirmForm(Model model) {
        model.addAttribute("user", new User());
        return "password-confirm-form";
    }

    @PostMapping("/password-confirm")
    public String passwordConfirm(@ModelAttribute User user, BindingResult bindingResult, Model model) {
        if(authorizationService.confirmPassword(user.getPassword())) {
            return "redirect:/main";
        }
        return "password-confirm-form";
    }

    @GetMapping("/bad-register")
    public String badRegister(Model model) {
        return "bad-register";
    }
}
