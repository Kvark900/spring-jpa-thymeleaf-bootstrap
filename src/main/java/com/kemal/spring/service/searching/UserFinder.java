package com.kemal.spring.service.searching;

import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.web.dto.UserDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Data
@Service
public class UserFinder {
    private UserDtoService userDtoService;

    @Autowired
    public UserFinder(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    public UserSearchResult searchUsersByProperty(PageRequest pageRequest, UserSearchParameters userSearchParameters) {
        Page<UserDto> userDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        switch (userSearchParameters.getUsersProperty().get()) {
            case "ID":
                try {
                    long id = Long.parseLong(userSearchParameters.getPropertyValue().get());
                    userDtoPage = userDtoService.findByIdPageable(id, pageRequest);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return new UserSearchResult(userDtoService.findAllPageable(pageRequest), true);
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
        return new UserSearchResult(userDtoPage, false);
    }
}
