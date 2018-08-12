package com.kemal.spring.service;

import com.kemal.spring.domain.User;
import com.kemal.spring.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<UserDto> findAllPageable(Pageable pageable) {
        Page<User> users = userService.findAllPageable(pageable);
        List <UserDto> userDtos = new ArrayList<>();

        for (User user: users) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    public UserDto findById(Long id){
        if (userService.findById(id).isPresent())
            return modelMapper.map(userService.findById(id).get(), UserDto.class);
        return null;
    }

    public UserDto findByEmail(String email){
        return modelMapper.map(userService.findByEmail(email), UserDto.class);
    }

    public UserDto findByUsername(String username){
        return modelMapper.map(userService.findByUsername(username), UserDto.class);
    }

    public Page<UserDto> findByNameContaining(String name, PageRequest pageRequest) {
        Page<User> users = userService.findByNameContaining(name, pageRequest);
        List <UserDto> userDtos = new ArrayList<>();

        for (User user: users) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findBySurnameContaining(String surname, PageRequest pageRequest) {
        Page<User> users = userService.findBySurnameContaining(surname, pageRequest);
        List <UserDto> userDtos = new ArrayList<>();

        for (User user: users) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findByUsernameContaining(String username, PageRequest pageRequest) {
        Page<User> users = userService.findByUsernameContaining(username, pageRequest);
        List <UserDto> userDtos = new ArrayList<>();

        for (User user: users) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findByEmailContaining(String email, PageRequest pageRequest) {
        Page<User> users = userService.findByEmailContaining(email, pageRequest);
        List <UserDto> userDtos = new ArrayList<>();

        for (User user: users) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }
}
