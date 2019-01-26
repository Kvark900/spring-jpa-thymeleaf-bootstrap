package com.kemal.spring.web.controllers.restApiControllers;

import com.kemal.spring.domain.User;
import com.kemal.spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by Keno&Kemo on 16.12.2017..
 */

@RestController
public class restController {
    private UserService userService;

    public restController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/adminPage/json-users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> allUsers = userService.findAll();

        if (allUsers == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else if (allUsers.isEmpty()) return new ResponseEntity<>(allUsers, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/adminPage/json-users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> userToDelete = userService.findById(id);

        if (!userToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.deleteById(id);
        return new ResponseEntity<>(userToDelete.get(), HttpStatus.NO_CONTENT);
    }
}