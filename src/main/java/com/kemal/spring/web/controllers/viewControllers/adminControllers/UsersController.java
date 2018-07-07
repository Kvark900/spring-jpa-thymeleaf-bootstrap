package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.service.UserUpdateDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import com.kemal.spring.web.paging.InitialPagingSizes;
import com.kemal.spring.web.paging.Pager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public String showUsers(Model model, @RequestParam("pageSize") Optional<Integer> pageSize,
                            @RequestParam("page") Optional<Integer> page) {

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(InitialPagingSizes.getInitialPageSize());
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? InitialPagingSizes.getInitialPage() : page.get() - 1;

        Page<UserDto> users = userDtoService.findAllPageable(new PageRequest(evalPage, evalPageSize));
        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), InitialPagingSizes.getButtonsToShow());
        model.addAttribute("users", users);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", InitialPagingSizes.getPageSizes());
        model.addAttribute("pager", pager);
        return "adminPage/user/users";
    }

    @GetMapping("/users/{id}")
    public String getEditUserForm(@PathVariable Long id, Model model) {
        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);
        List<Role> allRoles = roleService.findAll();

        userUpdateDto.setRoles(userService.getAssignedRolesList(userUpdateDto));

        model.addAttribute("userUpdateDto", userUpdateDto);
        model.addAttribute("allRoles", allRoles);
        return "adminPage/user/editUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model, @PathVariable Long id,
                             @ModelAttribute("oldUser") @Valid final UserUpdateDto userUpdateDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Optional<User> persistedUser = userService.findById(id);
        String formWithErrors = "adminPage/user/editUser";

        List<User> allUsers = userService.findAll();
        List <Role> allRoles = roleService.findAll();

        User emailAlreadyExists = userService.findByEmailAndIdNot(userUpdateDto.getEmail(), id);
        User usernameAlreadyExists = userService.findByUsernameAndIdNot(userUpdateDto.getUsername(), id);
        boolean hasErrors = false;

        if (emailAlreadyExists != null) {
            bindingResult.rejectValue("email", "emailAlreadyExists", "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists != null) {
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
            userService.save(userService.getUpdatedUser(persistedUser.get(), userUpdateDto));
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

        User emailAlreadyExists = userService.findByEmail(newUser.getEmail());
        User usernameAlreadyExists = userService.findByUsername(newUser.getUsername());
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
