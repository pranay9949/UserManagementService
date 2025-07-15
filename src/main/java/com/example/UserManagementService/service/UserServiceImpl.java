package com.example.UserManagementService.service;


import com.example.UserManagementService.dto.UserRequest;
import com.example.UserManagementService.dto.UserResponse;
import com.example.UserManagementService.entity.UserMaster;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.repo.UsersRepo;
import com.example.UserManagementService.utils.EmailUtils;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.SecureRandom;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService {
    private  UsersRepo usersRepo;
    private EmailUtils emailUtils;

    public UserServiceImpl(UsersRepo usersRepo,EmailUtils emailUtils) {
        this.usersRepo = usersRepo;
        this.emailUtils=emailUtils;
    }

    public Boolean registerUser(UserRequest request){
        UserMaster master = new UserMaster();
        BeanUtils.copyProperties(request,master);
        master.setPassword(generatePassword());
        master.setAccStatus("In-Active");
        String fileName = "RegisterBody.txt";
        String subject = "User Registeration Success Mail";
        String emailBody = readEmailBody(request.getUserName(),master.getPassword(),fileName);
        emailUtils.sendMail(request.getEmailId(),subject,fileName);

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
        if(userMaster.getAccStatus().equals("In-Active")){
            userMaster.setAccStatus("Active");
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
        String url ="";
        String mailBody = null;

        try(FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line!=null){
                sb.append(line);
                line=br.readLine();
            }
            mailBody= br.toString();
            mailBody.replace("{FULL-NAME}",fullName);
            mailBody.replace("{TEMP-PWD}",pass);
            mailBody.replace("URL",url);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mailBody;
    }

}
