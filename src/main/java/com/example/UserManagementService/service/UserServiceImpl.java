package com.example.UserManagementService.service;


import com.example.UserManagementService.config.AppProperties;
import com.example.UserManagementService.constants.AppConstants;
import com.example.UserManagementService.dto.ActivateAccountRequest;
import com.example.UserManagementService.dto.LoginRequest;
import com.example.UserManagementService.dto.UserRequest;
import com.example.UserManagementService.dto.UserResponse;
import com.example.UserManagementService.entity.UserMaster;
import com.example.UserManagementService.exception.EmailExistsException;
import com.example.UserManagementService.exception.InvalidPasswordException;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.exception.ValidEmailException;
import com.example.UserManagementService.repo.UsersRepo;
import com.example.UserManagementService.utils.EmailUtils;
import jakarta.persistence.criteria.Predicate;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.security.SecureRandom;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService {
    private  UsersRepo usersRepo;
    private EmailUtils emailUtils;
    private Map<String, String> messages;

    public UserServiceImpl(UsersRepo usersRepo,EmailUtils emailUtils,AppProperties appProperties) {
        this.usersRepo = usersRepo;
        this.emailUtils=emailUtils;
        this.messages= appProperties.getMessages();
    }

    public Boolean registerUser(UserRequest request) throws EmailExistsException {
        UserMaster master = new UserMaster();
        if(usersRepo.existsByEmailId(request.getEmailId())){
            throw  new EmailExistsException("Email Already Exists");
        }
        BeanUtils.copyProperties(request,master);
        master.setPassword(generatePassword());
        master.setAccStatus(messages.get(AppConstants.USER_STATUS_INACTIVE));
        String fileName = "RegisterBody.txt";
        String subject = "User Registeration Success Mail";
        String emailBody = readEmailBody(request.getUserName(),master.getPassword(),fileName);
        emailUtils.sendMail(request.getEmailId(),subject,emailBody);

        return usersRepo.save(master)!=null;


    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserMaster> list = usersRepo.findAll();
        List<UserResponse> userResponses =
        list.stream().map(user->{
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user,userResponse);
            return  userResponse;
        }).toList();



        return userResponses;
    }

    @Override
    public UserResponse getUserById(Integer id) throws UserNotFoundException {
        UserMaster userMaster = usersRepo.findById(id).orElseThrow(()->new UserNotFoundException("Please Enter A Valid User Id"));
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(userMaster,response);
        return  response;
    }

    @Override
    public Boolean deleteUserById(Integer id) throws UserNotFoundException {
        UserMaster userMaster = usersRepo.findById(id).orElseThrow(()->new UserNotFoundException("Please Enter A Valid User Id"));
      usersRepo.delete(userMaster);

      return userMaster!=null;



    }

    @Override
    public Boolean changeAccountStatus(Integer id) throws UserNotFoundException {
        UserMaster userMaster = usersRepo.findById(id).orElseThrow(()->new UserNotFoundException("Please Enter A Valid User Id"));
        if(userMaster.getAccStatus().equals(messages.get(AppConstants.USER_STATUS_INACTIVE))){
            userMaster.setAccStatus(messages.get(AppConstants.USER_STATUS_ACTIVE));
        }
        else if (userMaster.getAccStatus().equals("Active")){
            userMaster.setAccStatus("In-Active");
        }
        usersRepo.save(userMaster);
        return userMaster!=null;

    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest request) throws UserNotFoundException {
        UserMaster userMaster = usersRepo.findById(id).orElseThrow(()->new UserNotFoundException("Please Enter A Valid User Id"));
       if(request.getUserName()!=null){
           userMaster.setUserName(request.getUserName());
       }
        if(request.getEmailId()!=null){
            userMaster.setEmailId(request.getEmailId());

        }
        if(request.getGender()!=null){
            userMaster.setGender(request.getGender());
        }
        if(request.getSsn()!=null){
            userMaster.setSsn(request.getSsn());
        }
        if(request.getMobileNumber()!=null){
            userMaster.setMobileNumber(request.getMobileNumber());
        }
        if(request.getDateOfBirth()!=null){
            userMaster.setDateOfBirth(request.getDateOfBirth());
        }
        usersRepo.save(userMaster);
        UserResponse  u =new  UserResponse();
        BeanUtils.copyProperties(userMaster,u);
        return u;
    }

    @Override
    public Boolean activateAccount(ActivateAccountRequest request) throws ValidEmailException, InvalidPasswordException {
        UserMaster userMaster =usersRepo.findByEmailId(request.getEmailId());
        if(userMaster==null){
            throw  new ValidEmailException("Please Enter Valid Email Address");
        }
        if(!request.getTempPass().equals(userMaster.getPassword())){
            throw new InvalidPasswordException("Please Enter A Valid Temporory Password");

        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new InvalidPasswordException("Password And Confirm Password Does Not Match");
        }

        userMaster.setPassword(request.getPassword());
        userMaster.setAccStatus(messages.get(AppConstants.USER_STATUS_ACTIVE));
        usersRepo.save(userMaster);
        return true;


    }

    @Override
    public String accountLogin(LoginRequest loginRequest) throws ValidEmailException {

         UserMaster master = usersRepo.findByEmailId(loginRequest.getEmailId());
         if(master==null || !loginRequest.getPassword().equals(master.getPassword()) ){
             throw  new ValidEmailException("Please Enter Valid Credentials");
         }
        if (!"Active".equalsIgnoreCase(master.getAccStatus())) {
            throw new ValidEmailException("Account is not active. Please verify your email.");
        }

        return "Login Successful";




    }

    @Override
    public Boolean forgetPassword(String email) throws ValidEmailException {

       if(usersRepo.existsByEmailId(email)){
           UserMaster master = usersRepo.findByEmailId(email);
           String fileName = "ForgetPass.txt";
           String subject = "Password Retrieval Mail";
           String emailBody = readEmailBody(master.getUserName(),master.getPassword(),fileName);
           emailUtils.sendMail(master.getEmailId(),subject,emailBody);
       }
       else{

               throw  new ValidEmailException("Please Enter Valid Emial Address");

       }
       return  true;
    }


    private String generatePassword(){
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        int length = 6;

        for(int i=0;i<length;i++){
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);

        }
        return sb.toString();

    }

    public String readEmailBody(String fullName , String pass, String fileName){
        String url ="http://localhost:7002/api/user/activateAccount";
        String mailBody = null;
        try{
        ClassPathResource resource = new ClassPathResource(fileName);
        Path filePath = Path.of(resource.getURI());
        try(FileReader fr = new FileReader(filePath.toFile()); BufferedReader br = new BufferedReader(fr)) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line!=null){
                sb.append(line);
                line=br.readLine();
            }
            mailBody= sb.toString();
            mailBody = mailBody.replace("{FULL-NAME}",fullName);
           mailBody=  mailBody.replace("{TEMP-PWD}",pass);
          mailBody =  mailBody.replace("{URL}",url);


        }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mailBody;
    }

}
