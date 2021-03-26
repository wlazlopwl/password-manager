package com.example.wallet.service;

import com.example.wallet.model.PasswordItem;
import com.example.wallet.model.User;
import com.example.wallet.repository.PasswordItemRepository;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.util.PasswordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final PasswordItemRepository passwordItemRepository;
    private final HashService hashService;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    public void savePasswordInfo(PasswordItem passwordItem) {
        String encodedPassword = hashService.AESEncrypt(passwordItem.getPassword());
        Optional<User> user = userRepository.findById(authorizationService.getLoggedUserName());
        passwordItem.setPasswordType(PasswordType.AES);
        passwordItem.setPassword(encodedPassword);

        if(user.isPresent()) {
            passwordItem.setUser(user.get());
        }

        passwordItemRepository.save(passwordItem);
    }

    public List<PasswordItem> getPasswords() {
       List<PasswordItem> passwordItems = passwordItemRepository.findByUserName(authorizationService.getLoggedUserName());

       if(authorizationService.isPasswordConfirmed()){
           passwordItems
                   .stream()
                   .forEach(passwordItem -> {
                       passwordItem.setPassword(
                               hashService.AESDecrypt(passwordItem.getPassword())
                       );
                   });
       }
       return passwordItems;
    }
}
