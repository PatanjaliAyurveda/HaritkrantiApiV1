package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.configuration.TokenProvider;
import com.bharuwa.haritkranti.models.newmodels.WeatherForecastWeekly;
import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.exceptionHandler.ResourceAlreadyExists;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.exceptionHandler.UnauthorizedException;
import com.bharuwa.haritkranti.models.newmodels.AppLoginTable;
import com.bharuwa.haritkranti.models.newmodels.MandiRateRecord;
import com.bharuwa.haritkranti.models.newmodels.WeatherData;
import com.bharuwa.haritkranti.models.Address;
import com.bharuwa.haritkranti.models.AddressInHindi;
import com.bharuwa.haritkranti.models.FinancialDetails;
import com.bharuwa.haritkranti.models.OTP;
import com.bharuwa.haritkranti.models.RegistrationOtp;
import com.bharuwa.haritkranti.models.Role;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.UserKhasraMapping;
import com.bharuwa.haritkranti.models.payments.EmployeeAssignmentHistory;
import com.bharuwa.haritkranti.models.payments.EmployeeHistory;
import com.bharuwa.haritkranti.models.requestModels.FarmerVerificationReqBody;
import com.bharuwa.haritkranti.models.requestModels.LoginUser;
import com.bharuwa.haritkranti.models.requestModels.UserReqBody;
import com.bharuwa.haritkranti.models.requestModels.UserReqBodyForAppUser;
import com.bharuwa.haritkranti.models.requestModels.VerifyOtp;
import com.bharuwa.haritkranti.models.responseModels.CountAll;
import com.bharuwa.haritkranti.msg91.Msg91Services;
import com.bharuwa.haritkranti.repositories.UserRepo;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.utils.GenericMongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.bharuwa.haritkranti.models.User.CreatedVia.MARKET_APP;
import static com.bharuwa.haritkranti.service.impl.GenerateReportServiceImpl.getUserMainRole;
import static com.bharuwa.haritkranti.utils.Constants.*;
import static com.bharuwa.haritkranti.utils.HelperMethods.*;

