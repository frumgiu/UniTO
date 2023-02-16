package org.uniti.usermicroservice.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.uniti.usermicroservice.model.User;
import org.uniti.usermicroservice.repository.UserRepository;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    private static final String HD_UNITO_EDU = "edu.unito.it";


    @Autowired
    private UserRepository userRepository;



    @PostMapping("/")
    public User googleAuthentication(@RequestBody User authenticationUser) {
        System.out.println("Google Authentication");
        if(!validateGoogleUnitoUserProfile(authenticationUser))
            return null;
        String email = authenticationUser.getEmail();
        if (!userRepository.findUserByEmail(email).isPresent()) {
            System.out.println("User is not registered...");
            return createUserFromGoogleAuth(authenticationUser);
        }
        System.out.println("User already exists");
        return userRepository.findUserByEmail(email).get();

    }


    private boolean validateGoogleUnitoUserProfile(User authenticationUser) {
        if (authenticationUser == null || authenticationUser.getEmail().equals(""))
            return false;
        try {
            InternetAddress internetAddress = new InternetAddress(authenticationUser.getEmail());
            internetAddress.validate();
            return StringUtils.substringAfter(internetAddress.getAddress(),"@").equals(HD_UNITO_EDU);
        } catch (AddressException e) {
            return false;
        }
    }



    private User createUserFromGoogleAuth(User authenticationUser) {
        System.out.println("Creating user...");
        User user = new User(
                authenticationUser.getEmail(),
                authenticationUser.getName(),
                authenticationUser.getSurname(),
                authenticationUser.getRole()
        );
        if (authenticationUser.getUrlPicture() != null)
            user.setUrlPicture(authenticationUser.getUrlPicture());
        return userRepository.save(user);
    }

}
