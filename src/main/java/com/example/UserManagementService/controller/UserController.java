package com.example.UserManagementService.controller;


import com.example.UserManagementService.config.AppProperties;
import com.example.UserManagementService.constants.AppConstants;
import com.example.UserManagementService.dto.ActivateAccountRequest;
import com.example.UserManagementService.dto.LoginRequest;
import com.example.UserManagementService.dto.UserRequest;
import com.example.UserManagementService.dto.UserResponse;
import com.example.UserManagementService.exception.EmailExistsException;
import com.example.UserManagementService.exception.InvalidPasswordException;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.exception.ValidEmailException;
import com.example.UserManagementService.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    private Map<String, String> messages;

    public UserController(UserService userService,AppProperties appProperties) {
        this.userService = userService;
        this.messages=appProperties.getMessages();
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request) throws EmailExistsException {
        Boolean isSaved = userService.registerUser(request);
        String responseMsg;
        if(isSaved){
            responseMsg = messages.get(AppConstants.USER_SAVE_SUCC);
            return new ResponseEntity<>(responseMsg, HttpStatus.CREATED);
        }
      else{
            responseMsg = messages.get(AppConstants.USER_SAVE_FAIL);

            return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> li = userService.getAllUsers();
        return new ResponseEntity<>(li,HttpStatus.OK);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) throws UserNotFoundException {
        UserResponse response = userService.
                getUserById(id);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) throws UserNotFoundException {
        Boolean isDeleted = userService.deleteUserById(id);
        String responseMsg;
        if(isDeleted){
            responseMsg = messages.get(AppConstants.USER_DELETE_SUCC);

        }
        else{
            responseMsg = messages.get(AppConstants.USER_DELETE_FAIL);

        }
        return  new ResponseEntity<>(responseMsg,HttpStatus.OK);
    }
    @PutMapping("/statusChange/{id}")
    public ResponseEntity<String> StatusChangeById(@PathVariable Integer id) throws UserNotFoundException {
        Boolean isDeleted = userService.changeAccountStatus(id);
        String responseMsg;
        if(isDeleted){
            responseMsg = messages.get(AppConstants.USER_UPDATESTATUS_SUCC);

        }
        else{
            responseMsg = messages.get(AppConstants.USER_UPDATESTATUS_FAIL);

        }
        return  new ResponseEntity<>(responseMsg,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id,@RequestBody UserRequest request) throws UserNotFoundException {
        UserResponse userResponse = userService.updateUser(id,request);

        return  new ResponseEntity<>(userResponse,HttpStatus.OK);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<String> activateAccount(@RequestBody ActivateAccountRequest request) throws InvalidPasswordException, ValidEmailException {
        Boolean isActivated = userService.activateAccount(request);
        String responseMsg= null ;
        if(isActivated){
            responseMsg="User account Activated";


        }
        return new ResponseEntity<>(responseMsg,HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAccount(@RequestBody LoginRequest request) throws ValidEmailException {
        String  msg = userService.accountLogin(request);
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }


    @PostMapping("/forgetPassword/{email}")
    public ResponseEntity<String> passRetrieve(@PathVariable String email) throws ValidEmailException {
        Boolean isSent = userService.forgetPassword(email);
        String responseMsg = null;
        if(isSent){
            responseMsg="Password Sent Successfully";
        }
        return new ResponseEntity<>(responseMsg,HttpStatus.OK);
    }

}
