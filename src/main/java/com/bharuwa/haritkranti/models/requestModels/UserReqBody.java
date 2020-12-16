package com.bharuwa.haritkranti.models.requestModels;

import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.User;

import javax.validation.constraints.Email;
import java.util.Date;

/**
 * @author harman
 */
public class UserReqBody {

    private String firstName;

    private String middleName;

    private String lastName;

    private String fatherName;
    
    private String password;

    private Address addressModel = new Address();

    private String primaryPhone;

    private  String profileImage;

    private User.Qualification qualification = null;

    private String aadhaarCardNumber;

    private User.Gender gender = User.Gender.MALE;

    private String religion;

    private User.Category category = User.Category.GN;

    private Date dateOfBirth;

    @Email
    private String email;

    private String createdByUserId;

    private String bankAccountNumber;

    private String ifscCode;

    private String bankPassBookImage;

    private String aadharCardImage;

    private String roleName;
    
    private String otp;

    private User.CreatedVia createdVia = User.CreatedVia.AGENT;

    private String accountHolderName;

    private String bankName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Address getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(Address addressModel) {
        this.addressModel = addressModel;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User.Qualification getQualification() {
        return qualification;
    }

    public void setQualification(User.Qualification qualification) {
        this.qualification = qualification;
    }

    public String getAadhaarCardNumber() {
        return aadhaarCardNumber;
    }

    public void setAadhaarCardNumber(String aadhaarCardNumber) {
        this.aadhaarCardNumber = aadhaarCardNumber;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public User.Category getCategory() {
        return category;
    }

    public void setCategory(User.Category category) {
        this.category = category;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankPassBookImage() {
        return bankPassBookImage;
    }

    public void setBankPassBookImage(String bankPassBookImage) {
        this.bankPassBookImage = bankPassBookImage;
    }

    public String getAadharCardImage() {
        return aadharCardImage;
    }

    public void setAadharCardImage(String aadharCardImage) {
        this.aadharCardImage = aadharCardImage;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public User.CreatedVia getCreatedVia() {
        return createdVia;
    }

    public void setCreatedVia(User.CreatedVia createdVia) {
        this.createdVia = createdVia;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
