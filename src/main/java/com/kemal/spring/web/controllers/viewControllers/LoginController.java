package com.kemal.spring.web.controllers.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Keno&Kemo on 17.11.2017..
 */

@Controller
@RequestMapping("")
public class LoginController {

    // Login form with error
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "website/login";
    }
}