/**
 * @author anuragdhunna
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final MongoTemplate mongoTemplate;
    
    public LoginServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate; 
    }

    @Value("${agent_app_link}")
    private String agentAppLink;
    @Value("${admin_panel_link}")
    private String adminPanelLink;

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private LandService landService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    EmployeeHistoryService employeeHistoryService;

    @Autowired
    private CountService countService;

    @Autowired
    GenericMongoTemplate genericMongoTemplate;


    @Override
    public void adminSignUp(User user, Boolean viewAccessOnly) {

        User userExist = userService.getUserByPhoneNum(user.getPrimaryPhone());
        logger.info("admin");

        if (userExist == null) {
            // Signup

            // get Roles
            List<Role> roleList = new ArrayList<>();
            if(viewAccessOnly){
                Role adminView = roleService.roleName(ROLE_ADMIN_VIEW);
                roleList.add(adminView);
                logger.info("admin view added");
            } else {
                Role role = roleService.roleName(ROLE_ADMIN);
                roleList.add(role);
                logger.info("admin  added");

            }
            user.setRoles(roleList);
            user.setPrimaryPhone(user.getPrimaryPhone());

            //generate Unique userCode
            user.setUserCode(generateUniqueCodeForUser());

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActive(true);
            user.setCreationDate(Calendar.getInstance().getTime());

            userService.saveUser(user);
        }
    }

    @Override
    public User adminLogin(LoginUser loginUser) {

        User user = login(loginUser);
        Role role = roleService.roleName(ROLE_ADMIN);
        Role adminView = roleService.roleName(ROLE_ADMIN_VIEW);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role agentManager = roleService.roleName(ROLE_AGENT_MANAGER);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(adminView);
        roleList.add(stateManager);
        roleList.add(districtManager);
        roleList.add(agentManager);

        if (Collections.disjoint(roleList, user.getRoles())) {
            throw new UnauthorizedException("You don't have permission");
        }
        return user;
    }

    @Override
    public User login(LoginUser loginUser) throws AuthenticationException {

        if (StringUtils.isEmpty(loginUser.getUsername()) || StringUtils.isEmpty(loginUser.getPassword())) {
            throw new CustomException("Credentials are not valid");
        }
        User user = userService.getUserByPhoneNum(loginUser.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("Invalid credentials. Please try again");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
    //    System.out.println(encoder.);
        if (user.getDesc()==null && !encoder.matches(loginUser.getPassword(), user.getPassword())) {// Modified BY SONU ON 14/09/2020
            logger.info("encoder===ifff===", encoder);
            throw new CustomException("Invalid credentials. please try again.");
        }
        
       

        if (!user.isActive()) {
            throw new CustomException("Account is Disabled");
        }
        String pass="";
        if(user.getDesc()!=null) {
        	pass = user.getDesc();
        }else
        	pass = loginUser.getPassword();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        pass
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        user.setAuthToken(token);
        user.setAuthTokenCreationTime(LocalDateTime.now());
        user.setDesc(pass);
        userService.saveUser(user);
        return user;
    }
    
    
    @Override
    public User farmerLogin(LoginUser loginUser) throws AuthenticationException {

        if (StringUtils.isEmpty(loginUser.getUsername()) || StringUtils.isEmpty(loginUser.getPassword())) {
            throw new CustomException("Credentials are not valid");
        }
        User user = userService.getUserByPhoneNum(loginUser.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("Mobile number is not registered");
        }
        
        if (user != null && user.getPassword()==null) {
            throw new ResourceNotFoundException("You have entered wrong password");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        
        if (!encoder.matches(loginUser.getPassword(), user.getPassword())) {
           // logger.info("encoder===ifff===", encoder);
            throw new ResourceNotFoundException("You have entered wrong password");
        }

        if (!user.isActive()) {
            throw new ResourceNotFoundException("Account is Disabled");
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        user.setAuthToken(token);
        user.setAuthTokenCreationTime(LocalDateTime.now());

        userService.saveUser(user);
        return user;
    }
    
    @Override
    public User getFarmer(String primaryPhone) throws Exception {
        validateNumber(primaryPhone.trim());
        User user = userService.getUserByPhoneNum(primaryPhone.trim());
        return user;
    }
	
    @Override
    public User sendOTP(String phoneNumber, String source) throws Exception {
    	
        if (StringUtils.isEmpty(source)) {
            throw new CustomException("Please Update your mobile app");
        }
        validateNumber(phoneNumber);
        User userExist = userService.getUserByPhoneNum(phoneNumber);
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not Exist, Please ask you Manager for SignUp");
        }
        if (!userExist.isActive() || userExist.isDeleted()) {
            throw new UnauthorizedException("You are not authorized");
        }
        // Filter Login between Managers and (Agent and Farmer)
        List<Role> userRoles = userExist.getRoles();
        boolean isAuthorized;
        switch (source) {
            case "WEB": {
                isAuthorized = userRoles.stream().anyMatch(item -> item.getRoleName().contains("USER"));
                break;
            }
            case "APP": {
                isAuthorized = userRoles.stream().anyMatch(item -> item.getRoleName().contains("USER"));
                break;
            }
            case "APP-AGENT": {
                isAuthorized = userRoles.stream().anyMatch(item -> item.getRoleName().equals(ROLE_AGENT));
                break;
            }
            case "APP-FARMER": {
                isAuthorized = userRoles.stream().anyMatch(item -> item.getRoleName().equals(ROLE_USER));
                break;
            }

            default:
                throw new UnauthorizedException("You are not authorized......");
        }

        if (!isAuthorized) {
            logger.info(userExist.getPrimaryPhone() + "is trying to login on "+source);
            throw new UnauthorizedException("You are not authorized......");
        }

        OTP mOTP = generateOtp();
        System.out.println("========otp======="+mOTP.getOtp());
        userExist.setOtp(mOTP);
        userService.saveUser(userExist);

        Msg91Services.sendMsg(mOTP.getOtp(), userExist.getPrimaryPhone());
        return userExist;
    }
    
    @Override
    public String sendOTPForAppUser(String phoneNumber, String source, String languageName) throws Exception {
    	
        if (StringUtils.isEmpty(source)) {
            throw new CustomException("Please Update your mobile app");
        }
        validateNumber(phoneNumber);
        User userExist = userService.getUserByPhoneNum(phoneNumber);

        OTP mOTP = generateOtp();
        System.out.println("========otp======="+mOTP.getOtp());
        AppLoginTable loginTable = new AppLoginTable(phoneNumber, mOTP, languageName);
        if(userExist!=null)
        	loginTable.setRegistered(true);

        userService.saveAppLoginOtp(loginTable);

        Msg91Services.sendMsg(mOTP.getOtp(),phoneNumber);
        return mOTP.getOtp();
    }
    
    @Override
    public WeatherData getWeatherData(String phoneNumber){
    	User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String zipCode = userExist.getAddressModel().getZipCode();
    	String uri = "http://api.openweathermap.org/data/2.5/weather?zip="+zipCode+",in&appid=664913f5ddb12a56230ed1d59f171017";
	    
    	RestTemplate restTemplate = new RestTemplate();
	    WeatherData result = restTemplate.getForObject(uri,WeatherData.class);
//	    System.out.println(result.getCoord().getLat());
	    return result;
    }
    
    @Override
    public WeatherForecastWeekly getWeatherForecastData(String phoneNumber){
    	//final String uri = "https://samples.openweathermap.org/data/2.5/forecast/daily?zip=824201,in&appid=439d4b804bc8187953eb36d2a8c26a02";
      //  RestTemplate restTemplate = new RestTemplate();
     //   WeatherForecastWeekly result = restTemplate.getForObject(uri,WeatherForecastWeekly.class);
     //   System.out.println(result.getCity().getCoord().getLat());
        
    	
    	User userExist = userService.getUserByPhoneNum(phoneNumber);
    	String zipCode = userExist.getAddressModel().getZipCode();
    	String uri = "https://samples.openweathermap.org/data/2.5/forecast/daily?zip="+zipCode+",in&appid=664913f5ddb12a56230ed1d59f171017";
	    
    	RestTemplate restTemplate = new RestTemplate();
    	WeatherForecastWeekly result = restTemplate.getForObject(uri,WeatherForecastWeekly.class);
	    System.out.println(result.getCity().getCoord());
	    return result;
    }
    
    @Override
    public void sendResgistrationOTP(String phoneNumber, String source) throws Exception {

        if (StringUtils.isEmpty(source)) {
            throw new CustomException("Please Update your mobile app");
        }

        validateNumber(phoneNumber);

       // User userExist = userService.getUserByPhoneNum(phoneNumber);
        
      //  if (userExist == null) {
      //      throw new ResourceNotFoundException("User does not Exist, Please ask you Manager for SignUp");
      //  }

     //   if (!userExist.isActive() || userExist.isDeleted()) {
     //       throw new UnauthorizedException("You are not authorized");
     //   }

        // Filter Login between Managers and (Agent and Farmer)
     //   List<Role> userRoles = userExist.getRoles();
		/*
		 * boolean isAuthorized; switch (source) { case "WEB": { isAuthorized =
		 * userRoles.stream().anyMatch(item -> item.getRoleName().contains("USER"));
		 * break; } case "APP-AGENT": { isAuthorized = userRoles.stream().anyMatch(item
		 * -> item.getRoleName().equals(ROLE_AGENT)); break; } case "APP-FARMER": {
		 * isAuthorized = userRoles.stream().anyMatch(item ->
		 * item.getRoleName().equals(ROLE_USER)); break; }
		 * 
		 * default: throw new UnauthorizedException("You are not authorized......"); }
		 * 
		 * if (!isAuthorized) { logger.info(userExist.getPrimaryPhone() +
		 * "is trying to login on "+source); throw new
		 * UnauthorizedException("You are not authorized......"); }
		 */

        OTP mOTP = generateOtp();
        System.out.println("========otp======="+mOTP.getOtp());
       // userExist.setOtp(mOTP);
        RegistrationOtp otp = new RegistrationOtp();
        otp.setPhoneNumber(phoneNumber);
        otp.setOtp(mOTP.getOtp());
        otp.setExpiryTime(mOTP.getExpiryTime());
        userService.save(otp);

        Msg91Services.sendMsg(mOTP.getOtp(), phoneNumber);
      //  return userExist;
    }
    
    @Override
    public void sendResetPasswordOTP(String phoneNumber, String source) throws Exception {

        if (StringUtils.isEmpty(source)) {
            throw new CustomException("Please Update your mobile app");
        }

        validateNumber(phoneNumber);

        User userExist = userService.getUserByPhoneNum(phoneNumber);
        
        if (userExist == null) {
            throw new ResourceNotFoundException("User does not Exist, Please register yourself");
        }

     //   if (!userExist.isActive() || userExist.isDeleted()) {
     //       throw new UnauthorizedException("You are not authorized");
     //   }

        // Filter Login between Managers and (Agent and Farmer)
     //   List<Role> userRoles = userExist.getRoles();
		/*
		 * boolean isAuthorized; switch (source) { case "WEB": { isAuthorized =
		 * userRoles.stream().anyMatch(item -> item.getRoleName().contains("USER"));
		 * break; } case "APP-AGENT": { isAuthorized = userRoles.stream().anyMatch(item
		 * -> item.getRoleName().equals(ROLE_AGENT)); break; } case "APP-FARMER": {
		 * isAuthorized = userRoles.stream().anyMatch(item ->
		 * item.getRoleName().equals(ROLE_USER)); break; }
		 * 
		 * default: throw new UnauthorizedException("You are not authorized......"); }
		 * 
		 * if (!isAuthorized) { logger.info(userExist.getPrimaryPhone() +
		 * "is trying to login on "+source); throw new
		 * UnauthorizedException("You are not authorized......"); }
		 */

        OTP mOTP = generateOtp();
        System.out.println("========otp======="+mOTP.getOtp());
       // userExist.setOtp(mOTP);
        RegistrationOtp otp = new RegistrationOtp();
        otp.setPhoneNumber(phoneNumber);
        otp.setOtp(mOTP.getOtp());
        otp.setExpiryTime(mOTP.getExpiryTime());
        userService.save(otp);

        Msg91Services.sendMsg(mOTP.getOtp(), phoneNumber);
      //  return userExist;
    }

    @Override
    public User verifyOTP(VerifyOtp verifyOtp) {
        validateNumber(verifyOtp.getPhoneNumber());
        System.out.println("========otp======="+verifyOtp.getOtp());

        User user = userService.getUserByPhoneNum(verifyOtp.getPhoneNumber());
        if (user == null) {
            throw new ResourceNotFoundException("User not Exist, Please ask you Manager for SignUp");
        }
        //sonu

        OTP mOTP = user.getOtp();
        String userOtp = mOTP.getOtp();
        if (!userOtp.equals(verifyOtp.getOtp())) {
            throw new CustomException("OTP doesn't match");
        }

        Date expiryTime;
        if (user.getOtp() == null) {
            logger.warn("===otp not found for userId===", user.getId());
            throw new CustomException("OTP does not match");
        }
        expiryTime = user.getOtp().getExpiryTime();

        Date nowDate = new Date();
        if (nowDate.after(expiryTime)) {
            throw new CustomException("OTP expired");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(user.getPrimaryPhone());

        // tODO: need to check this
        loginUser.setPassword(user.getPrimaryPhone());

        // TODO: remove otp
//        user.setOtp(null);
//        userService.saveUser(user);
       // AppLoginTable loginData = userService.getAppLoginOtp(verifyOtp.getOtp());
        return login(loginUser);
    }
    
    @Override
    public User verifyOTPForAppUser(VerifyOtp verifyOtp) {
    	validateNumber(verifyOtp.getPhoneNumber());
        System.out.println("========otp======="+verifyOtp.getOtp());
        
        AppLoginTable loginData = userService.getAppLoginOtp(verifyOtp.getOtp());
        User user = userService.getUserByPhoneNum(verifyOtp.getPhoneNumber());

        if (loginData == null) {
            logger.warn("===otp not found for userId===", user.getId());
            throw new CustomException("OTP does not match");
        }
        
        OTP mOTP = loginData.getOtp();
        String userOtp = mOTP.getOtp();
        if (!userOtp.equals(verifyOtp.getOtp())) {
            throw new CustomException("OTP doesn't match");
        }

        Date expiryTime;
        expiryTime = loginData.getOtp().getExpiryTime();

        Date nowDate = new Date();
        if (nowDate.after(expiryTime)) {
            throw new CustomException("OTP expired");
        }
        LoginUser loginUser = new LoginUser();
        
        if(user!=null) {
        	user.setRegistered(true);
        	loginUser.setUsername(user.getPrimaryPhone());
            loginUser.setPassword(user.getPrimaryPhone());
           // user = login(loginUser);
        }
        
        if(user==null){
        	user = new User();
        	user.setRegistered(false);
        }
                
        return user;
    }
    
    @Override
    public User verifyRegistrationOTP(VerifyOtp verifyOtp) {
        validateNumber(verifyOtp.getPhoneNumber());
        System.out.println("========otp======="+verifyOtp.getOtp());

        User user = userService.getUserByPhoneNum(verifyOtp.getPhoneNumber());
        if (user == null) {
            throw new ResourceNotFoundException("User not Exist, Please ask you Manager for SignUp");
        }

        OTP mOTP = user.getOtp();
        String userOtp = mOTP.getOtp();
        if (!userOtp.equals(verifyOtp.getOtp())) {
            throw new CustomException("OTP doesn't match");
        }

        Date expiryTime;
        if (user.getOtp() == null) {
            logger.warn("===otp not found for userId===", user.getId());
            throw new CustomException("OTP does not match");
        }
        expiryTime = user.getOtp().getExpiryTime();

        Date nowDate = new Date();
        if (nowDate.after(expiryTime)) {
            throw new CustomException("OTP expired");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(user.getPrimaryPhone());

        // tODO: need to check this
        loginUser.setPassword(user.getPrimaryPhone());

        // TODO: remove otp
//        user.setOtp(null);
//        userService.saveUser(user);
        return login(loginUser);
    }

    @Override
    public User signUp(User user) throws Exception {
        validateNumber(user.getPrimaryPhone());

        OTP mOTP = generateOtp();

        System.out.println("========otp======="+mOTP.getOtp());
        User userExist = userService.getUserByPhoneNum(user.getPrimaryPhone());
        if (userExist == null) {
            throw new ResourceNotFoundException("User not Exist, Please ask you Manager for SignUp");
        }

        // Signup
        List<Role> roleList = new ArrayList<>();
        Role role = roleService.roleName(ROLE_USER);
        roleList.add(role);

        //If User already exist as Family member then add new user role and update existing user
        if(userExist.getRoles().contains(role)) {
            throw new ResourceAlreadyExists("User already exists");
        }
        addUser(user, mOTP, roleList,ROLE_USER);

        // update count for farmers
        CountAll countAll = countService.getCountAll();
        countAll.setTotalFarmers(countService.getTotalFarmerCount());
        countAll.setTotalUsers(countService.getTotalUserCount());
        countAll.setTotalFemaleFarmers(countService.getTotalFemaleFarmerCount());
        countAll.setTotalMaleFarmers(countService.getTotalMaleFarmerCount());
        countService.storeCountAll(countAll);

        Msg91Services.sendMsg(mOTP.getOtp(), user.getPrimaryPhone());
        return user;
    }

    @Deprecated
    @Override
    public User signUpForAgent(User user) throws Exception {
        validateNumber(user.getPrimaryPhone());
        User userExist = userService.getUserByPhoneNum(user.getPrimaryPhone());
        if (userExist != null) {
            throw new ResourceAlreadyExists("User already exists");
        }
        OTP mOTP = generateOtp();
        System.out.println("========otp======="+mOTP.getOtp());
        Role role = roleService.roleName(ROLE_AGENT);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        addUser(user, mOTP, roleList,ROLE_AGENT);

        // update count for Agents
        CountAll countAll = countService.getCountAll();
        countAll.setTotalActiveAgents(countService.getTotalAgentCount(true));
        countAll.setTotalAgents(countService.getTotalAgentCount(false));
        countAll.setTotalUsers(countService.getTotalUserCount());
        countService.storeCountAll(countAll);

        String message = "Your Username for "+APP_NAME+" Agent App is:\n" +
                "username = " + user.getPrimaryPhone() + "\n" + agentAppLink;
        Msg91Services.sendFarmerAppLinkMsg(message, user.getPrimaryPhone());
        return user;
    }

    @Override
    public User createManagerAgent(UserReqBody userReqBody) throws Exception {
        validateNumber(userReqBody.getPrimaryPhone());
        User userExist = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone().trim());

        if (userExist != null) {
            throw new ResourceAlreadyExists("User already exists");
        }

        OTP mOTP = generateOtp();

        String message = "Your Username for "+APP_NAME+" Agent App is:\n" +
                "username = " + userReqBody.getPrimaryPhone() + "\n" + agentAppLink;

        List<Role> roleList = new ArrayList<>();
        if(!userReqBody.getRoleName().equals(ROLE_ACCOUNTANT)) {
            Role agentRole = roleService.roleName(ROLE_AGENT);
            roleList.add(agentRole);
        }
        // If user is any type of Manager
        if (!StringUtils.isEmpty(userReqBody.getRoleName()) && !userReqBody.getRoleName().equals(ROLE_AGENT) && !userReqBody.getRoleName().equals(ROLE_USER)) {
            Role role = roleService.roleName(userReqBody.getRoleName());
            roleList.add(role);
            message = "Welcome to "+APP_NAME+"\n\n"+
                    APP_NAME+" App Link: "+agentAppLink+"\n"+
                    APP_NAME+"Admin Panel Link: "+ adminPanelLink;
        }

        // create new user
        User user = new User();
        user.setFirstName(userReqBody.getFirstName());
        user.setMiddleName(userReqBody.getMiddleName());
        user.setLastName(userReqBody.getLastName());
        user.setFatherName(userReqBody.getFatherName());
        user.setPrimaryPhone(userReqBody.getPrimaryPhone());
        user.setGender(userReqBody.getGender());
        user.setEmail(userReqBody.getEmail());
        user.setDateOfBirth(userReqBody.getDateOfBirth());
        user.setAddressModel(userReqBody.getAddressModel());


//       added from admin-panel by admin
        if(StringUtils.isEmpty(userReqBody.getCreatedByUserId())) {
            User admin = userService.getUserByPhoneNum("1212121212");
            user.setCreatedByUserId(admin.getId());
        } else {
//            added from admin-panel by others
            user.setCreatedByUserId(userReqBody.getCreatedByUserId());
        }

        // Set Manager Role
        addUser(user, mOTP, roleList,userReqBody.getRoleName());

        //count set according to Manager type
        CountAll countAll = countService.getCountAll();
        if(userReqBody.getRoleName().equals(ROLE_NATIONAL_MANAGER)){
            countAll.setTotalNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,false));
            countAll.setTotalActiveNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,true));
        } else if(userReqBody.getRoleName().equals(ROLE_STATE_MANAGER)){
            countAll.setTotalActiveStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,true));
            countAll.setTotalStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_DISTRICT_MANAGER)){
            countAll.setTotalActiveDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,true));
            countAll.setTotalDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_AGENT_MANAGER)){
            countAll.setTotalActiveAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,true));
            countAll.setTotalAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_AGENT)){
            countAll.setTotalActiveAgents(countService.getTotalAgentCount(true));
            countAll.setTotalAgents(countService.getTotalAgentCount(false));
        }
        countAll.setTotalUsers(countService.getTotalUserCount());
        countService.storeCountAll(countAll);

        //Set hasUser in User
        User agent = userService.findByid(user.getCreatedByUserId());
        if (!agent.isHasUser()) {
            agent.setHasUser(true);
            userService.saveUser(agent);
        }

        if(!userReqBody.getRoleName().equals(ROLE_ACCOUNTANT)) {
            FinancialDetails financialDetail = new FinancialDetails();
            financialDetail.setUserId(user.getId());
            financialDetail.setBankName(userReqBody.getBankName());
            financialDetail.setAccountHolderName(userReqBody.getAccountHolderName());
            financialDetail.setIfscCode(userReqBody.getIfscCode());
            financialDetail.setAccountNumber(userReqBody.getBankAccountNumber());
            financialDetail.setAadhaarCardNumber(userReqBody.getAadhaarCardNumber());
            financialDetail.setAadharCardImage(userReqBody.getAadharCardImage());
            financialDetail.setBankPassBookImage(userReqBody.getBankPassBookImage());
            financialDetail.setPrimaryAccount(true);
            userBankService.addFinancialDetails(financialDetail);
        }
     //   Msg91Services.sendFarmerAppLinkMsg(message, user.getPrimaryPhone());
        return user;
    }
    
    @Override
    public User createFarmer(UserReqBody userReqBody) throws Exception {
        validateNumber(userReqBody.getPrimaryPhone());
        User userExist = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone().trim());

        if (userExist != null) {
        	throw new ResourceAlreadyExists("User already exist with this Phone Number : ");
        }
        
        RegistrationOtp otp = userService.getRegisteredOtp(userReqBody.getPrimaryPhone().trim(),userReqBody.getOtp());
        if(otp==null) {
        	throw new ResourceAlreadyExists("OTP does not match : ");
        }
        OTP mOTP = generateOtp();

        String message = "Your Username for "+APP_NAME+" Agent App is:\n" +
                "username = " + userReqBody.getPrimaryPhone() + "\n" + agentAppLink;

        List<Role> roleList = new ArrayList<>();
        Role farmer = roleService.roleName("USER");
        roleList.add(farmer);
		/*	MODIFIED BY SONU KUMAR SINGH ON 29-07-2020
		 * if(!userReqBody.getRoleName().equals(ROLE_ACCOUNTANT)) { Role agentRole =
		 * roleService.roleName(ROLE_AGENT); roleList.add(agentRole); } // If user is
		 * any type of Manager if (!StringUtils.isEmpty(userReqBody.getRoleName()) &&
		 * !userReqBody.getRoleName().equals(ROLE_AGENT) &&
		 * !userReqBody.getRoleName().equals(ROLE_USER)) { Role role =
		 * roleService.roleName(userReqBody.getRoleName()); roleList.add(role); message
		 * = "Welcome to "+APP_NAME+"\n\n"+ APP_NAME+" App Link: "+agentAppLink+"\n"+
		 * APP_NAME+"Admin Panel Link: "+ adminPanelLink; }
		 */

        // create new user
        User user = new User();
        user.setFirstName(userReqBody.getFirstName());
        user.setMiddleName(userReqBody.getMiddleName());
        user.setLastName(userReqBody.getLastName());
        user.setFatherName(userReqBody.getFatherName());
        user.setPrimaryPhone(userReqBody.getPrimaryPhone());
        user.setGender(userReqBody.getGender());
        user.setEmail(userReqBody.getEmail());
        user.setDateOfBirth(userReqBody.getDateOfBirth());
        user.setAddressModel(userReqBody.getAddressModel());
        user.setPassword(userReqBody.getPassword());
        user.setDesc(userReqBody.getPassword());

