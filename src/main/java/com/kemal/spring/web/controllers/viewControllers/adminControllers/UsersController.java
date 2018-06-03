package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.service.UserUpdateDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */


@Controller
@RequestMapping("/adminPage")
public class UsersController {
    private UserService userService;
    private RoleService roleService;
    private UserUpdateDtoService userUpdateDtoService;
    private UserDtoService userDtoService;

    public UsersController(UserService userService, RoleService roleService,
                           UserUpdateDtoService userUpdateDtoService,
                           UserDtoService userDtoService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userUpdateDtoService = userUpdateDtoService;
        this.userDtoService = userDtoService;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userUpdateDtoService.findAll());
        return "adminPage/user/users";
    }

    @GetMapping("/users/{id}")
    public String getEditUserForm(@PathVariable Long id, Model model) {
        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);
        List<Role> allRoles = roleService.findAll();
        userUpdateDto.setRoles(userService.getAssignedRolesList(userUpdateDto));

        model.addAttribute("userUpdateDto", userUpdateDto);
        model.addAttribute("rolesList", allRoles);
        return "adminPage/user/editUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model, @PathVariable Long id,
                             @ModelAttribute("oldUser") @Valid final UserUpdateDto userUpdateDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        User persistedUser = userService.findById(id);
        String formWithErrors = "adminPage/user/editUser";

        List<User> allUsers = userService.findAll();
        List<Role> allRoles = roleService.findAll();

        boolean emailAlreadyExists = userService.checkIfEmailIsTaken(allUsers, userUpdateDto, persistedUser);
        boolean usernameAlreadyExists = userService.checkIfUsernameIsTaken(allUsers, userUpdateDto, persistedUser);
        boolean hasErrors = false;


        if (emailAlreadyExists) {
            bindingResult.rejectValue("email", "emailAlreadyExists", "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists) {
            bindingResult.rejectValue("username", "usernameAlreadyExists", "Oops!  There is already a user registered with the username provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("userUpdateDto", userUpdateDto);
            model.addAttribute("rolesList", allRoles);
            model.addAttribute("org.springframework.validation.BindingResult.userUpdateDto", bindingResult);
            return formWithErrors;
        }
        else {
            userService.save(userService.updateUser(persistedUser, userUpdateDto));
            redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
            return "redirect:/adminPage/users";
        }
    }

    @GetMapping("/users/newUser")
    public String getAddNewUserForm(Model model) {
        UserDto newUser = new UserDto();
        model.addAttribute("newUser", newUser);
        return "adminPage/user/newUser";
    }

    @PostMapping("/users/newUser")
    public String saveNewUser(Model model, @ModelAttribute("newUser") @Valid final UserDto newUser,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        UserDto emailAlreadyExists = userDtoService.findByEmail(newUser.getEmail());
        UserDto usernameAlreadyExists = userDtoService.findByUsername(newUser.getUsername());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/user/newUser";

        if (emailAlreadyExists != null) {
            bindingResult.rejectValue("email", "emailAlreadyExists",
                    "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists != null) {
            bindingResult.rejectValue("username", "usernameAlreadyExists",
                    "Oops!  There is already a user registered with the username provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        else {
            User user = userService.createNewAccount(newUser);
            user.setEnabled(true);

            userService.save(user);
            redirectAttributes.addFlashAttribute("userHasBeenSaved", true);
            return "redirect:/adminPage/users";
        }
    }

}
