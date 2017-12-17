package com.kemal.spring.web.controllers.restApiControllers;

import com.kemal.spring.domain.User;
import com.kemal.spring.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Keno&Kemo on 16.12.2017..
 */
@RestController
public class restController {
    private UserService userService;

    public restController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/adminPage/json-users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping ("/adminPage/json-users/delete/{id}")
    public void deleteUser (@PathVariable Long id){
        User deletedUser = userService.findById(id);
        //removing all roles before deleting a user
        deletedUser.getRoles().clear();
        userService.deleteUser(id);
    }
}
