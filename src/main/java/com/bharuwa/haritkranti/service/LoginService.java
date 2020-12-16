package com.bharuwa.haritkranti.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.newmodels.WeatherData;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastWeekly;
import com.bharuwa.haritkranti.models.requestModels.FarmerVerificationReqBody;
import com.bharuwa.haritkranti.models.requestModels.LoginUser;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBodyForAppUser;
import com.bharuwa.haritkranti.models.requestModels.VerifyOtp;

/**
 * @author anuragdhunna
 */
public interface LoginService {

    void adminSignUp(User user,Boolean viewAccessOnly);

    User adminLogin(LoginUser loginUser);

    User login(LoginUser loginUser);

    User sendOTP(String phoneNumber, String source) throws Exception;
    
    public String sendOTPForAppUser(String phoneNumber, String source, String languageName) throws Exception ;

    User verifyOTP(VerifyOtp verifyOtp);

    User signUp(User user) throws Exception;

    @Deprecated
    User signUpForAgent(User user) throws Exception;

    User createManagerAgent(UserReqBody userReqBody) throws Exception;
    
    User createFarmer(UserReqBody userReqBody) throws Exception;

    String sendMoblieOTP(String phoneNumber) throws Exception;

    String sendOTPFarmerVerification(FarmerVerificationReqBody farmerVerificationReqBody) throws Exception;

    String verifyOTPFarmerVerification(VerifyOtp verifyOtp);
    
    public void sendResgistrationOTP(String phoneNumber, String source) throws Exception;
    
    public User verifyRegistrationOTP(VerifyOtp verifyOtp);
    
    public void sendResetPasswordOTP(String phoneNumber, String source) throws Exception;
    
    public User resetPassword(UserReqBody userReqBody) throws Exception;
    
    public User farmerLogin(LoginUser loginUser) throws AuthenticationException;
    
    public WeatherData getWeatherData(String phoneNumber);
    
    public WeatherForecastWeekly getWeatherForecastData(String phoneNumber);
    
    public User getFarmer(String primaryPhone) throws Exception;
    
    public User updateFarmer(UserReqBody userReqBody) throws Exception;
    
    public User verifyOTPForAppUser(VerifyOtp verifyOtp);
    
    public User createFarmerFromApp(UserReqBodyForAppUser userReqBody) throws Exception;
    
}
