package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.aws.AwsS3;
import com.bharuwa.haritkranti.configuration.TokenProvider;
import com.bharuwa.haritkranti.controllers.newcontrollers.GovtSchemesController;
//import com.bharuwa.haritkranti.bidSystem.services.AuctionService;
//import com.bharuwa.haritkranti.bidSystem.services.BidService;
//import com.bharuwa.haritkranti.bidSystem.services.ProductService;
import com.bharuwa.haritkranti.service.*;
import com.bharuwa.haritkranti.service.newservice.AccountService;
import com.bharuwa.haritkranti.service.newservice.CropYieldService;
import com.bharuwa.haritkranti.service.newservice.EmployeeService;
import com.bharuwa.haritkranti.service.newservice.FertilizerCenterService;
import com.bharuwa.haritkranti.service.newservice.GovtSchemesService;
import com.bharuwa.haritkranti.service.newservice.MandiRateService;
import com.bharuwa.haritkranti.service.newservice.NewsAndFeedsService;
import com.bharuwa.haritkranti.service.newservice.SeedCenterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * @author anuragdhunna
 */
public class BaseController {

    @Autowired
    RoleService roleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenProvider jwtTokenUtil;

    @Autowired
    CropService cropService;

    @Autowired
    UserService userService;

    @Autowired
    LandService landService;

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    FarmingService farmingService;

    @Autowired
    CsvServices csvServices;

    @Autowired
    LocationServices locationServices;

    @Autowired
    ReportHistoryService reportHistoryService;

    @Autowired
    DealerService dealerService;

    @Autowired
    AgentService agentService;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    CountService countService;

    @Autowired
    GenerateReportService generateReportService;

    @Autowired
    GovernmentSchemesService governmentSchemesService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserExtraService userExtraService;

    @Autowired
    UserBankService userBankService;

    @Autowired
    OrganiseService organiseService;

    @Autowired
    FarmDetailService farmDetailService; 

    @Autowired
    CropAdvisoryService cropAdvisoryService;
    
    @Autowired
    public SeedCenterService seedCenterService;
    
    @Autowired
    public FertilizerCenterService fertilizerCenterService;
    
    @Autowired
    public CropYieldService cropYieldService;
    
    @Autowired
    public FarmerAppLocationService farmerAppLocationService;
    
    @Autowired
    public WeatherForcastDataIbmInHindiService weatherForcastDataIbmInHindiService;
    
    @Autowired
    public WeatherForcastDataIbmInEnglishService weatherForcastDataIbmInEnglishService;
    
   // @Autowired
   // public ProductService productService;

  //  @Autowired
  //  public BidService bidService;

  //  @Autowired
  //  public AuctionService auctionService;

    @Autowired
    public SalaryService salaryService;

    @Autowired
    public CommissionService commissionService;

    @Autowired
    public PaymentCycleService paymentCycleService;

    @Autowired
    public PaymentReportsService paymentReportsService;

    @Autowired
    public EmployeeHistoryService employeeHistoryService;

    @Autowired
    public RateService rateService;
    
    @Autowired
    public MandiRateService mandiRateService;
    
    @Autowired
    public GovtSchemesService govtSchemesService;
    
    @Autowired
    public NewsAndFeedsService newsAndFeedsService;
    
    @Autowired
    public ContactUsService contactUsService;
    
    @Autowired
    public AlertService alertService;
    
    @Autowired
    public EmployeeService employeeService;
    
    @Autowired
    public AccountService accountService;
    
    @Autowired
    public AwsS3 awsClientService;
    
    @Autowired
    public FarmerAppCropService farmerAppCropService;

}
