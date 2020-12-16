package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.City;
import com.bharuwa.haritkranti.models.location.State;

/**
 * @author anuragdhunna
 */
public class Dealer extends BaseObject {

    private String nameOfFarm;
    private String address;
    private City city;
    private State state;
    private String pincode;
    private String telephone;
    private String phoneNumber;
    private String fertLicenseNo;
    private String officerName;
    private String contactPerson;
    private String location;
    private double latitude;
    private double longitude;


    public String getNameOfFarm() {
        return nameOfFarm;
    }

    public void setNameOfFarm(String nameOfFarm) {
        this.nameOfFarm = nameOfFarm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFertLicenseNo() {
        return fertLicenseNo;
    }

    public void setFertLicenseNo(String fertLicenseNo) {
        this.fertLicenseNo = fertLicenseNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
