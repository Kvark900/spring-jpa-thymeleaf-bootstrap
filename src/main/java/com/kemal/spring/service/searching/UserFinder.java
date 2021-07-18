package com.kemal.spring.service.searching;

import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.web.dto.UserDto;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.lang.Long.parseLong;

@Log4j2
@Data
@Service
public class UserFinder {
    private UserDtoService userDtoService;

    @Autowired
    public UserFinder(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    public UserSearchResult searchUsersByProperty(PageRequest pageRequest, UserSearchParameters params) {
        Page<UserDto> userDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        String s = params.getUsersProperty().get();

        if ("ID".equals(s)) {
            try {
                userDtoPage = userDtoService.findByIdPageable(parseLong(params.getPropertyValue().get()), pageRequest);
            } catch (NumberFormatException e) {
                log.error(e);
                return new UserSearchResult(userDtoService.findAllPageable(pageRequest), true);
            }
        }
        else if ("Name".equals(s))
            userDtoPage = userDtoService.findByNameContaining(params.getPropertyValue().get(), pageRequest);
        else if ("Surname".equals(s))
            userDtoPage = userDtoService.findBySurnameContaining(params.getPropertyValue().get(), pageRequest);
        else if ("Username".equals(s))
            userDtoPage = userDtoService.findByUsernameContaining(params.getPropertyValue().get(), pageRequest);
        else if ("Email".equals(s))
            userDtoPage = userDtoService.findByEmailContaining(params.getPropertyValue().get(), pageRequest);
        return new UserSearchResult(userDtoPage, false);
    }
}
