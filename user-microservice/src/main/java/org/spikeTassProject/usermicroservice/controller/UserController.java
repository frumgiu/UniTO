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

    @GetMapping("/getUser/{email}")
    public User getUser(@PathVariable("email") String email) {
        System.out.println("Get specific user " + email);
        User result = userRepository.findUserByEmail(email);
        System.out.println("User trovato: " + result.toString());
        return result;
    }

    /* Nella barra di ricerca metto solo una stringa di una parola
    * Non so distinguere se sia un nome o un cognome quindi cerco
    * per entrambe le cose  */
    @GetMapping("/getUsers/{text}")
    public List<User> getUsers(@PathVariable("text") String name) {
        System.out.println("Get users with name or surname: " + name);
        return userRepository.findUserByNameOrSurname(name, name);
    }

    /* Nella barra di ricerca metto due parole separate da spazio quindi
    * le tratto come due strnighe separate, una per il nome e una per il cognome
    * Secondi nomi/cognomi vanno separati dal primo con -, es. Giulia-Milea */
    @GetMapping("/getUsers/{name}/{surname}")
    public List<User> getUsersNameComplete(@PathVariable("name") String name, @PathVariable("surname") String surname) {
        System.out.println("Get users with name and surname: " + name + " " + surname);
        return userRepository.findUserByNameAndSurname(name, surname);
    }

    /* TODO: Non salva la modifica in db */
    @GetMapping(value = "/changeBio/{id}/{newBio}")
    public User changeBioUser(@PathVariable("id") Long id, @PathVariable("newBio") String newBio) {
        User userFromDB = null;
        if (userRepository.findById(id).isPresent()) {
            userFromDB = userRepository.findById(id).get();
            userFromDB.setBio(newBio);
        }
        return userFromDB;
    }

    @PostMapping(value = "/createUser")
    public User postUsers(@RequestBody User param) {
        System.out.println("Create a new User... " + param.toString());
        return userRepository.save(
                new User(param.getEmail(),param.getName(), param.getSurname(), param.getRole()));
    }
}