//       added from admin-panel by admin
        if(StringUtils.isEmpty(userReqBody.getCreatedByUserId())) {
            User admin = userService.getUserByPhoneNum("1212121212");
            user.setCreatedByUserId(admin.getId());
        } else {
//            added from admin-panel by others
            user.setCreatedByUserId(userReqBody.getCreatedByUserId());
        }

        // Set Manager Role
    //    addUser(user, mOTP, roleList,userReqBody.getRoleName());
        addFarmerAsUser(user, mOTP, roleList,userReqBody.getRoleName());
        //count set according to Manager type
        CountAll countAll = countService.getCountAll();
        if(userReqBody.getRoleName().equals(ROLE_NATIONAL_MANAGER)){
            countAll.setTotalNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,false));
            countAll.setTotalActiveNationalManagers(countService.getUserTypeCount(ROLE_NATIONAL_MANAGER,true));
        } else if(userReqBody.getRoleName().equals(ROLE_STATE_MANAGER)){
            countAll.setTotalActiveStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,true));
            countAll.setTotalStateManagers(countService.getUserTypeCount(ROLE_STATE_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_DISTRICT_MANAGER)){
            countAll.setTotalActiveDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,true));
            countAll.setTotalDistrictManagers(countService.getUserTypeCount(ROLE_DISTRICT_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_AGENT_MANAGER)){
            countAll.setTotalActiveAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,true));
            countAll.setTotalAgentManagers(countService.getUserTypeCount(ROLE_AGENT_MANAGER,false));
        } else if(userReqBody.getRoleName().equals(ROLE_AGENT)){
            countAll.setTotalActiveAgents(countService.getTotalAgentCount(true));
            countAll.setTotalAgents(countService.getTotalAgentCount(false));
        }
        countAll.setTotalUsers(countService.getTotalUserCount());
        countService.storeCountAll(countAll);

        //Set hasUser in User
        /*    User agent = userService.findByid(user.getCreatedByUserId()); COMMENTED BY SONU SINGH ON 29-07-2020
	
	 * if (!agent.isHasUser()) { agent.setHasUser(true);
	 * userService.saveUser(agent); }
	 */
        if(!userReqBody.getRoleName().equals(ROLE_ACCOUNTANT)) {
            FinancialDetails financialDetail = new FinancialDetails();
            financialDetail.setUserId(user.getId());
            financialDetail.setBankName(userReqBody.getBankName());
            financialDetail.setAccountHolderName(userReqBody.getAccountHolderName());
            financialDetail.setIfscCode(userReqBody.getIfscCode());
            financialDetail.setAccountNumber(userReqBody.getBankAccountNumber());
            financialDetail.setAadhaarCardNumber(userReqBody.getAadhaarCardNumber());
            financialDetail.setAadharCardImage(userReqBody.getAadharCardImage());
            financialDetail.setBankPassBookImage(userReqBody.getBankPassBookImage());
            financialDetail.setPrimaryAccount(true);
            userBankService.addFinancialDetails(financialDetail);
        }
     //   Msg91Services.sendFarmerAppLinkMsg(message, user.getPrimaryPhone());
        return user;
    }
    
    @Override
    public User createFarmerFromApp(UserReqBodyForAppUser userReqBody) throws Exception {
        
        User userExist = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone().trim());
        if (userExist != null) {
        	throw new ResourceAlreadyExists("User already exist with this Phone Number : ");
        }
        List<Role> roleList = new ArrayList<>();
        Role farmer = roleService.roleName("USER");
        roleList.add(farmer);

        // create new user
        User user = new User();
        Address addressModel = new Address();
        addressModel.setState(userReqBody.getAddressModel().getState());
        addressModel.setCity(userReqBody.getAddressModel().getCity());
        addressModel.setTehsil(userReqBody.getAddressModel().getTehsil());
        addressModel.setBlock(userReqBody.getAddressModel().getBlock());
        addressModel.setVillageModel(userReqBody.getAddressModel().getVillageModel());
        user.setAddressModel(addressModel);
        
        AddressInHindi addressModeliNHindi = new AddressInHindi();
        addressModeliNHindi.setState(userReqBody.getAddressModelInHindi().getState());
        addressModeliNHindi.setCity(userReqBody.getAddressModelInHindi().getCity());
        addressModeliNHindi.setTehsil(userReqBody.getAddressModelInHindi().getTehsil());
        addressModeliNHindi.setBlock(userReqBody.getAddressModelInHindi().getBlock());
        addressModeliNHindi.setVillageModel(userReqBody.getAddressModelInHindi().getVillageModel());
        user.setAddressModelInHindi(addressModeliNHindi);
        
        user.setPrimaryPhone(userReqBody.getPrimaryPhone());
        user.setKhasraNumber(userReqBody.getKhasraNumber());
        user.setNoOfChildren(userReqBody.getNoOfChildren());
        user.setNoOfFeMale(userReqBody.getNoOfFeMale());
        user.setNoOfMale(userReqBody.getNoOfMale());
        User admin = userService.getUserByPhoneNum("1212121212");
        user.setCreatedByUserId(admin.getId());
        
        User savedUser=addFarmerFromAppAsUser(user,roleList);
        List<UserKhasraMapping> userKhasraMappingList=userReqBody.getUserKhasraMappingList();
        for(UserKhasraMapping mapping:userKhasraMappingList) {
        	mapping.setUserId(savedUser.getId());
        	landService.storeUserKhasraMapping(mapping);
        }
        return user;
    }
    
    @Override
    public User updateFarmer(UserReqBody userReqBody) throws Exception {
        validateNumber(userReqBody.getPrimaryPhone());
        User user = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone().trim());
        user.setFirstName(userReqBody.getFirstName());
        user.setMiddleName(userReqBody.getMiddleName());
        user.setLastName(userReqBody.getLastName());
        user.setFatherName(userReqBody.getFatherName());
  //    user.setPrimaryPhone(userReqBody.getPrimaryPhone());
        user.setGender(userReqBody.getGender());
        user.setEmail(userReqBody.getEmail());
        user.setDateOfBirth(userReqBody.getDateOfBirth());
        user.setAddressModel(userReqBody.getAddressModel());
        user.setAadhaarCardNumber(userReqBody.getAadhaarCardNumber());
        userService.saveUser(user);
        return user;
    }
    
    @Override
    public User resetPassword(UserReqBody userReqBody) throws Exception {
        validateNumber(userReqBody.getPrimaryPhone());
        User user = userService.getUserByPhoneNum(userReqBody.getPrimaryPhone().trim());
        
        RegistrationOtp otp = userService.getRegisteredOtp(userReqBody.getPrimaryPhone().trim(),userReqBody.getOtp());
        if(otp==null) {
        	throw new ResourceAlreadyExists("OTP does not match : ");
        }else {
        	user.setPassword(userReqBody.getPassword());
        	userService.updateFarmerPassword(user);
        }
        return user;
    }

    @Override
    public String sendMoblieOTP(String phoneNumber) throws Exception {

        //check phone number valid or not
        validateNumber(phoneNumber);

        //generate otp
        OTP mOTP = generateOtp();

        System.out.println("========otp============="+mOTP.getOtp());

        //check user exists or not.
        //if the user exists than store otp otherwise a new user will be created
        User user = userService.getUserByPhoneNum(phoneNumber);
        List<Role> roleList = new ArrayList<>();
        Role role = roleService.roleName(ROLE_MARKET_USER);

        if(user == null){
            user = new User();
            //set values in user table i.e motp, createdVia : market_app , phone number, password, usercode and roleList.
            user.setOtp(mOTP);
            user.setCreatedVia(MARKET_APP);
            user.setPrimaryPhone(phoneNumber.trim());
            user.setActive(true);

            //generate Unique userCode
            user.setUserCode(generateUniqueCodeForUser());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(phoneNumber));
            roleList.add(role);
            user.setRoles(roleList);
            userService.saveUser(user);
        } else{
            // Store OTP
            user.setOtp(mOTP);
            List<Role> userRoleList = user.getRoles();

            // check market role exist or not
            if (!userRoleList.contains(role)) {
                user.addRole(role);
            }
            userService.saveUser(user);
        }

        // Send OTP
        Msg91Services.sendMsg(mOTP.getOtp(), phoneNumber);
        return "success";
    }

    private void validateNumber(@RequestParam String phoneNumber) {
        if (phoneNumber.length() < 10) {
            throw new CustomException("Enter a Valid Phone Number.");
        }
    }

    private void addUser(@RequestBody User user, OTP mOTP, List<Role> roleList, String userPrimaryRoleName) {
        user.setRoles(roleList);
        user.setPrimaryPhone(user.getPrimaryPhone().trim());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));
        user.setActive(true);
        user.setCreationDate(Calendar.getInstance().getTime());
        user.setOtp(mOTP);
