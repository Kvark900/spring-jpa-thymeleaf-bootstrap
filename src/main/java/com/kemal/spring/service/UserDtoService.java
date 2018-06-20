package com.kemal.spring.service;

import com.kemal.spring.domain.User;
import com.kemal.spring.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keno&Kemo on 04.12.2017..
 */
@Service
public class UserDtoService {

    private UserService userService;
    private ModelMapper modelMapper;

    public UserDtoService(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<UserDto> findAll(){
        List<User> userList = userService.findAll();
        List<UserDto> userDtosList = new ArrayList<>();

        for(User user : userList){
            userDtosList.add(modelMapper.map(user, UserDto.class));
        }

        return userDtosList;
    }

    public UserDto findById(Long id){
        UserDto userDto = new UserDto();
        for(UserDto userDto1 : findAll()){
            if(userDto1.getId() == id){
                userDto = userDto1;
            }
        }
        return userDto;
    }

    public UserDto findByEmail(String email){
        for(UserDto userDto : findAll()){
            if(userDto.getEmail().equals(email)){
                return userDto;
            }
        }
        return null;
    }

    public UserDto findByUsername(String username){
        for(UserDto userDto : findAll()){
            if(userDto.getUsername().equals(username)){
                return userDto;
            }
        }
        return null;
    }




}
