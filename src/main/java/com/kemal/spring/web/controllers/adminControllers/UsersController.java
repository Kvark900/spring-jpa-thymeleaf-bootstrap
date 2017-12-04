package com.kemal.spring.web.controllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.service.UserUpdateDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */


@Controller
@RequestMapping("/adminPage")
public class UsersController {
    private UserService userService;
    private RoleService roleService;
    private ModelMapper modelMapper;
    private UserUpdateDtoService userUpdateDtoService;
    private UserDtoService userDtoService;

    public UsersController(UserService userService, RoleService roleService, ModelMapper modelMapper, UserUpdateDtoService userUpdateDtoService, UserDtoService userDtoService) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.userUpdateDtoService = userUpdateDtoService;
        this.userDtoService = userDtoService;
    }

    @GetMapping("/users")
    public ModelAndView showUsers(){
        ModelAndView modelAndView = new ModelAndView("adminPage/users");

        modelAndView.addObject("users", userUpdateDtoService.findAll());
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView getEditUserForm(@PathVariable Long id){

        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);

        ModelAndView modelAndView = new ModelAndView("adminPage/editUser");

        Map<Long, Role> assignedRoleMap = new HashMap<>();
        List<Role> roles = userUpdateDto.getRoles();
        for (Role role : roles) {
            assignedRoleMap.put(role.getId(), role);
        }

        List<Role> userRoles = new ArrayList<>();
        List<Role> allRoles = roleService.getAllRoles();
        for (Role role : allRoles) {
            if(assignedRoleMap.containsKey(role.getId())){
                userRoles.add(role);
            } else {
                userRoles.add(null);
            }
        }

        userUpdateDto.setRoles(userRoles);

        modelAndView.addObject("userUpdateDto", userUpdateDto);
        modelAndView.addObject("rolesList", allRoles);
        return modelAndView;
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model, @PathVariable Long id,
                             @ModelAttribute("oldUser")@Valid final UserUpdateDto userUpdateDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes){

        User persistedUser = userService.findById(id);
        String redirectToPageWithErrors = "adminPage/editUser";

        boolean emailAlreadyExists = false;
        boolean usernameAlreadyExists = false;
        boolean hasErrors = false;

        List <User> allUsers = userService.findAll();

        Map<Long, Role> assignedRoleMap = new HashMap<>();
        List<Role> roles = userUpdateDto.getRoles();
        for (Role role : roles) {
            assignedRoleMap.put(role.getId(), role);
        }

        List<Role> userRoles = new ArrayList<>();
        List<Role> allRoles = roleService.getAllRoles();
        for (Role role : allRoles) {
            if(assignedRoleMap.containsKey(role.getId())){
                userRoles.add(role);
            } else {
                userRoles.add(null);
            }
        }

        for(User user : allUsers){
            //Check if email is edited and if it is taken
            if(!userUpdateDto.getEmail().equals(persistedUser.getEmail())
                    && userUpdateDto.getEmail().equals(user.getEmail())){
                emailAlreadyExists = true;
            }

            //Check if username is edited and if it is taken
            if(!userUpdateDto.getUsername().equals(persistedUser.getUsername())
                    && userUpdateDto.getUsername().equals(user.getUsername())){
                usernameAlreadyExists = true;
            }
        }

        if(emailAlreadyExists){
            bindingResult.rejectValue("email", "emailAlreadyExists",
                    "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists){
            bindingResult.rejectValue("username", "usernameAlreadyExists",
                    "Oops!  There is already a user registered with the username provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) {
            hasErrors = true;
        }

        if(hasErrors){
            model.addAttribute("userUpdateDto", userUpdateDto);
            model.addAttribute("rolesList", allRoles);
            model.addAttribute("org.springframework.validation.BindingResult.userUpdateDto", bindingResult);
            return redirectToPageWithErrors;
        }

        else {
            persistedUser.setName(userUpdateDto.getName());
            persistedUser.setSurname(userUpdateDto.getSurname());
            persistedUser.setUsername(userUpdateDto.getUsername());
            persistedUser.setEmail(userUpdateDto.getEmail());

            userService.saveUser(persistedUser);

            redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
            return "redirect:/adminPage/users";

        }
    }

    @GetMapping("/users/newUser")
    public ModelAndView getAddNewUserForm(){
        UserDto newUser = new UserDto();
        ModelAndView modelAndView = new ModelAndView("adminPage/newUser");
        modelAndView.addObject("newUser", newUser);
        return modelAndView;
    }

    @PostMapping("/users/newUser")
    public ModelAndView saveUser(ModelAndView modelAndView, @ModelAttribute("newUser")
                                @Valid final UserDto newUser, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        UserDto mailExists = userDtoService.findByEmail(newUser.getEmail());
        UserDto usernameExists = userDtoService.findByUsername(newUser.getUsername());

        if (mailExists != null) {
            modelAndView.setViewName("adminPage/newUser");
            bindingResult.rejectValue("email", "mailExists",
                    "Oops!  There is already a user registered with the email provided.");
        }

        if (usernameExists != null){
            modelAndView.setViewName("adminPage/newUser");
            bindingResult.rejectValue("username", "usernameExists",
                    "Oops!  There is already a user registered with the username provided.");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("adminPage/newUser");
        }

        else {
            User user = userService.createNewAccount(newUser);
            user.setEnabled(true);

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("userHasBeenSaved", true);
            modelAndView.setViewName("redirect:/adminPage/users");
        }

        return modelAndView;
    }
}
