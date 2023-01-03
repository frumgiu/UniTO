package org.spikeTassProject.usermicroservice.repository;

import org.spikeTassProject.usermicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAll();
    List<User> findUsersByName(String name);
    List<User> findUsersBySurname(String surname);

    User findUsersByEmail(String email);
}