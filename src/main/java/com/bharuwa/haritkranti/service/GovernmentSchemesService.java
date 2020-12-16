package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.GovernmentSchemes;
import com.bharuwa.haritkranti.models.requestModels.GovernmentSchemeStatus;
import com.bharuwa.haritkranti.models.responseModels.UserAdoptedSchemeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author harman
 */
public interface GovernmentSchemesService  {

    GovernmentSchemes storeGovernmentScheme(GovernmentSchemes scheme);

    List<UserAdoptedSchemeResponse> getGovernmentSchemesList(String userId);

    Page<GovernmentSchemes> getAllGovernmentSchemesList(int page, int size);

    String setGovernmentSchemeStatus(GovernmentSchemeStatus governmentSchemeStatus);

    List<GovernmentSchemes> searchGovernmentSchemesByName(String searchText);

    GovernmentSchemes findGovernmentSchemeByName(String name);
}
