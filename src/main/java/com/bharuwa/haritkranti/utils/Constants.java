package com.bharuwa.haritkranti.utils;

import java.math.BigDecimal;

public class Constants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "argriculture_key";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";

    public static final String FARMER_APP_LINK = "https://play.google.com/store/apps/details?id=com.patanjali.annadatafarmer&hl=en_IN";

    // ROLES

    public static final String ROLE_USER = "USER";//DESC_ROLE_USER

    public static final String ROLE_ADMIN = "ADMIN";//DESC_ROLE_SUPER_ADMIN

    public static final String ROLE_AGENT = "AGENT";//DESC_ROLE_AGENT

    public static final String ROLE_MARKET_USER = "MARKET_USER";//DESC_ROLE_MARKET_USER

    public static final String ROLE_FAMILY_MEMBER = "FAMILY_MEMBER";//DESC_ROLE_FAMILY_MEMBER

//    public static final String ROLE__MANAGER = "MANAGER";//DESC_ROLE_MANAGER

    public static final String ROLE_STATE_MANAGER = "STATE_MANAGER";//DESC_ROLE_STATE_MANAGER

    public static final String ROLE_DISTRICT_MANAGER = "DISTRICT_MANAGER";//DESC_ROLE_DISTRICT_MANAGER

    public static final String ROLE_TEHSIL_MANAGER = "TEHSIL_MANAGER";//DESC_ROLE_TEHSIL_MANAGER

    public static final String ROLE_AGENT_MANAGER = "AGENT_MANAGER";//DESC_ROLE_AGENT_MANAGER

    public static final String ROLE_NATIONAL_MANAGER = "NATIONAL_MANAGER";//DESC_ROLE_NATIONAL_MANAGER

    public static final String ROLE_ACCOUNTANT = "ACCOUNTANT_MANAGER";//DESC_ROLE_ACCOUNTANT

    public static final String ROLE_ADMIN_VIEW = "ADMIN_VIEW"; //DESC_ROLE_ADMIN_VIEW

    static final String DESC_ROLE_USER = "Default role for all Users";

    static final String DESC_ROLE_ADMIN = "Administrator role full powers";

    static final String DESC_ROLE_AGENT = "Default role for all Agents";

    static final String DESC_ROLE_MARKET_USER = "Default role for all Market Users";

    static final String DESC_ROLE_FAMILY_MEMBER = "Default role for all Family Members";

//    static final String DESC_ROLE_MANAGER = "Default role for all Managers";

    static final String DESC_ROLE_STATE_MANAGER = "Default role for all State Managers";

    static final String DESC_ROLE_DISTRICT_MANAGER = "Default role for all District Managers";

    static final String DESC_ROLE_AGENT_MANAGER = "Default role for all Agent Managers";

    static final String DESC_ROLE_TEHSIL_MANAGER = "Default role for all Tehsil Managers";

    static final String DESC_ROLE_NATIONAL_MANAGER = "Default role for all National Managers";

    static final String DESC_ROLE_ACCOUNTANT = "Default role for all Accountant For Managing Payments";

    public static final String DESC_ROLE_ADMIN_VIEW = "Administrator role for view only";

    public static final String FINANCIAL_DETAILS = "FinancialDetails";
    public static final String INSURANCE_DETAILS = "InsuranceDetails";
    public static final String LOAN_DETAILS = "LoanDetails";
    public static final String FARM_DETAIL = "FarmDetails";

    public static final String HORTICULTURE = "Horticulture";
    public static final String PERSONAL_DETAILS = "PersonalDetails";
    public static final String FAMILY_DETAILS = "FamilyDetails";
    public static final String DAIRY_FARM_DETAIL = "DairyFarmDetails";
    public static final String CROP_EXPENSE_DETAIL = "CropExpenseDetails";
    public static final String BEEKEEPING_DETAIL = "BeekeepingDetails";
    public static final String GOVERNMENT_SCHEME = "GovernmentScheme";
    public static final String KHASRA_LOC_COORDINATES = "KhasraLocationCoordinates";
    public static final String SUBSIDY_DETAIL = "Subsidy";
    public static final String VILLAGE_ASSET = "VillageAssets";
    public static final String HEALTH_DETAIL = "HealthDetails";
    public static final String FARM_EQUIPMENT = "FarmEquipment";
    public static final String USER_ASSIGNMENT_HISTORY = "UserAssignment";
    public static final String USER_STATUS = "UserStatus";
    public static final String RECOMMENDATION_REPORT = "RecommendationReport";

    public static final String ADMIN_PANEl_LINK = "https://dhartikadoctor.co.in";
//    public static final String ADMIN_PANEl_LINK = "http://13.232.123.83:3000/";

    public static final String AGENT_APP_LINK = "https://play.google.com/store/apps/details?id=com.patanjali.dhartikadoctor";

    public static final String APP_NAME = "Dharti ka Doctor";

    public static final String CENTRAL = "Central";

    public static final String BK_QR_CODE_IMAGE_PATH = "./BKQRCode.png";

    public static final BigDecimal NEW_FARMER_KHASRA_RATE = new BigDecimal(35);
    public static final BigDecimal OLD_FARMER_KHASRA_RATE = new BigDecimal(25);
}