package com.kemal.spring.service.searching;

import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.paging.InitialPagingSizes;
import com.kemal.spring.web.paging.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserFinder {
    private UserDtoService userDtoService;

    @Autowired
    public UserFinder(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }


    public UserSearchResult searchUsersByProperty(String usersProperty, String propertyValue,
                                                  Page<UserDto> userDtoPage, PageRequest pageRequest) {
        switch (usersProperty) {
            case "ID":
                try {
                    List<UserDto> users = new ArrayList();
                    users.add(userDtoService.findById(Long.parseLong(propertyValue)));
                    users.removeIf(Objects::isNull);
                    userDtoPage = new PageImpl<>(users, pageRequest, users.size());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    userDtoPage = userDtoService.findAllPageable(pageRequest);
                    return new UserSearchResult(userDtoPage, true);
                }
                break;
            case "Name":
                userDtoPage = userDtoService.findByNameContaining(propertyValue, pageRequest);
                break;
            case "Surname":
                userDtoPage = userDtoService.findBySurnameContaining(propertyValue, pageRequest);
                break;
            case "Username":
                userDtoPage = userDtoService.findByUsernameContaining(propertyValue, pageRequest);
                break;
            case "Email":
                userDtoPage = userDtoService.findByEmailContaining(propertyValue, pageRequest);
                break;
        }

        return new UserSearchResult(userDtoPage, false);
    }

    public ModelAndView searchResultHasNumberFormatException (ModelAndView modelAndView, UserSearchResult userSearchResult) {
        Pager pager = new Pager(userSearchResult.getUserDtoPage().getTotalPages(), userSearchResult.getUserDtoPage()
                .getNumber(), InitialPagingSizes.getButtonsToShow(), userSearchResult.getUserDtoPage().getTotalElements());

        modelAndView.addObject("numberFormatException", "Please enter valid number");
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserDtoPage());
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    public ModelAndView searchResultIsEmpty (ModelAndView modelAndView, UserSearchResult userSearchResult, PageRequest pageRequest) {
        userSearchResult.setUserDtoPage(userDtoService.findAllPageable(pageRequest));
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("users", userSearchResult.getUserDtoPage());
        return modelAndView;
    }
}
