package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.*;
import com.bharuwa.haritkranti.models.requestModels.GovernmentSchemeStatus;
import com.bharuwa.haritkranti.models.requestModels.UserStatus;
import com.bharuwa.haritkranti.models.responseModels.UserAdoptedSchemeResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.bharuwa.haritkranti.utils.Constants.TOKEN_PREFIX;

/**
 * @author anuragdhunna
 */


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {

    @RequestMapping(value = "/getProfile", method = RequestMethod.GET)
    public User getProfile(@RequestParam String id) {
        return userService.findByid(id);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public User updateProfile(@RequestBody User user) {
        return userService.saveUser(user);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/getAllUsers",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getAllUsers(@RequestParam(value = "page",defaultValue = "0")int page,
                                  @RequestParam(value = "size",defaultValue = "20")int size){
        return userService.getAllUsers(page,size);
    }

    @RequestMapping(value = "/getRelationships", method = RequestMethod.GET)
    public List<User.Relationship> getRelationships() {
        List<User.Relationship> relationships =Arrays.asList(User.Relationship.values());
//        relationships.remove(User.Relationship.GrandFather);
//        relationships.remove(User.Relationship.GrandMother);
//        relationships.remove(User.Relationship.GrandSon);
//        relationships.remove(User.Relationship.GrandDaughter);
//        relationships.remove(User.Relationship.Brother);
//        relationships.remove(User.Relationship.Sister);
        return relationships;
    }

    @RequestMapping(value = "/getAgentByUserId", method = RequestMethod.GET)
    public User getAgentByUserId(@RequestParam String userId) {
        return userService.getAgentByUserId(userId);
    }

    @RequestMapping(value = "/storeFarmerExtraDetail",method = RequestMethod.POST)
    public FarmerExtraDetails storeFarmerExtraDetail (@RequestBody FarmerExtraDetails farmerExtraDetails){
        return userService.storeFarmerExtraDetail(farmerExtraDetails);
    }

    @RequestMapping(value = "/getFarmerExtraDetail",method = RequestMethod.GET)
    public FarmerExtraDetails getFarmerExtraDetail (@RequestParam String farmerId){
        return userService.getFarmerExtraDetail(farmerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/setUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public String setUserStatus(@RequestBody UserStatus userStatus, @RequestHeader("Authorization") String authTokenHeader) {
        String loggedInUserName = jwtTokenUtil.getUsernameFromToken(authTokenHeader.replace(TOKEN_PREFIX, ""));
        return userService.setUserStatus(userStatus, loggedInUserName);
    }

    @RequestMapping(value = "/storeGovernmentSchemes",method = RequestMethod.POST)
    @ResponseBody
    public GovernmentSchemes storeGovernmentSchemes(@RequestBody GovernmentSchemes governmentSchemes){
        return governmentSchemesService.storeGovernmentScheme(governmentSchemes);
    }

    @RequestMapping(value = "/getAllGovernmentSchemesList",method = RequestMethod.GET)
    @ResponseBody
    public Page<GovernmentSchemes> getAllGovernmentSchemesList(@RequestParam(value = "page",defaultValue = "0")int page,
                                                               @RequestParam(value = "size",defaultValue = "20")int size){
        return governmentSchemesService.getAllGovernmentSchemesList(page,size);
    }

    @RequestMapping(value = "/getGovernmentSchemesList",method = RequestMethod.GET)
    @ResponseBody
    public List<UserAdoptedSchemeResponse> getGovernmentSchemesList(@RequestParam String userId){
        return governmentSchemesService.getGovernmentSchemesList(userId);
    }

    @RequestMapping(value = "/saveUserScheme",method = RequestMethod.POST)
    @ResponseBody
    public UserSchemes saveUserScheme(@RequestBody UserSchemes userSchemes){
        return userService.saveUserScheme(userSchemes);
    }

    @RequestMapping(value = "/getUserSchemesList",method = RequestMethod.GET)
    @ResponseBody
    public List<UserSchemes> getUserSchemesList(@RequestParam String userId){
        return userService.getUserSchemesList(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "setGovernmentSchemeStatus",method = RequestMethod.POST)
    @ResponseBody
    public String setGovernmentSchemeStatus(@RequestBody GovernmentSchemeStatus governmentSchemeStatus){
        return governmentSchemesService.setGovernmentSchemeStatus(governmentSchemeStatus);
    }

    @RequestMapping(value = "/saveUserCertificate",method = RequestMethod.POST)
    @ResponseBody
    public UserCertificate saveUserCertificate(@RequestBody UserCertificate userCertificate){
        return userService.saveUserCertificate(userCertificate);
    }

    @RequestMapping(value = "/getUserCertificate",method = RequestMethod.GET)
    @ResponseBody
    public UserCertificate getUserCertificate(@RequestParam String userId,@RequestParam String khasraNo){
        return userService.getUserCertificate(userId,khasraNo);
    }

    @RequestMapping(value = "/getUserByPhoneNumber", method = RequestMethod.GET)
    public User getUserByPhoneNumber(@RequestParam String phoneNumber) {
        return userService.getUserByPhoneNum(phoneNumber);
    }


    /**
     * search By Scheme Name
     * @param searchText
     * @return
     */
    @RequestMapping(value = "/searchGovernmentSchemesByName",method = RequestMethod.GET)
    public List<GovernmentSchemes> searchGovernmentSchemesByName(@RequestParam String searchText){
        return governmentSchemesService.searchGovernmentSchemesByName(searchText);
    }

    @RequestMapping(value = "/findGovernmentSchemeByName",method = RequestMethod.GET)
    @ResponseBody
    public GovernmentSchemes findGovernmentSchemeByName(@RequestParam String name){
        return governmentSchemesService.findGovernmentSchemeByName(name);
    }
}
