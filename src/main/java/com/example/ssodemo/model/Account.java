package com.example.ssodemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Set;

@MappedSuperclass
public class Account extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 50)
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty(message = "name is not empty")
    @Size(min = 3, max = 50)
    @Column(name = "full_name")
    private String fullName;

    //    @NotEmpty(message = "fistName is not empty")
    @Size(min = 1, max = 25)
    @Column(name = "first_name")
    private String fistName;

    //    @NotEmpty(message = "lastName is not empty")
    @Size(min = 1, max = 25)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "national_code")
    private String nationalCode; // phone number national code prefix ( +84, ... )

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "status")
    private Integer status;

    @Column(name = "account_type")
    private Integer accountType;

    @Transient
    private Set<String> roles;


    @NotEmpty
    @Size(max = 50)
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_expire_date")
    private Date passwordExpireDate;

    @Column(name = "trial_status")
    private Integer trialStatus;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "trial_start_date")
    private Date trialStartDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "trial_end_date")
    private Date trialEndDate;

    @Column(name = "account_purpose")
    private Integer accountPurpose;

    @Column(name = "hifa_verifying")
    private Integer hifaVerifying = 0;

    /**
     *
     */
    public Account() {
        super();
    }

    /**
     * @param id
     * @param username
     * @param password
     * @param enabled
     * @param credentialsExpired
     * @param expired
     * @param locked
     * @param roles
     * @param email
     */
    public Account(String fullName, Long id, String username, String password, boolean enabled,
                   boolean credentialsExpired, boolean expired, boolean locked, Set<String> roles,
                   String email) {
        super();
        this.fullName = fullName;
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }

    public Account(Account account) {
        super();
        this.fullName = account.getFullName();
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.email = account.getEmail();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getPasswordExpireDate() {
        return passwordExpireDate;
    }

    public void setPasswordExpireDate(Date passwordExpireDate) {
        this.passwordExpireDate = passwordExpireDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getTrialStatus() {
        return trialStatus;
    }

    public void setTrialStatus(Integer trialStatus) {
        this.trialStatus = trialStatus;
    }

    public Date getTrialStartDate() {
        return trialStartDate;
    }

    public void setTrialStartDate(Date trialStartDate) {
        this.trialStartDate = trialStartDate;
    }

    public Date getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(Date trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public Integer getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(Integer accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public Integer getHifaVerifying() {
        return hifaVerifying;
    }

    public void setHifaVerifying(Integer hifaVerifying) {
        this.hifaVerifying = hifaVerifying;
    }
}
