package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.service.UserUpdateDtoService;
import com.kemal.spring.service.searching.UserFinder;
import com.kemal.spring.service.searching.UserSearchErrorResponse;
import com.kemal.spring.service.searching.UserSearchParameters;
import com.kemal.spring.service.searching.UserSearchResult;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import com.kemal.spring.web.paging.InitialPagingSizes;
import com.kemal.spring.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.kemal.spring.web.paging.InitialPagingSizes.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */
@Controller
@RequestMapping("/adminPage")
public class UsersController {
    public static final String EDIT_USER_FORM = "adminPage/user/editUser";
    public static final String REDIRECT_ADMIN_PAGE_USERS = "redirect:/adminPage/users";

    private final UserService userService;
    private final RoleService roleService;
    private final UserUpdateDtoService userUpdateDtoService;
    private final UserDtoService userDtoService;
    private final UserFinder userFinder;
    private final UserSearchErrorResponse userSearchErrorResponse;

    public UsersController(UserService userService,
                           RoleService roleService,
                           UserUpdateDtoService userUpdateDtoService,
                           UserDtoService userDtoService,
                           UserFinder userFinder,
                           UserSearchErrorResponse userSearchErrorResponse) {
        this.userService = userService;
        this.roleService = roleService;
        this.userUpdateDtoService = userUpdateDtoService;
        this.userDtoService = userDtoService;
        this.userFinder = userFinder;
        this.userSearchErrorResponse = userSearchErrorResponse;
    }

    /**
     * Get all users or search users if searching parameters exist
     */
    @GetMapping("/users")
    public ModelAndView getUsers (ModelAndView modelAndView, UserSearchParameters searchParams) {
        int selectedPageSize = searchParams.getPageSize().orElse(INITIAL_PAGE_SIZE);
        int selectedPage = (searchParams.getPage().orElse(0) < 1) ? INITIAL_PAGE : (searchParams.getPage().get() - 1);

        PageRequest pageRequest = of(selectedPage, selectedPageSize, Sort.by(ASC, "id"));
        UserSearchResult userSearchResult = new UserSearchResult();

        if (searchParams.getPropertyValue().isEmpty() || searchParams.getPropertyValue().get().isEmpty())
            userSearchResult.setUserPage(userDtoService.findAllPageable(pageRequest));

        else {
            userSearchResult = userFinder.searchUsersByProperty(pageRequest, searchParams);

            if (userSearchResult.isNumberFormatException())
                return userSearchErrorResponse.respondToNumberFormatException(userSearchResult, modelAndView);

            if (userSearchResult.getUserPage().getTotalElements() == 0) {
                modelAndView = userSearchErrorResponse.respondToEmptySearchResult(modelAndView, pageRequest);
                userSearchResult.setUserPage(userDtoService.findAllPageable(pageRequest));
            }
            modelAndView.addObject("usersProperty", searchParams.getUsersProperty().get());
            modelAndView.addObject("propertyValue", searchParams.getPropertyValue().get());
        }

        Pager pager = new Pager(userSearchResult.getUserPage().getTotalPages(),
                                userSearchResult.getUserPage().getNumber(),
                                BUTTONS_TO_SHOW,
                                userSearchResult.getUserPage().getTotalElements());
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserPage());
        modelAndView.addObject("selectedPageSize", selectedPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public String getEditUserForm(@PathVariable Long id, Model model) {
        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);
        List<Role> allRoles = roleService.findAll();

        userUpdateDto.setRoles(userService.getAssignedRolesList(userUpdateDto));

        model.addAttribute("userUpdateDto", userUpdateDto);
        model.addAttribute("allRoles", allRoles);
        return EDIT_USER_FORM;
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model,
                             @PathVariable Long id,
                             @ModelAttribute("oldUser") @Valid UserUpdateDto userUpdateDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        Optional<User> persistedUser = userService.findById(id);
        List<Role> allRoles = roleService.findAll();

        boolean emailAlreadyExists = userService.findByEmailAndIdNot(userUpdateDto.getEmail(), id) != null;
        boolean usernameAlreadyExists = userService.findByUsernameAndIdNot(userUpdateDto.getUsername(), id) != null;
        boolean validationFailed = emailAlreadyExists || usernameAlreadyExists || bindingResult.hasErrors();

        if (emailAlreadyExists) bindingResult.rejectValue("email", "emailAlreadyExists");
        if (usernameAlreadyExists) bindingResult.rejectValue("username", "usernameAlreadyExists");
        if (validationFailed) {
            model.addAttribute("userUpdateDto", userUpdateDto);
            model.addAttribute("rolesList", allRoles);
            model.addAttribute("org.springframework.validation.BindingResult.userUpdateDto", bindingResult);
            return EDIT_USER_FORM;
        }
        userService.save(userService.getUpdatedUser(persistedUser.get(), userUpdateDto));
        redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
        return REDIRECT_ADMIN_PAGE_USERS;
    }

    @GetMapping("/users/newUser")
    public String getAddNewUserForm(Model model) {
        model.addAttribute("newUser", new UserDto());
        return "adminPage/user/newUser";
    }

    @PostMapping("/users/newUser")
    public String saveNewUser(@ModelAttribute("newUser") @Valid UserDto newUser,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        boolean emailAlreadyExists = userService.findByEmail(newUser.getEmail()) != null;
        boolean usernameAlreadyExists = userService.findByUsername(newUser.getUsername()) != null;
        boolean validationFailed = emailAlreadyExists || usernameAlreadyExists || bindingResult.hasErrors();
        String formWithErrors = "adminPage/user/newUser";

        if (emailAlreadyExists) bindingResult.rejectValue("email", "emailAlreadyExists");
        if (usernameAlreadyExists) bindingResult.rejectValue("username", "usernameAlreadyExists");
        if (validationFailed) return formWithErrors;

        User user = userService.createNewAccount(newUser);
        user.setEnabled(true);

        userService.save(user);
        redirectAttributes.addFlashAttribute("userHasBeenSaved", true);
        return REDIRECT_ADMIN_PAGE_USERS;
    }
}
