package com.bharuwa.haritkranti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bharuwa.haritkranti.models.FertilizerCalculations;
import com.bharuwa.haritkranti.models.FertilizerCalculationsInHindi;
import com.bharuwa.haritkranti.models.NPKRequiredValue;
import com.bharuwa.haritkranti.models.NPKStandardValue;
import com.bharuwa.haritkranti.models.crops.CropDetail;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.service.FertilizerRecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author sunaina
 */
@RestController
@RequestMapping("/api")
public class FertilizerRecommendationController {

    @Autowired
    public FertilizerRecommendationService fertilizerRecommendationService;

    /**
     * get list of FertilizerRecommendation from FertilizerCalculations table against cropName, agroClimaticZoneName
     **/
    
    @RequestMapping(value = "/getFertilizerCal", method = RequestMethod.GET)
    public List getFertilizerCal(@RequestParam String cropName,@RequestParam String language,@RequestParam Integer farmId,@RequestParam String userId) {
        return fertilizerRecommendationService.getFertilizerCal(cropName,language,farmId,userId);
    }
    
    @RequestMapping(value = "/storeFertilizerCal",method = RequestMethod.POST)
    public FertilizerCalculationsInHindi storeFertilizerCal(@RequestBody FertilizerCalculationsInHindi fertilizerCalculationsInHindi){
        return fertilizerRecommendationService.storeFertilizerCal(fertilizerCalculationsInHindi);
    }
    
    @RequestMapping(value = "/storeFertCalcInEnglish",method = RequestMethod.POST)
    public void storeFertCalcInEnglish(@RequestParam Integer farmId,@RequestParam String userId){
    	CropDetail cropDetail = fertilizerRecommendationService.getCropDetailByFarmId(farmId);
    	CropYield cropYield=fertilizerRecommendationService.getCropYieldByFarmId(farmId);
    	if(cropDetail !=null) {
    		cropYield.setCropName(cropDetail.getActualCrop());
    		cropYield.setCropNameInHindi(cropDetail.getActualCropInHindi());
    	}
    	NPKStandardValue standardValue = fertilizerRecommendationService.getNPKStandardValue(cropYield.getCropName());
    	NPKRequiredValue requiredValue = npkCalculation(cropYield,standardValue);
    	FertilizerCalculations calculation = fertilizerCalculation(requiredValue);
    	fertilizerRecommendationService.saveFertilizerCalculation(calculation,farmId,userId);
    }
    
