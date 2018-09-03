package com.kemal.spring.service.searching;

import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.web.paging.InitialPagingSizes;
import com.kemal.spring.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserSearchErrorResponse {

    private UserDtoService userDtoService;

    public UserSearchErrorResponse(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    public ModelAndView respondToNumberFormatException(UserSearchResult userSearchResult, ModelAndView modelAndView) {
        Pager pager = new Pager(userSearchResult.getUserDtoPage().getTotalPages(),
                userSearchResult.getUserDtoPage().getNumber(), InitialPagingSizes.getButtonsToShow(),
                userSearchResult.getUserDtoPage().getTotalElements());

        modelAndView.addObject("numberFormatException", true);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserDtoPage());
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    public ModelAndView respondToEmptySearchResult(ModelAndView modelAndView, PageRequest pageRequest) {
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("users", userDtoService.findAllPageable(pageRequest));
        return modelAndView;
    }
}
