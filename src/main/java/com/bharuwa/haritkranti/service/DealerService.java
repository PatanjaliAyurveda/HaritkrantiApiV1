package com.bharuwa.haritkranti.service;

import com.bharuwa.haritkranti.models.Dealer;

import java.util.List;

public interface DealerService {


    void createDealer(Dealer dealer);
    List<Dealer> getDealersByState(String stateId);
}

