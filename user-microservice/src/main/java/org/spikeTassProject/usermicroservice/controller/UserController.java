package org.spikeTassProject.usermicroservice.controller;

import org.spikeTassProject.usermicroservice.model.User;
import org.spikeTassProject.usermicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        System.out.println("Get all Users");
        return userRepository.findAll();
    }
    @PostMapping(value = "/createUser")
    public User postUsers(@RequestBody User param) {
        System.out.println("Create a new User... " + param.toString());
        return userRepository.save(
                new User(param.getEmail(),param.getName(), param.getSurname(), param.getRole()));
    }
}