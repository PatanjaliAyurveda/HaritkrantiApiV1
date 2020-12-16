package com.bharuwa.haritkranti.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/api/adminLogin").permitAll()
                .antMatchers("/api/registerFarmer").permitAll()
                .antMatchers("/api/getFarmerData").permitAll()
                .antMatchers("/api/getAssignLocsOfManager").permitAll()
                .antMatchers("/api/farmerLogin").permitAll()
                .antMatchers("/api/getWeatherData").permitAll()
                .antMatchers("/api/getWeatherForecastData").permitAll()
                .antMatchers("/api/getMandiRate").permitAll()
                .antMatchers("/api/getMandiRateData").permitAll()
                .antMatchers("/api/updateCropName").permitAll()
                .antMatchers("/api/getCommodityList").permitAll()
                .antMatchers("/api/getMarketList").permitAll()
                .antMatchers("/api/getFilteredMarketRateData").permitAll()
                .antMatchers("/api/getFilteredMandiRateData").permitAll()
                .antMatchers("/api/getVarietyList").permitAll()
                .antMatchers("/api/getCropYield").permitAll()
                .antMatchers("/api/addFarmCoordinate").permitAll()
                .antMatchers("/api/getKhasraList").permitAll()
                .antMatchers("/api/addKhasra").permitAll()
                .antMatchers("/api/getWeatherAlert").permitAll()
                .antMatchers("/api/getUserReportByKhsraNoAndCropId").permitAll()
                .antMatchers("/api/getGeoLocationMappingByType").permitAll()
                .antMatchers("/api/getMoveableEquipments").permitAll()
                .antMatchers("/api/getImmoveableEquipments").permitAll()
                .antMatchers("/api/getUserEquipments").permitAll()
                .antMatchers("/api/getGovtSchemes").permitAll()
                .antMatchers("/api/getWeatherForecastInHindi").permitAll()
                .antMatchers("/api/fetchCropNameFromCropYield").permitAll()
                .antMatchers("/api/storeCropDetail").permitAll()
                .antMatchers("/api/updateAlreadySownCropDetail").permitAll()
                .antMatchers("/api/updateCropDetail").permitAll()
                .antMatchers("/api/getfarmCordinate").permitAll()
                .antMatchers("/api/getMandiRateDataByLatLong").permitAll()
                .antMatchers("/api/getWeatherForecastInEnglish").permitAll()
                .antMatchers("/api/createEmployeeTest").permitAll()
                .antMatchers("/api/getFarmCordinate").permitAll()
                .antMatchers("/api/getCropYieldTehsilList").permitAll()
                .antMatchers("/api/getCropYieldBlockList").permitAll()
                .antMatchers("/api/getCropYieldVillageList").permitAll()
                .antMatchers("/api/getFertilizerCenterList").permitAll()
                .antMatchers("/api/storeFertCalcInEnglish").permitAll()
                .antMatchers("/api/getCropYieldList").permitAll()
                .antMatchers("/api/getCropYieldKhasraList").permitAll()
                .antMatchers("/api/getNewAndFeeds").permitAll()
                .antMatchers("/api/getSeedCenterList").permitAll()
                .antMatchers("/api/getAlertList").permitAll()
                .antMatchers("/api/getContactUsList").permitAll()
                .antMatchers("/api/getFamilyMembersNew").permitAll()
                .antMatchers("/api/getFarmerExtraDetail").permitAll()
                .antMatchers("/api/getOrganicCertificateList").permitAll()
                .antMatchers("/api/addFamilyMemberNew").permitAll()
                .antMatchers("/api/addUserLandDetail").permitAll()
                .antMatchers("/api/getFarmerAllLandDetail").permitAll()
                .antMatchers("/api/getSoils").permitAll()
                .antMatchers("/api/getStates").permitAll()
                .antMatchers("/api/getStateList").permitAll()
                .antMatchers("/api/getBlockListByTehsil").permitAll()
                .antMatchers("/api/getVillageListByBlockId").permitAll()
                .antMatchers("/api/getUserReports").permitAll()
                .antMatchers("/api/getReportPdf").permitAll()
                .antMatchers("/api/getAllKhasraCropByFarmerId").permitAll()
                .antMatchers("/api/getCropGroupsByType").permitAll()
                .antMatchers("/api/getCityListByStateId").permitAll()
                .antMatchers("/api/getTehsilListByDistrict").permitAll()
                .antMatchers("/api/getCropList").permitAll()
                .antMatchers("/api/updateFarmerName").permitAll()
                .antMatchers("/api/updateFarmerStatus").permitAll()
                .antMatchers("/api/getCropByType").permitAll()
                .antMatchers("/api/uploadFarmerImage").permitAll()
                .antMatchers("/api/registerFarmerFromApp").permitAll()
                .antMatchers("/api/getTehsilsByDistrict").permitAll()
                .antMatchers("/api/getBlocksByTehsil").permitAll()
                .antMatchers("/api/getVillagesByBlockId").permitAll()
                .antMatchers("/api/getCropVarietyList").permitAll()
                .antMatchers("/api/getGovtLandDetail").permitAll()
                .antMatchers("/api/getCropSelectionList").permitAll()
                .antMatchers("/api/verifyOTP").permitAll()
                .antMatchers("/api/addCropYieldData").permitAll()
                .antMatchers("/api/addAnndataCropCategory").permitAll()
                .antMatchers("/api/sendMobileOTP").permitAll()
                .antMatchers("/api/sendOTP").permitAll()
                .antMatchers("/api/getAllCategories").permitAll()
                .antMatchers("/api/getSubCategories").permitAll()
                .antMatchers("/api/getVarieties").permitAll()
                .antMatchers("/api/getEmployeeList").permitAll()
                .antMatchers("/api/getEmployee").permitAll()
                .antMatchers("/api/getCropPesticides").permitAll()
                .antMatchers("/api/getCropDisease").permitAll()
                .antMatchers("/api/getCropWeed").permitAll()
                .antMatchers("/api/getCropSeed").permitAll()
                .antMatchers("/api/getFertilizerCal").permitAll()
                .antMatchers("/api/sendRegistrationOTP").permitAll()
                .antMatchers("/api/sendResetPasswordOTP").permitAll()
                .antMatchers("/api/getUserByPhoneNumber").permitAll() // can access user by phone number for annadata
                .antMatchers("/api/getOCQRResponse").permitAll() // QRCODE,
                .antMatchers("/api/getReportPdf").permitAll()   //  PDF
                .antMatchers("/api/generateQRCodeForBeekeeping").permitAll()   //  BKQR
                .antMatchers("/api/getBeekeepingQRCodeResponse").permitAll()   //  BKQRCODE RESPONSE
                .antMatchers("/api/generateCertificate").permitAll()   //  BKQRCODE RESPONSE
                .antMatchers("/api/getUserCropListReadyForSale").permitAll()   //  BKQRCODE RESPONSE
                .antMatchers("/api/getApp").permitAll()   //  BKQRCODE RESPONSE
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
