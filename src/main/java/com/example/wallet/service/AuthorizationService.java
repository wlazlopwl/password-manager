package com.example.wallet.service;

import com.example.wallet.model.User;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.util.PasswordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;
    private final HashService hashService;
    private String loggedUser;
    private boolean passwordConfirmed = false;

    public boolean registerUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getName());

        if (existingUser.isPresent()) {
            return false;
        }

        String passwordEncoded;
        if (user.getPasswordType().equals(PasswordType.SHA512)) {
            String salt =  new String(hashService.calculateMD5Hash(user.getName()));
            passwordEncoded = hashService.SHA512Encrypt(user.getPassword(), salt);
            user.setSalt(salt);
        } else {
            passwordEncoded = hashService.HMACEncrypt(user.getPassword());
        }
        user.setPassword(passwordEncoded);

        userRepository.save(user);
        return true;
    }

    public boolean loginUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getName());

        if(existingUser.isEmpty()){
            return false;
        }

        String passwordEncoded;
        if (existingUser.get().getPasswordType().equals(PasswordType.SHA512)) {
            String salt =  new String(hashService.calculateMD5Hash(user.getName()));
            passwordEncoded = hashService.SHA512Encrypt(user.getPassword(), salt);
        } else {
            passwordEncoded = hashService.HMACEncrypt(user.getPassword());
        }

        if(passwordEncoded.equals(existingUser.get().getPassword())) {
            loggedUser = existingUser.get().getName();
            return true;
        }

        return false;
    }

    public boolean confirmPassword(String password) {
        Optional<User> existingUser = userRepository.findById(loggedUser);

        if(existingUser.isEmpty()) {
            return false;
        }

        String passwordEncoded;
        if (existingUser.get().getPasswordType().equals(PasswordType.SHA512)) {
            String salt =  new String(hashService.calculateMD5Hash(loggedUser));
            passwordEncoded = hashService.SHA512Encrypt(password, salt);
        } else {
            passwordEncoded = hashService.HMACEncrypt(password);
        }

        if(passwordEncoded.equals(existingUser.get().getPassword())) {
            loggedUser = existingUser.get().getName();
            passwordConfirmed = true;
            return true;
        }

        return false;
    }

    public String getLoggedUserName() {
        return loggedUser;
    }

    public void logout() {
        passwordConfirmed = false;
        loggedUser = null;
    }

    public void changePassword(User user) {
        Optional<User> existingUser = userRepository.findById(loggedUser);

        if(existingUser.isPresent()) {
            String passwordEncoded;
            if (user.getPasswordType().equals(PasswordType.SHA512)) {
                String salt =  new String(hashService.calculateMD5Hash(loggedUser));
                passwordEncoded = hashService.SHA512Encrypt(user.getPassword(), salt);
                existingUser.get().setSalt(salt);
                existingUser.get().setPasswordType(PasswordType.SHA512);
            } else {
                passwordEncoded = hashService.HMACEncrypt(user.getPassword());
                existingUser.get().setPasswordType(PasswordType.HMAC);
                existingUser.get().setSalt(null);
            }
            existingUser.get().setPassword(passwordEncoded);
            userRepository.save(existingUser.get());
        }
    }

    public boolean isPasswordConfirmed() {
        return passwordConfirmed;
    }
}
