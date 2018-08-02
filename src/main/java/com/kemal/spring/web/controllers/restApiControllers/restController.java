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

        else return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    //region Unused
    //================================================================================================================
    /*@GetMapping("/adminPage/json-users/search")
    public ResponseEntity<List<User>> getUsersByProperty(@RequestParam(value = "usersProperty") String usersProperty,
                                                         @RequestParam(value = "propertyValue") String propertyValue){
        List<User> users = new ArrayList();

        switch (usersProperty) {
            case "ID":
                try {
                    Optional<User> user = userService.findById(Long.parseLong(propertyValue));
                    users.add(user.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Name":
                users = userService.findByNameContaining(propertyValue);
                break;
            case "Surname":
                users = userService.findBySurnameContaining(propertyValue);
                break;
            case "Username":
                users = userService.findByUsernameContaining(propertyValue);
                break;
            case "Email":
                users = userService.findByEmailContaining(propertyValue);
                break;
        }

        if (users == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else if (users.isEmpty()) return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);

        else return new ResponseEntity<>(users, HttpStatus.OK);
    }*/
    //================================================================================================================
    //endregion


    @DeleteMapping("/adminPage/json-users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> userToDelete = userService.findById(id);

        if (!userToDelete.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else {
            userService.deleteById(id);
            return new ResponseEntity<>(userToDelete.get(), HttpStatus.NO_CONTENT);
        }
    }
}