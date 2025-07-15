package com.example.UserManagementService.service;


import com.example.UserManagementService.dto.UserRequest;
import com.example.UserManagementService.dto.UserResponse;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.repo.UsersRepo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    public Boolean registerUser(UserRequest request);

    public List<UserResponse> getAllUsers();

    public UserResponse getUserById(Integer id) throws UserNotFoundException;

    public Boolean deleteUserById(Integer id) throws UserNotFoundException;

    public Boolean changeAccountStatus(Integer id) throws UserNotFoundException;


    public UserResponse updateUser(Integer id, UserRequest request) throws  UserNotFoundException;
}
