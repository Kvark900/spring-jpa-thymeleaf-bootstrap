package com.kemal.spring.service.searching;

import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.paging.InitialPagingSizes;
import com.kemal.spring.web.paging.Pager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Data
@Service
public class UserFinder {
    private UserSearchParameters userSearchParameters;
    private UserSearchResult userSearchResult;
    private UserDtoService userDtoService;

    @Autowired
    public UserFinder(UserSearchParameters userSearchParameters, UserDtoService userDtoService) {
        this.userSearchParameters = userSearchParameters;
        this.userDtoService = userDtoService;
    }

    public UserSearchResult searchUsersByProperty(PageRequest pageRequest) {
        Page<UserDto> userDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        switch (userSearchParameters.getUsersProperty().get()) {
            case "ID":
                try {
                    long id = Long.parseLong(userSearchParameters.getPropertyValue().get());
                    userDtoPage = userDtoService.findByIdPageable(id, pageRequest);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    setUserSearchResult(new UserSearchResult(userDtoService.findAllPageable(pageRequest), true));
                    return userSearchResult;
                }
                break;
            case "Name":
                userDtoPage = userDtoService.findByNameContaining(userSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Surname":
                userDtoPage = userDtoService.findBySurnameContaining(userSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Username":
                userDtoPage = userDtoService.findByUsernameContaining(userSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Email":
                userDtoPage = userDtoService.findByEmailContaining(userSearchParameters.getPropertyValue().get(), pageRequest);
                break;
        }
        setUserSearchResult(new UserSearchResult(userDtoPage, false));
        return userSearchResult;
    }

    public ModelAndView searchResultHasNumberFormatException (ModelAndView modelAndView) {
        Pager pager = new Pager(userSearchResult.getUserDtoPage().getTotalPages(), userSearchResult.getUserDtoPage()
                .getNumber(), InitialPagingSizes.getButtonsToShow(), userSearchResult.getUserDtoPage().getTotalElements());

        modelAndView.addObject("numberFormatException", "Please enter valid number");
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserDtoPage());
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    public ModelAndView searchResultIsEmpty (ModelAndView modelAndView, PageRequest pageRequest) {
        userSearchResult.setUserDtoPage(userDtoService.findAllPageable(pageRequest));
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("users", userSearchResult.getUserDtoPage());
        return modelAndView;
    }
}
