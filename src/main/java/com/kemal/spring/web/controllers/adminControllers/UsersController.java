package com.kemal.spring.web.controllers.adminControllers;

import com.kemal.spring.domain.User;
import com.kemal.spring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */
@Controller
@RequestMapping("/adminPage")
public class UsersController {
    UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ModelAndView showUsers(){
        ModelAndView modelAndView = new ModelAndView("adminPage/users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView editUser (@PathVariable Long id){
        User oldUser = userService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/editUser");
        modelAndView.addObject("oldUser", oldUser);
        return modelAndView;

    }

}