    public NPKRequiredValue npkCalculation(CropYield cropYield,NPKStandardValue standardValue) {
    	
    	int nValue=0;
		int pValue=0;
		int kValue=0;
		
		boolean greenManureIsFound=false;
		boolean organicManureIsFound=false;
		boolean isLegumeCrop=false;
		
		String nCurrentLevel = "low";
		if(Float.parseFloat(cropYield.getnValue()) >= 0 && Float.parseFloat(cropYield.getnValue())<=280)
			nCurrentLevel = "low";
		else if(Float.parseFloat(cropYield.getnValue()) > 280 && Float.parseFloat(cropYield.getnValue())<=460)
			nCurrentLevel = "moderate";
		else if(Float.parseFloat(cropYield.getnValue()) > 460 && Float.parseFloat(cropYield.getnValue())<=700)
			nCurrentLevel = "high";
		
		String pCurrentLevel = "low";
		if(Float.parseFloat(cropYield.getpValue()) >= 0 && Float.parseFloat(cropYield.getpValue())<=10)
			pCurrentLevel = "low";
		else if(Float.parseFloat(cropYield.getpValue()) > 10 && Float.parseFloat(cropYield.getpValue())<=25)
			pCurrentLevel = "moderate";
		else if(Float.parseFloat(cropYield.getpValue()) > 25 && Float.parseFloat(cropYield.getpValue())<=35)
			pCurrentLevel = "high";
		
		String kCurrentLevel = "low";
		if(Float.parseFloat(cropYield.getkValue()) >= 0 && Float.parseFloat(cropYield.getkValue())<=150)
			kCurrentLevel = "low";
		else if(Float.parseFloat(cropYield.getkValue()) > 150 && Float.parseFloat(cropYield.getkValue())<=250)
			kCurrentLevel = "moderate";
		else if(Float.parseFloat(cropYield.getkValue()) > 250 && Float.parseFloat(cropYield.getkValue())<=300)
			kCurrentLevel = "high";
		
		if(nCurrentLevel.equalsIgnoreCase("low")) {
			nValue = standardValue.getnStandardValue()+((standardValue.getnStandardValue()*25)/100);
		}else if(nCurrentLevel.equalsIgnoreCase("moderate")) {
			int tempValue=0;
			tempValue = standardValue.getnStandardValue();
			nValue = tempValue;
			if(greenManureIsFound || organicManureIsFound ||isLegumeCrop) {
				nValue = nValue - ((tempValue*25)/100);
			}
		}else if(nCurrentLevel.equalsIgnoreCase("high")) {
			int tempValue=0;
			tempValue = standardValue.getnStandardValue();
			nValue = standardValue.getnStandardValue()-((standardValue.getnStandardValue()*25)/100);
			if(greenManureIsFound || organicManureIsFound ||isLegumeCrop) {
				nValue = nValue - ((tempValue*25)/100);
			}
		}
		
		if(pCurrentLevel.equalsIgnoreCase("low")) {
			pValue = standardValue.getpStandardValue()+((standardValue.getpStandardValue()*25)/100);
		}else if(pCurrentLevel.equalsIgnoreCase("moderate")) {
			int tempValue=0;
			tempValue = standardValue.getpStandardValue();
			pValue=tempValue;
			if(organicManureIsFound || isLegumeCrop) {
				pValue = pValue - ((tempValue*25)/100);
			}
		}else if(pCurrentLevel.equalsIgnoreCase("high")) {
			int tempValue=0;
			tempValue = standardValue.getpStandardValue();
			pValue = standardValue.getpStandardValue()-((standardValue.getpStandardValue()*25)/100);
			if(organicManureIsFound ||isLegumeCrop) {
				pValue = pValue - ((tempValue*25)/100);
			}
		}
		
		if(kCurrentLevel.equalsIgnoreCase("low")) {
			kValue = standardValue.getkStandardValue()+((standardValue.getkStandardValue()*25)/100);
		}else if(kCurrentLevel.equalsIgnoreCase("moderate")) {
			int tempValue=0;
			tempValue = standardValue.getkStandardValue();
			kValue=tempValue;
			if(organicManureIsFound ||isLegumeCrop) {
				kValue = kValue - ((tempValue*25)/100);
			}
		}else if(kCurrentLevel.equalsIgnoreCase("high")) {
			int tempValue=0;
			tempValue = standardValue.getkStandardValue();
			kValue = standardValue.getkStandardValue()-((standardValue.getkStandardValue()*25)/100);
			if(organicManureIsFound ||isLegumeCrop) {
				kValue = kValue - ((tempValue*25)/100);
			}
		}
		NPKRequiredValue requiredValue = new NPKRequiredValue(nValue,pValue,kValue);
		requiredValue.setCropName(cropYield.getCropName());
		requiredValue.setCropNameInHindi(cropYield.getCropNameInHindi());
		
		System.out.println("NPK Recomendation"+" is "+nValue+" "+pValue+" "+kValue);
		return requiredValue;
    }
    
