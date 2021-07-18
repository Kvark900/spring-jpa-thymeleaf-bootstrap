package com.kemal.spring.service;

import com.kemal.spring.domain.User;
import com.kemal.spring.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by Keno&Kemo on 04.12.2017..
 */
@Service
public class UserDtoService {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserDtoService(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<UserDto> findAll(){
        List<User> users = userService.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
    }

    public Page<UserDto> findAllPageable(Pageable pageable) {
        Page<User> users = userService.findAllPageable(pageable);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    public Optional<UserDto> findById(Long id) {
        Optional<User> retrievedUser = userService.findById(id);
        return retrievedUser.map(user -> modelMapper.map(user, UserDto.class));
    }

    public UserDto findByEmail(String email){
        return modelMapper.map(userService.findByEmail(email), UserDto.class);
    }

    public Page<UserDto> findByIdPageable(Long id, PageRequest pageRequest) {
        Page<User> users = userService.findByIdPageable(id, pageRequest);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public UserDto findByUsername(String username){
        return modelMapper.map(userService.findByUsername(username), UserDto.class);
    }

    public Page<UserDto> findByNameContaining(String name, PageRequest pageRequest) {
        Page<User> users = userService.findByNameContaining(name, pageRequest);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findBySurnameContaining(String surname, PageRequest pageRequest) {
        Page<User> users = userService.findBySurnameContaining(surname, pageRequest);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findByUsernameContaining(String username, PageRequest pageRequest) {
        Page<User> users = userService.findByUsernameContaining(username, pageRequest);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }

    public Page<UserDto> findByEmailContaining(String email, PageRequest pageRequest) {
        Page<User> users = userService.findByEmailContaining(email, pageRequest);
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toList());
        return new PageImpl<>(userDtos, pageRequest, users.getTotalElements());
    }
}
