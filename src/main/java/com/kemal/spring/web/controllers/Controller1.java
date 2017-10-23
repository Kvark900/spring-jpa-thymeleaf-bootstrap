package com.kemal.spring.web.controllers;

import com.kemal.spring.domain.User;
import com.kemal.spring.service.EmailService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.web.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Keno&Kemo on 30.09.2017..
 */

@Controller
public class Controller1 {

    private UserService userService;
    private EmailService emailService;

    public Controller1(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index (ModelAndView modelAndView){
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping(value = "/login")
    public String login (){
        return "login";
    }

    @GetMapping(value = "/register")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "register";
    }

    @PostMapping(value = "/register")
    public ModelAndView saveUser(ModelAndView modelAndView, @Valid @ModelAttribute ("userDto") UserDto userDto,
                                 BindingResult bindingResult, HttpServletRequest request, Errors errors){

        User userExists = userService.findByEmail(userDto.getEmail());

        System.out.println(userExists);

        if (userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else { // new user so we create user and send confirmation e-mail

            User user = userService.createNewAccount(userDto);
            // Disable user until they click on confirmation link in email

            user.setEnabled(false);


            userService.saveUser(user);

            /*String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("Please confirm the registration");
            registrationEmail.setFrom("email@email.com");

            emailService.sendEmail(registrationEmail);*/

            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + userDto.getEmail());
            modelAndView.setViewName("registered");
        }

        return modelAndView;
    }



}
