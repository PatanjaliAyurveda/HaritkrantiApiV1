package com.bharuwa.haritkranti.models.responseModels;

import java.util.List;

/**
 * @author harman
 */
public class OCQRCodeResponse {

    private UserResponse userResponse;

    private List<OCFertilizerResponse> ocFertilizerResponses;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public List<OCFertilizerResponse> getOcFertilizerResponses() {
        return ocFertilizerResponses;
    }

    public void setOcFertilizerResponses(List<OCFertilizerResponse> ocFertilizerResponses) {
        this.ocFertilizerResponses = ocFertilizerResponses;
    }
}
