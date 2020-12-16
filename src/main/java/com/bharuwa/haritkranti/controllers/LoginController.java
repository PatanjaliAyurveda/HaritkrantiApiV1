package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.exceptionHandler.ResourceAlreadyExists;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastWeekly;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.newmodels.WeatherData;
import com.bharuwa.haritkranti.models.requestModels.FarmerVerificationReqBody;
import com.bharuwa.haritkranti.models.requestModels.LoginUser;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBodyForAppUser;
import com.bharuwa.haritkranti.models.requestModels.VerifyOtp;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author anuragdhunna
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {

	@ApiOperation(value = "SignUp API for Farmers")
    @RequestMapping(value = "/registerFarmer", method = RequestMethod.POST)
    public User registerFarmer(@RequestBody UserReqBody userReqBody) throws Exception {
        return loginService.createFarmer(userReqBody);
    }
	
	@ApiOperation(value = "SignUp API for Farmers from App")
    @RequestMapping(value = "/registerFarmerFromApp", method = RequestMethod.POST)
    public User registerFarmerFromApp(@RequestBody UserReqBodyForAppUser userReqBody) throws Exception {
       return loginService.createFarmerFromApp(userReqBody);
    }
	
	@RequestMapping(value = "/uploadFarmerImage", method = RequestMethod.POST)
    public User uploadFarmerImage(@RequestParam("image") MultipartFile image,@RequestParam String primaryPhone) throws Exception {
		User userExist = userService.getUserByPhoneNum(primaryPhone);
		// return loginService.createFarmer(userReqBody);
		//image upload to AWS

//      String fileUrl = awsClientService.uploadFile(aadharImageFile);

		String fileUrl = awsClientService.uploadFile(image);
		userExist.setProfileImage(fileUrl);
		userService.saveUser(userExist);
        //url update in our database

       // User userFetched=userService.getUserByPhoneNum(phoneNumber);

      //  userFetched.setIdImage(fileUrl);

      //  userService.saveUser(userFetched);

		//return userFetched;
		return userExist;
    }
	
	@RequestMapping(value = "/updateFarmerName", method = RequestMethod.POST)
    public User updateFarmerName(@RequestParam String primaryPhone,@RequestParam String name) throws Exception {
		User userExist = userService.getUserByPhoneNum(primaryPhone);
		userExist.setFirstName(name);
		userService.saveUser(userExist);
		return userExist;
    }
	
	@RequestMapping(value = "/updateFarmerStatus", method = RequestMethod.POST)
    public User updateFarmerStatus(@RequestParam String primaryPhone,@RequestParam String status) throws Exception {
		User userExist = userService.getUserByPhoneNum(primaryPhone);
		userExist.setFarmerStatus(status);
		userService.saveUser(userExist);
		return userExist;
    }
	
	@ApiOperation(value = "Update API for Farmers")
    @RequestMapping(value = "/updateFarmer", method = RequestMethod.POST)
    public User updateFarmer(@RequestBody UserReqBody userReqBody) throws Exception {
		return loginService.updateFarmer(userReqBody);
    }
	
	@ApiOperation(value = "Reset Password API for Farmers")
    @RequestMapping(value = "/resetFarmerPassword", method = RequestMethod.POST)
    public User resetFarmerPassword(@RequestBody UserReqBody userReqBody) throws Exception {
        return loginService.resetPassword(userReqBody);
    }
	
	@ApiOperation(value = "Get Farmer Data")
    @RequestMapping(value = "/getFarmerData", method = RequestMethod.GET)
    public User getFarmerData(@RequestParam String primaryPhone) throws Exception {
        return loginService.getFarmer(primaryPhone);
//		return null;
    }
	
    @RequestMapping(value = "/adminLogin",method = RequestMethod.POST)
    public User adminLogin(@RequestBody LoginUser loginUser){
        return loginService.adminLogin(loginUser);
    }
    
    @RequestMapping(value = "/farmerLogin",method = RequestMethod.POST)
    public User farmerLogin(@RequestBody LoginUser loginUser){
        return loginService.farmerLogin(loginUser);
    }
    
    @RequestMapping(value = "/getWeatherData",method = RequestMethod.POST)
    public WeatherData getWeatherData(@RequestBody String phoneNumber){
    	System.out.println(phoneNumber);
    	phoneNumber = phoneNumber.substring(0,10);
    	return loginService.getWeatherData(phoneNumber);
    	//System.out.println(phoneNumber);
        //return null;
    }
    
    @RequestMapping(value = "/getWeatherForecastData",method = RequestMethod.POST)
    public WeatherForecastWeekly getWeatherForecastData(@RequestBody String phoneNumber){
    	System.out.println(phoneNumber);
    	phoneNumber = phoneNumber.substring(0,10);
    	return loginService.getWeatherForecastData(phoneNumber);
    	//System.out.println(phoneNumber);
        //return null;
    }

    @ApiOperation(value = "Login API for Managers, Agent, Farmer")
    @RequestMapping(value = "/sendRegistrationOTP", method = RequestMethod.POST)
    public String sendOTPForRegistration(@RequestParam String phoneNumber, @Nullable @RequestParam String source) throws Exception {
        loginService.sendResgistrationOTP(phoneNumber.trim(), source);
        return "OTP Send";
    }
    
    @ApiOperation(value = "Login API for Managers, Agent, Farmer")
    @RequestMapping(value = "/sendResetPasswordOTP", method = RequestMethod.POST)
    public String sendOTPForResetPassword(@RequestParam String phoneNumber, @Nullable @RequestParam String source) throws Exception {
        loginService.sendResetPasswordOTP(phoneNumber.trim(), source);
        return "OTP Send";
    }
    
    /**
     * Send OTP
     * If the user is not registered then throw Exception
     * @param phoneNumber user phone Number
     * @param source WEB or APP-AGENT, APP-FARMER for filtering the login between Managers, Agent and Farmer
     * @return user
     * @throws Exception
     */
    @ApiOperation(value = "API for Farmer Login from App")
    @RequestMapping(value = "/sendOTP", method = RequestMethod.POST)
    public String sendOTP(@RequestParam String phoneNumber, @RequestParam String source, @RequestParam String languageName) throws Exception {
      //  return loginService.sendOTP(phoneNumber.trim(), source);
    	return loginService.sendOTPForAppUser(phoneNumber, source, languageName);
    }

    /**
     * Verify OTP
     * @param verifyOtp
     * @return
     */
    @RequestMapping(value = "/verifyOTP", method = RequestMethod.POST)
    public User verifyOTP(@RequestBody VerifyOtp verifyOtp) {
        return loginService.verifyOTPForAppUser(verifyOtp);
    }

    /**
     * 
     * 
     *  signUp
     * If the user is not registered then a new user will be created
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public User signUp(@RequestBody User user) throws Exception, ResourceAlreadyExists {
        return loginService.signUp(user);
    }

    @Deprecated
    /**
     *  signUpForAgent
     * If the user is not registered then a new user will be created
     * @param user
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = "/signUpForAgent", method = RequestMethod.POST)
    public User signUpForAgent(@RequestBody User user) throws Exception,ResourceAlreadyExists {
        return loginService.signUpForAgent(user);
    }

    @ApiOperation(value = "SignUp API for Managers or Agents or Accountants")
    @RequestMapping(value = "/createManagerAgent", method = RequestMethod.POST)
    public User createManagerAgent(@RequestBody UserReqBody userReqBody) throws Exception {
        return loginService.createManagerAgent(userReqBody);
    }

    /**
     * Send OTP
     * Api use for marketing App
     * if your is exist on farmer app than otp store in existing user table otherwise a new user will be created
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Login API for Seller or Buyer")
    @RequestMapping(value = "/sendMobileOTP", method = RequestMethod.POST)
    public String sendMobileOTP(@RequestParam String phoneNumber) throws Exception {
        return loginService.sendMoblieOTP(phoneNumber);
    }


    /**
     * send OTP to Farmer's number to check whether farmer provides a valid phone Number to DKD agent or not
     * @param farmerVerificationReqBody
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "API for sending OTP to verify Farmer's Phone Number")
    @RequestMapping(value = "/sendOTPFarmerVerification", method = RequestMethod.POST)
    public String sendOTPFarmerVerification(@RequestBody FarmerVerificationReqBody farmerVerificationReqBody) throws Exception {
        return loginService.sendOTPFarmerVerification(farmerVerificationReqBody);
    }

    /**
     * Verify OTP to check whether farmer provides a valid phone Number to DKD agent or not
     * @param verifyOtp
     * @return
     */
    @RequestMapping(value = "/verifyOTPFarmerVerification", method = RequestMethod.POST)
    public String verifyOTPFarmerVerification(@RequestBody VerifyOtp verifyOtp) {
        return loginService.verifyOTPFarmerVerification(verifyOtp);
    }

}