//        generate Unique userCode
        user.setUserCode(generateUniqueCodeForUser());
        userService.saveUser(user);

        EmployeeHistory employeeHistory = new EmployeeHistory();
        employeeHistory.setUser(user);
        employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
        employeeHistory.setCreatedByUserId(user.getCreatedByUserId());
        employeeHistoryService.storeEmployeeHistory(employeeHistory);

        // add EmployeeAssignmentHistory
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);

        User fromUser = userService.findByid(user.getCreatedByUserId());

        EmployeeAssignmentHistory employeeAssignmentHistory = new EmployeeAssignmentHistory();
        employeeAssignmentHistory.setToUser(user);
        employeeAssignmentHistory.setFromUser(fromUser);

        employeeAssignmentHistory.setEmplyeeRelationship(getEmployeeRelationshipByToUserType(userPrimaryRoleName, fromUser,nationalManager,stateManager,districtManager,blockManager));
        employeeHistoryService.storeEmployeeAssignmentHistory(employeeAssignmentHistory);
    }
    
    private void addFarmerAsUser(@RequestBody User user, OTP mOTP, List<Role> roleList, String userPrimaryRoleName) {
    	user.setRoles(roleList);
        user.setPrimaryPhone(user.getPrimaryPhone().trim());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
     //   user.setPassword(bCryptPasswordEncoder.encode(user.getPrimaryPhone()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreationDate(Calendar.getInstance().getTime());
        user.setOtp(mOTP);
//        generate Unique userCode
        user.setUserCode(generateUniqueCodeForUser());
        userService.saveUser(user);

        EmployeeHistory employeeHistory = new EmployeeHistory();
        employeeHistory.setUser(user);
        employeeHistory.setStatus(EmployeeHistory.Status.ACTIVE);
        employeeHistory.setCreatedByUserId(user.getCreatedByUserId());
        employeeHistoryService.storeEmployeeHistory(employeeHistory);

        // add EmployeeAssignmentHistory
        Role nationalManager = roleService.roleName(ROLE_NATIONAL_MANAGER);
        Role stateManager = roleService.roleName(ROLE_STATE_MANAGER);
        Role districtManager = roleService.roleName(ROLE_DISTRICT_MANAGER);
        Role blockManager = roleService.roleName(ROLE_AGENT_MANAGER);

		/*COMMENTED BY SONU SINGH ON 29-07-2020
		 * User fromUser = userService.findByid(user.getCreatedByUserId());
		 * 
		 * EmployeeAssignmentHistory employeeAssignmentHistory = new
		 * EmployeeAssignmentHistory(); employeeAssignmentHistory.setToUser(user);
		 * employeeAssignmentHistory.setFromUser(fromUser);
		 * 
		 * employeeAssignmentHistory.setEmplyeeRelationship(
		 * getEmployeeRelationshipByToUserType(userPrimaryRoleName,
		 * fromUser,nationalManager,stateManager,districtManager,blockManager));
		 * employeeHistoryService.storeEmployeeAssignmentHistory(
		 * employeeAssignmentHistory);
		 */
    }
    
    private User addFarmerFromAppAsUser(@RequestBody User user,List<Role> roleList) {
    	user.setRoles(roleList);
        user.setActive(true);
        user.setCreationDate(Calendar.getInstance().getTime());
        user.setUserCode(generateUniqueCodeForUser());
        return userService.saveUser(user);
    }


    @Override
    public String sendOTPFarmerVerification(FarmerVerificationReqBody farmerVerificationReqBody) throws Exception {
        //check phone number valid or not
        String phoneNumber = farmerVerificationReqBody.getPhoneNumber();
        validateNumber(phoneNumber);

        if(farmerVerificationReqBody.isPhoneNumberUpdated()){
            User user = userService.getUserByPhoneNum(farmerVerificationReqBody.getPhoneNumber());
            if(user != null){
                String userMainRole = getUserMainRole(user);
                throw new ResourceAlreadyExists("User already exist with Phone Number : "+phoneNumber+" and role : "+userMainRole);
            }
        }


        //generate otp
        OTP mOTP = checkOTPByNumber(phoneNumber);
        if(mOTP == null){
            mOTP = generateOtp();
        } else {
            Random random = new Random();
            String otp = String.format("%04d", random.nextInt(10000));

            final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (5 * ONE_MINUTE_IN_MILLIS));

            mOTP.setOtp(otp);
            mOTP.setExpiryTime(afterAddingTenMins);
        }
        mOTP.setPhoneNumber(phoneNumber);
        mongoTemplate.save(mOTP);

        // Send OTP
        System.out.println("====="+mOTP.getOtp());
        Msg91Services.sendMsg(mOTP.getOtp(), phoneNumber);
        return "success";
    }

    @Override
    public String verifyOTPFarmerVerification(VerifyOtp verifyOtp) {
        validateNumber(verifyOtp.getPhoneNumber());

        OTP farmerOTP = checkOTPByNumber(verifyOtp.getPhoneNumber());
        if(farmerOTP == null) {
            logger.warn("===otp not found=====");
            throw new CustomException("OTP does match");
        }

        if (!farmerOTP.getOtp().equals(verifyOtp.getOtp())) {
            throw new CustomException("OTP doesn't match");
        }

        Date expiryTime;
        expiryTime = farmerOTP.getExpiryTime();
        Date nowDate = new Date();
        if (nowDate.after(expiryTime)) {
            throw new CustomException("OTP expired");
        }
        return "Success";
    }
    
    

    private OTP checkOTPByNumber(String phoneNumber) {
        return genericMongoTemplate.findByKey("phoneNumber",phoneNumber,OTP.class);
    }

}