    public FertilizerCalculations fertilizerCalculation(NPKRequiredValue requiredValue) {
    	
    	float nValue=0;
		float pValue=0;
		float kValue=0;
		
		int nFert=0;
		int pFert=0;
		int kFert=0;
		int sFert=0;
		int mix101026Fert=0;
		int mix123216Fert=0;
		
	/*	List<NPKStandardValue> standardValueList = new ArrayList<NPKStandardValue>();
		standardValueList.add(new NPKStandardValue("Sunflower", 48, 24, 16));
		standardValueList.add(new NPKStandardValue("Rajma", 40, 32, 16));
		standardValueList.add(new NPKStandardValue("Sugarcane", 60, 24, 20));
		standardValueList.add(new NPKStandardValue("Wheat", 60, 24, 16));
		standardValueList.add(new NPKStandardValue("Soyabean", 8, 24, 16));
		standardValueList.add(new NPKStandardValue("Peagonpea", 6, 20, 12));
		standardValueList.add(new NPKStandardValue("Maize Early", 40, 24, 16));
		standardValueList.add(new NPKStandardValue("Paddy Hybrid", 60, 24, 16));*/
		
		//Scanner sc= new Scanner(System.in); 
	//	System.out.println("Enter crop name ");
		String cropName = requiredValue.getCropName();
	//	System.out.println("Enter Fertilizer Set");
		String fertilizerSet = "UDSM123216";
	//	sc.close();
		
		nValue = requiredValue.getnValue();
		pValue = requiredValue.getpValue();
		kValue = requiredValue.getkValue();
		
		if(fertilizerSet.equalsIgnoreCase("UDM")) {
			float nTempValue = 0.f;
			
			float temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
		}else if(fertilizerSet.equalsIgnoreCase("UDSM")) {
			
			float temp = 100.0f/16.0f ;
			float pTempValue = pValue*30.0f/100.0f;
			float sTempFert=temp*pTempValue;
			sFert = Math.round(sTempFert);
			
			float pRemainingValue = pValue-pTempValue;
			pValue = pRemainingValue;
			temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			float nTempValue = 0.f;
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
			
		}else if(fertilizerSet.equalsIgnoreCase("UDM102626")) {
			float temp = 100.0f/26.0f ;
			float pTempValue = pValue*30.0f/100.0f;
			temp=temp*pTempValue;
			mix101026Fert = Math.round(temp);
			
		//	temp = 100.0f/16.0f ;
		//	temp=temp*pTempValue;
		//	sFert = Math.round(temp);
			
			float pRemainingValue = pValue-pTempValue;
			pValue = pRemainingValue;
			temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			float nTempValue = 0.f;
			temp = 10.0f/100.0f;
			temp = temp*mix101026Fert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			nTempValue = 0.f;
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			float kTempValue = 0.f;
			temp = 26.0f/100.0f;
			temp = temp*mix101026Fert;
			kTempValue = kValue-temp;
			kValue = kTempValue;
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
		}else if(fertilizerSet.equalsIgnoreCase("UDM123216")) {
			float temp = 100.0f/32.0f ;
			float pTempValue = pValue*30.0f/100.0f;
			temp=temp*pTempValue;
			mix123216Fert = Math.round(temp);
			
		//	temp = 100.0f/16.0f ;
		//	temp=temp*pTempValue;
		//	sFert = Math.round(temp);
			
			float pRemainingValue = pValue-pTempValue;
			pValue = pRemainingValue;
			temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			float nTempValue = 0.f;
			temp = 12.0f/100.0f;
			temp = temp*mix123216Fert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			nTempValue = 0.f;
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			float kTempValue = 0.f;
			temp = 16.0f/100.0f;
			temp = temp*mix123216Fert;
			kTempValue = kValue-temp;
			kValue = kTempValue;
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
		}else if(fertilizerSet.equalsIgnoreCase("UDSM102626")) {
			
			float temp = 100.0f/26.0f ;
			float pTempValue = pValue*30.0f/100.0f;
			temp=temp*pTempValue;
			mix101026Fert = Math.round(temp);
			
			temp = 100.0f/16.0f ;
			temp=temp*pTempValue;
			sFert = Math.round(temp);
			
			float pRemainingValue = pValue-pTempValue-pTempValue;
			pValue = pRemainingValue;
			temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			float nTempValue = 0.f;
			temp = 10.0f/100.0f;
			temp = temp*mix101026Fert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			nTempValue = 0.f;
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			float kTempValue = 0.f;
			temp = 26.0f/100.0f;
			temp = temp*mix101026Fert;
			kTempValue = kValue-temp;
			kValue = kTempValue;
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
		}else if(fertilizerSet.equalsIgnoreCase("UDSM123216")) {
			float temp = 100.0f/32.0f ;
			float pTempValue = pValue*30.0f/100.0f;
			temp=temp*pTempValue;
			mix123216Fert = Math.round(temp);
			
			temp = 100.0f/16.0f ;
			temp=temp*pTempValue;
			sFert = Math.round(temp);
			
			float pRemainingValue = pValue-pTempValue-pTempValue;
			pValue = pRemainingValue;
			temp = 100.0f/46.0f ;
			temp = temp * pValue;
			pFert = Math.round(temp);
			
			float nTempValue = 0.f;
			temp = 12.0f/100.0f;
			temp = temp*mix123216Fert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			nTempValue = 0.f;
			temp = 18.0f/100.0f;
			temp = temp*pFert;
			nTempValue = nValue-temp;
			nValue = nTempValue;
			
			temp = 100.0f/46.0f;
			temp = temp * nValue;
			nFert = Math.round(temp);
			
			float kTempValue = 0.f;
			temp = 16.0f/100.0f;
			temp = temp*mix123216Fert;
			kTempValue = kValue-temp;
			kValue = kTempValue;
			
			temp = 100.0f/60.0f;
			temp = temp * kValue;
			kFert = Math.round(temp);
		}
		FertilizerCalculations calculation = new FertilizerCalculations();
		calculation.setUrea(nFert);
		calculation.setDap(pFert);
		calculation.setMop(kFert);
		calculation.setSsp(sFert);
		calculation.setMixFert102626(mix101026Fert);
		calculation.setMixFert123216(mix123216Fert);
		calculation.setCropName(requiredValue.getCropName());
		calculation.setCropNameInHindi(requiredValue.getCropNameInHindi());
		return calculation;
	//	System.out.println("Fertilizer Recomendation for "+cropName+" is "+nFert+" "+pFert+" "+kFert+" "+sFert+" "+mix101026Fert+" "+mix123216Fert);
    }
}
