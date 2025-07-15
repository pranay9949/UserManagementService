package com.example.UserManagementService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Table(name="User_Master")
@Entity
@Data
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Integer userId;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="EMAIL_ID")
    private String emailId;

    @Column(name="MOBILE_NUMBER")
    private Long mobileNumber;

    @Column(name="GENDER")
    private Character gender;

    @Column(name="DOB")
   private LocalDate dateOfBirth;

    @Column(name="SSN")
   private Long ssn;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="ACCOUNT_STATUS")
    private String accStatus;

    private Boolean isVerifiedEmail;

    @Column(name = "CREATED_DATE", updatable = false)
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "UPDATED_DATE", insertable = false)
    @UpdateTimestamp
    private LocalDate updatedDate;

   @Column(name="CREATED_BY")
    private String createdBy;

   @Column(name="UPDATED_BY")
    private String updatedBy;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getSsn() {
        return ssn;
    }

    public void setSsn(Long ssn) {
        this.ssn = ssn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public Boolean getVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setVerifiedEmail(Boolean verifiedEmail) {
        isVerifiedEmail = verifiedEmail;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
