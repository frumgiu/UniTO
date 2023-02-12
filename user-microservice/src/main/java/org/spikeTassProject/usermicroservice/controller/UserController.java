package org.spikeTassProject.usermicroservice.controller;

import org.spikeTassProject.usermicroservice.model.User;
import org.spikeTassProject.usermicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

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

    /* TODO Solo di test per comunicazione con client */
    @PostMapping("/getAllTest")
    public void test(@RequestParam("param") String param) {
        System.out.println("Get all Users");
        System.out.println(param);
    }
    @GetMapping("/getUser/{email}")
    public User getUser(@PathVariable("email") String email) {
        System.out.println("Get specific user " + email);
        User result = userRepository.findUserByEmail(email);
        System.out.println("User trovato: " + result);
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

    /* TODO: Testare funzionamento con postman */
    @PostMapping(value = "/updateUser")
    public User updateUserInfo(@RequestBody User param) {
        User userFromDB;
        if (userRepository.findById(param.getId()).isPresent()) {
            userFromDB = userRepository.findById(param.getId()).get();
            userFromDB.setName(param.getName());
            userFromDB.setSurname(param.getSurname());
            userFromDB.setBio(param.getBio());
            userFromDB.setUrlPicture(param.getUrlPicture());
            return userRepository.save(userFromDB);
        }
        return null;
    }

    @PostMapping(value = "/createUser")
    public User postUsers(@RequestBody User param) {
        System.out.println("Create a new User... " + param.toString());
        return userRepository.save(
                new User(param.getEmail(),param.getName(), param.getSurname(), param.getRole()));
    }

    @PostMapping(value = "/populateDBUsers")
    public boolean populateDBUsers() {
        System.out.println("Metodo di servizio/sviluppo per popolare il DB con alcuni utenti");
        User u1 = new User("fabio.ferrero111@edu.unito.it", "Fabio", "Ferrero", "urs");
        User u2 = new User("giulia.frumento@edu.unito.it", "Giulia", "Frumento", "urs");
        User u3 = new User("samuele.monasterolo@edu.unito.it", "Samuele", "Monasterolo", "urs");
        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        return true;
    }
}