package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.Village;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VillageAsset extends BaseObject {

    private String agentId;

    // TODO: @dbref
    private Village village;

    @Deprecated
    private String villageId;

    private int governmentSeedCentre;
    private int watershedDevelopmentProject;
    private int communityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam;
    private int noOfFPOOrPACS ; //Farmers Producer Organization(FPOs)/ Primary Agriculture Cooperative Society(PACS)
    private int foodStorageWarehouse;
    private int soilTestingCentres;
    private int fertilizerShop;
    private int dripIrrigationOrsprinkler_Irrigation;
    private int milkRoutesOrChillingCentres;
    private int poultryDevelopmentProjects;
    private int goataryDevelopmentProject;
    private int veterinaryClinicHospital;
    private int communityPondsForFisheries ;
    private int extensionFacilitiesForAquaculture;
    private int villageConnectedToAllOrigin;
    private int internalCcOrBrickRoad;
    private int publicTransport;
    private int railwayStation;
    private int electricityForDomesticUse;
    private int electricitySupplyToMSMEUnits;
    private int selfHelpGroups;
    private int primarySchool;
    private int middleSchool;
    private int highSchool;
    private int seniorSecondarySchool;
    private int govtDegreeCollege;
    private int vocationalEducationalCentreOrITIOrRSETIOrDDUGKY;
    private int marketsOrMandisOrHaats;
    private int subCentrePHCOrCHC;
    private int janAushadhiKendra;
    private int drainageFacilities;
    private int communityWasteDisposalSystem;
    private int communityBioGasOrRecycleOfWasteForProductionUse;
    private int aanganwadiCentre;
    private int pDS; //Public Distribution System (PDS)
    private int panchayatBhawan;
    private int cSC;//Panchayat Bhawan co located with CSC
    private int publicInformationBoard;
    private int healthFacility;//Mother and Child Health facilities
    private int communityForest;
    private int cottage;//cottage and small scale units(Fabrication/Construction material/Dairy based/Textile etc.) units
    private int commonPastures;
    private int adultEducationCentre;
    private int publicLibrary;
    private int recreationalCentreOrSportsOrPlayground;
    private int banks;
    private int atm;
    private int internetCafe;
    private int spoOrPo;//Post office/Sub-Post office
    private int csc;//Common service Centre (CSC)

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public int getGovernmentSeedCentre() {
        return governmentSeedCentre;
    }

    public void setGovernmentSeedCentre(int governmentSeedCentre) {
        this.governmentSeedCentre = governmentSeedCentre;
    }

    public int getWatershedDevelopmentProject() {
        return watershedDevelopmentProject;
    }

    public void setWatershedDevelopmentProject(int watershedDevelopmentProject) {
        this.watershedDevelopmentProject = watershedDevelopmentProject;
    }

    public int getCommunityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam() {
        return communityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam;
    }

    public void setCommunityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam(int communityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam) {
        this.communityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam = communityRainWaterHarvestingSystemOrPondOrDamOrWatershedOrCheckDam;
    }

    public int getNoOfFPOOrPACS() {
        return noOfFPOOrPACS;
    }

    public void setNoOfFPOOrPACS(int noOfFPOOrPACS) {
        this.noOfFPOOrPACS = noOfFPOOrPACS;
    }

    public int getFoodStorageWarehouse() {
        return foodStorageWarehouse;
    }

    public void setFoodStorageWarehouse(int foodStorageWarehouse) {
        this.foodStorageWarehouse = foodStorageWarehouse;
    }

    public int getSoilTestingCentres() {
        return soilTestingCentres;
    }

    public void setSoilTestingCentres(int soilTestingCentres) {
        this.soilTestingCentres = soilTestingCentres;
    }

    public int getFertilizerShop() {
        return fertilizerShop;
    }

    public void setFertilizerShop(int fertilizerShop) {
        this.fertilizerShop = fertilizerShop;
    }

    public int getDripIrrigationOrsprinkler_Irrigation() {
        return dripIrrigationOrsprinkler_Irrigation;
    }

    public void setDripIrrigationOrsprinkler_Irrigation(int dripIrrigationOrsprinkler_Irrigation) {
        this.dripIrrigationOrsprinkler_Irrigation = dripIrrigationOrsprinkler_Irrigation;
    }

    public int getMilkRoutesOrChillingCentres() {
        return milkRoutesOrChillingCentres;
    }

    public void setMilkRoutesOrChillingCentres(int milkRoutesOrChillingCentres) {
        this.milkRoutesOrChillingCentres = milkRoutesOrChillingCentres;
    }

    public int getPoultryDevelopmentProjects() {
        return poultryDevelopmentProjects;
    }

    public void setPoultryDevelopmentProjects(int poultryDevelopmentProjects) {
        this.poultryDevelopmentProjects = poultryDevelopmentProjects;
    }

    public int getGoataryDevelopmentProject() {
        return goataryDevelopmentProject;
    }

    public void setGoataryDevelopmentProject(int goataryDevelopmentProject) {
        this.goataryDevelopmentProject = goataryDevelopmentProject;
    }

    public int getVeterinaryClinicHospital() {
        return veterinaryClinicHospital;
    }

    public void setVeterinaryClinicHospital(int veterinaryClinicHospital) {
        this.veterinaryClinicHospital = veterinaryClinicHospital;
    }

    public int getCommunityPondsForFisheries() {
        return communityPondsForFisheries;
    }

    public void setCommunityPondsForFisheries(int communityPondsForFisheries) {
        this.communityPondsForFisheries = communityPondsForFisheries;
    }

    public int getExtensionFacilitiesForAquaculture() {
        return extensionFacilitiesForAquaculture;
    }

    public void setExtensionFacilitiesForAquaculture(int extensionFacilitiesForAquaculture) {
        this.extensionFacilitiesForAquaculture = extensionFacilitiesForAquaculture;
    }

    public int getVillageConnectedToAllOrigin() {
        return villageConnectedToAllOrigin;
    }

    public void setVillageConnectedToAllOrigin(int villageConnectedToAllOrigin) {
        this.villageConnectedToAllOrigin = villageConnectedToAllOrigin;
    }

    public int getInternalCcOrBrickRoad() {
        return internalCcOrBrickRoad;
    }

    public void setInternalCcOrBrickRoad(int internalCcOrBrickRoad) {
        this.internalCcOrBrickRoad = internalCcOrBrickRoad;
    }

    public int getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(int publicTransport) {
        this.publicTransport = publicTransport;
    }

    public int getRailwayStation() {
        return railwayStation;
    }

    public void setRailwayStation(int railwayStation) {
        this.railwayStation = railwayStation;
    }

    public int getElectricityForDomesticUse() {
        return electricityForDomesticUse;
    }

    public void setElectricityForDomesticUse(int electricityForDomesticUse) {
        this.electricityForDomesticUse = electricityForDomesticUse;
    }

    public int getElectricitySupplyToMSMEUnits() {
        return electricitySupplyToMSMEUnits;
    }

    public void setElectricitySupplyToMSMEUnits(int electricitySupplyToMSMEUnits) {
        this.electricitySupplyToMSMEUnits = electricitySupplyToMSMEUnits;
    }

    public int getSelfHelpGroups() {
        return selfHelpGroups;
    }

    public void setSelfHelpGroups(int selfHelpGroups) {
        this.selfHelpGroups = selfHelpGroups;
    }

    public int getPrimarySchool() {
        return primarySchool;
    }

    public void setPrimarySchool(int primarySchool) {
        this.primarySchool = primarySchool;
    }

    public int getMiddleSchool() {
        return middleSchool;
    }

    public void setMiddleSchool(int middleSchool) {
        this.middleSchool = middleSchool;
    }

    public int getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(int highSchool) {
        this.highSchool = highSchool;
    }

    public int getSeniorSecondarySchool() {
        return seniorSecondarySchool;
    }

    public void setSeniorSecondarySchool(int seniorSecondarySchool) {
        this.seniorSecondarySchool = seniorSecondarySchool;
    }

    public int getGovtDegreeCollege() {
        return govtDegreeCollege;
    }

    public void setGovtDegreeCollege(int govtDegreeCollege) {
        this.govtDegreeCollege = govtDegreeCollege;
    }

    public int getVocationalEducationalCentreOrITIOrRSETIOrDDUGKY() {
        return vocationalEducationalCentreOrITIOrRSETIOrDDUGKY;
    }

    public void setVocationalEducationalCentreOrITIOrRSETIOrDDUGKY(int vocationalEducationalCentreOrITIOrRSETIOrDDUGKY) {
        this.vocationalEducationalCentreOrITIOrRSETIOrDDUGKY = vocationalEducationalCentreOrITIOrRSETIOrDDUGKY;
    }

    public int getMarketsOrMandisOrHaats() {
        return marketsOrMandisOrHaats;
    }

    public void setMarketsOrMandisOrHaats(int marketsOrMandisOrHaats) {
        this.marketsOrMandisOrHaats = marketsOrMandisOrHaats;
    }

    public int getSubCentrePHCOrCHC() {
        return subCentrePHCOrCHC;
    }

    public void setSubCentrePHCOrCHC(int subCentrePHCOrCHC) {
        this.subCentrePHCOrCHC = subCentrePHCOrCHC;
    }

    public int getJanAushadhiKendra() {
        return janAushadhiKendra;
    }

    public void setJanAushadhiKendra(int janAushadhiKendra) {
        this.janAushadhiKendra = janAushadhiKendra;
    }

    public int getDrainageFacilities() {
        return drainageFacilities;
    }

    public void setDrainageFacilities(int drainageFacilities) {
        this.drainageFacilities = drainageFacilities;
    }

    public int getCommunityWasteDisposalSystem() {
        return communityWasteDisposalSystem;
    }

    public void setCommunityWasteDisposalSystem(int communityWasteDisposalSystem) {
        this.communityWasteDisposalSystem = communityWasteDisposalSystem;
    }

    public int getCommunityBioGasOrRecycleOfWasteForProductionUse() {
        return communityBioGasOrRecycleOfWasteForProductionUse;
    }

    public void setCommunityBioGasOrRecycleOfWasteForProductionUse(int communityBioGasOrRecycleOfWasteForProductionUse) {
        this.communityBioGasOrRecycleOfWasteForProductionUse = communityBioGasOrRecycleOfWasteForProductionUse;
    }

    public int getAanganwadiCentre() {
        return aanganwadiCentre;
    }

    public void setAanganwadiCentre(int aanganwadiCentre) {
        this.aanganwadiCentre = aanganwadiCentre;
    }

    public int getpDS() {
        return pDS;
    }

    public void setpDS(int pDS) {
        this.pDS = pDS;
    }

    public int getPanchayatBhawan() {
        return panchayatBhawan;
    }

    public void setPanchayatBhawan(int panchayatBhawan) {
        this.panchayatBhawan = panchayatBhawan;
    }

    public int getcSC() {
        return cSC;
    }

    public void setcSC(int cSC) {
        this.cSC = cSC;
    }

    public int getPublicInformationBoard() {
        return publicInformationBoard;
    }

    public void setPublicInformationBoard(int publicInformationBoard) {
        this.publicInformationBoard = publicInformationBoard;
    }

    public int getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(int healthFacility) {
        this.healthFacility = healthFacility;
    }

    public int getCommunityForest() {
        return communityForest;
    }

    public void setCommunityForest(int communityForest) {
        this.communityForest = communityForest;
    }

    public int getCottage() {
        return cottage;
    }

    public void setCottage(int cottage) {
        this.cottage = cottage;
    }

    public int getCommonPastures() {
        return commonPastures;
    }

    public void setCommonPastures(int commonPastures) {
        this.commonPastures = commonPastures;
    }

    public int getAdultEducationCentre() {
        return adultEducationCentre;
    }

    public void setAdultEducationCentre(int adultEducationCentre) {
        this.adultEducationCentre = adultEducationCentre;
    }

    public int getPublicLibrary() {
        return publicLibrary;
    }

    public void setPublicLibrary(int publicLibrary) {
        this.publicLibrary = publicLibrary;
    }

    public int getRecreationalCentreOrSportsOrPlayground() {
        return recreationalCentreOrSportsOrPlayground;
    }

    public void setRecreationalCentreOrSportsOrPlayground(int recreationalCentreOrSportsOrPlayground) {
        this.recreationalCentreOrSportsOrPlayground = recreationalCentreOrSportsOrPlayground;
    }

    public int getBanks() {
        return banks;
    }

    public void setBanks(int banks) {
        this.banks = banks;
    }

    public int getAtm() {
        return atm;
    }

    public void setAtm(int atm) {
        this.atm = atm;
    }

    public int getInternetCafe() {
        return internetCafe;
    }

    public void setInternetCafe(int internetCafe) {
        this.internetCafe = internetCafe;
    }

    public int getSpoOrPo() {
        return spoOrPo;
    }

    public void setSpoOrPo(int spoOrPo) {
        this.spoOrPo = spoOrPo;
    }

    public int getCsc() {
        return csc;
    }

    public void setCsc(int csc) {
        this.csc = csc;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }
}