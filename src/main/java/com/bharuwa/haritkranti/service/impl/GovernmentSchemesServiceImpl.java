package com.bharuwa.haritkranti.service.impl;

import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.GovernmentSchemes;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.UserSchemes;
import com.bharuwa.haritkranti.models.requestModels.GovernmentSchemeStatus;
import com.bharuwa.haritkranti.models.responseModels.UserAdoptedSchemeResponse;
import com.bharuwa.haritkranti.repositories.GovernmentSchemesRepo;
import com.bharuwa.haritkranti.service.GovernmentSchemesService;
import com.bharuwa.haritkranti.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harman
 */
@Service
public class GovernmentSchemesServiceImpl implements GovernmentSchemesService {

    @Autowired
    GovernmentSchemesRepo governmentSchemesRepo;

    @Autowired
    UserService userService;

    private final MongoTemplate mongoTemplate;

    public GovernmentSchemesServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public GovernmentSchemes storeGovernmentScheme(GovernmentSchemes scheme) {
        Query query = new Query();
        query.addCriteria(Criteria.where("schemeName").is(scheme.getSchemeName()).and("schemeCode").is(scheme.getSchemeCode()).and("schemeSponser").is(scheme.getSchemeSponser()));
        GovernmentSchemes value = mongoTemplate.findOne(query, GovernmentSchemes.class);

        System.out.println("value=========="+value);
        if (value == null) {
            scheme.setActive(true);
            governmentSchemesRepo.save(scheme);
        } else {
            value.setSchemeName(scheme.getSchemeName());
            value.setSchemeCode(scheme.getSchemeCode());
            value.setSchemeSponser(scheme.getSchemeSponser());
            value.setStates(scheme.getStates());
            value.setCentral(scheme.isCentral());
            value.setActive(scheme.isActive());
            governmentSchemesRepo.save(value);
        }
        return scheme;
    }

    @Override
    public List<UserAdoptedSchemeResponse> getGovernmentSchemesList(String userId) {
        User user = userService.findByid(userId);
        if(user == null){
            throw new ResourceNotFoundException("Invalid UserId");
        }

        Query query = new Query(new Criteria().orOperator(Criteria.where("states.$id").is(new ObjectId(user.getAddressModel().getState().getId())).and("deleted").ne(true),Criteria.where("isCentral").ne(false)));

        List<GovernmentSchemes> governmentSchemesList = mongoTemplate.find(query, GovernmentSchemes.class);

        List<UserSchemes> userAdoptedSchemes = userService.getUserSchemesList(userId);

        List<String> userAdoptedGovernmentSchemes = new ArrayList<>();

        for(UserSchemes userSchemes : userAdoptedSchemes) {
            userAdoptedGovernmentSchemes.add(userSchemes.getGovernmentSchemes().getId());
        }

        List<UserAdoptedSchemeResponse> userAdoptedSchemeResponseList = new ArrayList<>();

        for(GovernmentSchemes governmentSchemes : governmentSchemesList){

            UserAdoptedSchemeResponse userAdoptedSchemeResponse = new UserAdoptedSchemeResponse();

            if (userAdoptedGovernmentSchemes.contains(governmentSchemes.getId())) {
                userAdoptedSchemeResponse.setGovernmentSchemes(governmentSchemes);
                userAdoptedSchemeResponse.setStatus(true);
                userAdoptedSchemeResponseList.add(userAdoptedSchemeResponse);
            } else {
                userAdoptedSchemeResponse.setGovernmentSchemes(governmentSchemes);
                userAdoptedSchemeResponse.setStatus(false);
                userAdoptedSchemeResponseList.add(userAdoptedSchemeResponse);
            }
        }
        return userAdoptedSchemeResponseList;
    }

    private GovernmentSchemes getGovernmentSchemeById(String schemeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(schemeId)));
        return mongoTemplate.findOne(query, GovernmentSchemes.class);
    }

    @Override
    public Page<GovernmentSchemes> getAllGovernmentSchemesList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query().with(pageable);
        List<GovernmentSchemes> governmentSchemesList = mongoTemplate.find(query, GovernmentSchemes.class);
        return PageableExecutionUtils.getPage(
                governmentSchemesList,
                pageable,
                () -> mongoTemplate.count(query, GovernmentSchemes.class));
    }

    @Override
    public String setGovernmentSchemeStatus(GovernmentSchemeStatus governmentSchemeStatus) {
        GovernmentSchemes scheme = getGovernmentSchemeById(governmentSchemeStatus.getSchemeId());
        if (scheme == null) {
            throw new ResourceNotFoundException("Scheme does not exists");
        }
        
        if(governmentSchemeStatus.isStatus()) {
            scheme.setActive(true);
            scheme.setDeleted(false);
        } else {
            scheme.setActive(false);
            scheme.setDeleted(true);
        }
        storeGovernmentScheme(scheme);
        return "Status Updated";
    }

    @Override
    public List<GovernmentSchemes> searchGovernmentSchemesByName(String searchText) {
        Query query = new Query(Criteria.where("schemeName").regex(searchText, "i"));
        return mongoTemplate.find(query,GovernmentSchemes.class);
    }

    @Override
    public GovernmentSchemes findGovernmentSchemeByName(String name) {
        Query query = new Query(Criteria.where("schemeName").is(name));
        return mongoTemplate.findOne(query, GovernmentSchemes.class);
    }
}
