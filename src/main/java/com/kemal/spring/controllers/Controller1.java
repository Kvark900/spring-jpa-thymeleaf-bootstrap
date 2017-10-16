package com.kemal.spring.controllers;

import com.kemal.spring.entity.User;
import com.kemal.spring.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Keno&Kemo on 30.09.2017..
 */

@Controller
public class Controller1 {
    private UserRepository userRepository;

    public Controller1(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"/", "/index.html"})
    public String index (){
        return "index";
    }

    @GetMapping(value = "/login.html")
    public String login (){
        return "login";
    }

    @GetMapping(value = "/register")
    public String showRegistrationForm(WebRequest request, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/registered.html")
    public ModelAndView saveUser(@Valid @ModelAttribute ("user") User user, BindingResult bindingResult, WebRequest request, Errors errors){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject(user);
        ModelAndView modelAndView1 = new ModelAndView("registered");
        modelAndView1.addObject(user);
        if (!bindingResult.hasErrors()) {
            userRepository.save(user);
            return modelAndView1;
        }
        else {
            return modelAndView;
        }
    }
}
