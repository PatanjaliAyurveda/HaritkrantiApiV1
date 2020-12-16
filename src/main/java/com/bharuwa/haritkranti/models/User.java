package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.Tehsil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author anuragdhunna
 */
@Document(collection = "user")
public class User extends BaseObject {

    public enum Qualification {
        UNEDUCATED, PRIMARY, SECONDARY, SENIOR_SECONDARY, GRADUATE, POST_GRADUATE, DOCTORAL_DEGREE
    }

    public enum Gender {
        MALE, FEMALE, TRANSGENDER
    }

    public enum Category {
        GN, SC, ST, OBC, BC
    }

    public enum Relationship {
        GrandFather, GrandMother, Father, Mother,Brother, Sister, Husband, Wife, Son, Daughter, GrandSon, GrandDaughter, Other
    }

    public enum CreatedVia {
        SIGNUP, AGENT, ADMIN, MARKET_APP
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private String nameInHindi;

    private String fatherName;

    private String password;
    private String desc;
    private LocalDateTime passwordResetTime;
    private boolean registered;
    
    private String khasraNumber;
	private String noOfMale;
	private String noOfFeMale;
	private String noOfChildren;
	private String farmerStatus;
	private String farmerStatusInHindi;

    @Deprecated
    private String state;
    @Deprecated
    private String district;
    @Deprecated
    private String village;

    private Address addressModel = new Address();
    
    private AddressInHindi addressModelInHindi = new AddressInHindi();

    @DBRef
    private Tehsil tehsil;

    @Deprecated
    private String stateId;
    @Deprecated
    private String districtId;

    private Set<String> villageList;
    private String landSizeAcre;

    private Set<String> landKhasraNos;

    private Set<String> tehsils;

    @Indexed(unique = true)
    private String primaryPhone;

    private Language language = Language.English;

    @Indexed
    private List<Role> roles = new ArrayList<>();

    private  String profileImage;

    private OTP otp;

    private String authToken;
    private LocalDateTime authTokenCreationTime;

    private CreatedVia createdVia = CreatedVia.AGENT;

    private Qualification qualification = null;

    private String aadhaarCardNumber;
    
    private Gender gender = Gender.MALE;

    private String religion;

    private Category category = Category.GN;

    private Date dateOfBirth;

    @Deprecated
    private String address;

    @Email
    private String email;
    
    private boolean requirePassword = false;

    @Indexed(unique = true)
    private String userCode;

    private Relationship relationship = Relationship.Other;

    private String createdByUserId;

    @Deprecated
    private boolean hasFarmers = false;

    private boolean hasUser = false;

    private String vendorCode;

    private boolean phoneNumberVerified = false;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLandSizeAcre() {
        return landSizeAcre;
    }

    public void setLandSizeAcre(String landSizeAcre) {
        this.landSizeAcre = landSizeAcre;
    }


    public Set<String> getLandKhasraNos() {
        return landKhasraNos;
    }

    public void setLandKhasraNos(Set<String> landKhasraNos) {
        this.landKhasraNos = landKhasraNos;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public OTP getOtp() {
        return otp;
    }

    public void setOtp(OTP otp) {
        this.otp = otp;
    }

    private enum Language {
        English, Hindi, Punjabi
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Set<String> getVillageList() {
        return villageList;
    }

    public void setVillageList(Set<String> villageList) {
        this.villageList = villageList;
    }

    public Set<String> getTehsils() {
        return tehsils;
    }

    public void setTehsils(Set<String> tehsils) {
        this.tehsils = tehsils;
    }

    public CreatedVia getCreatedVia() {
        return createdVia;
    }

    public void setCreatedVia(CreatedVia createdVia) {
        this.createdVia = createdVia;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public void addRole(Role role) {
        if (role == null) return;
        if (roles == null) roles = new ArrayList<>();
//        roles.clear();
        roles.add(role);
    }

    public Role getRole(String roleName) {
        if (roles == null) return null;
        for (Role role : roles) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    public void removeRole(Role role) {
        if (role == null || getRoles() == null || getRoles().isEmpty()) return;
        getRoles().remove(role);
    }

	
	public Address getAddressModel() {
		return addressModel;
	}
	  
	public void setAddressModel(Address addressModel) { 
		this.addressModel =addressModel;
	}

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Tehsil getTehsil() {
        return tehsil;
    }

    public void setTehsil(Tehsil tehsil) {
        this.tehsil = tehsil;
    }

    public boolean isRequirePassword() {
        return requirePassword;
    }

    public void setRequirePassword(boolean requirePassword) {
        this.requirePassword = requirePassword;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isHasFarmers() {
        return hasFarmers;
    }

    public void setHasFarmers(boolean hasFarmers) {
        this.hasFarmers = hasFarmers;
    }

    public boolean isHasUser() {
        return hasUser;
    }

    public void setHasUser(boolean hasUser) {
        this.hasUser = hasUser;
    }

    public LocalDateTime getPasswordResetTime() {
        return passwordResetTime;
    }

    public void setPasswordResetTime(LocalDateTime passwordResetTime) {
        this.passwordResetTime = passwordResetTime;
    }

    public LocalDateTime getAuthTokenCreationTime() {
        return authTokenCreationTime;
    }

    public void setAuthTokenCreationTime(LocalDateTime authTokenCreationTime) {
        this.authTokenCreationTime = authTokenCreationTime;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

	public String getAadhaarCardNumber() {
		return aadhaarCardNumber;
	}

	public void setAadhaarCardNumber(String aadhaarCardNumber) {
		this.aadhaarCardNumber = aadhaarCardNumber;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getKhasraNumber() {
		return khasraNumber;
	}

	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}

	public String getNoOfMale() {
		return noOfMale;
	}

	public void setNoOfMale(String noOfMale) {
		this.noOfMale = noOfMale;
	}

	public String getNoOfFeMale() {
		return noOfFeMale;
	}

	public void setNoOfFeMale(String noOfFeMale) {
		this.noOfFeMale = noOfFeMale;
	}

	public String getNoOfChildren() {
		return noOfChildren;
	}

	public void setNoOfChildren(String noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public String getFarmerStatus() {
		return farmerStatus;
	}

	public void setFarmerStatus(String farmerStatus) {
		this.farmerStatus = farmerStatus;
	}

	public String getNameInHindi() {
		return nameInHindi;
	}

	public void setNameInHindi(String nameInHindi) {
		this.nameInHindi = nameInHindi;
	}

	public String getFarmerStatusInHindi() {
		return farmerStatusInHindi;
	}

	public void setFarmerStatusInHindi(String farmerStatusInHindi) {
		this.farmerStatusInHindi = farmerStatusInHindi;
	}

	public AddressInHindi getAddressModelInHindi() {
		return addressModelInHindi;
	}

	public void setAddressModelInHindi(AddressInHindi addressModelInHindi) {
		this.addressModelInHindi = addressModelInHindi;
	}

}

