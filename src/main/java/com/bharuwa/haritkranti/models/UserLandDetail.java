package com.bharuwa.haritkranti.models;

import com.bharuwa.haritkranti.models.location.*;
import com.bharuwa.haritkranti.models.location.Tehsil;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author harman
 */
@Document
public class UserLandDetail extends BaseObject {

    public enum OwnershipType {
        Purchased, Leased, Rented, Inherited_From_Ancestors, Self
    }

    public enum SoilType {
    	Alluvial_Soil, Sandy_Loam_Soil
    }
    
    public enum SourceOfIrrigation {
        Tubewells, Springs, River, Lake, Reservoir, Canal
    }

    private String userId;

    private String agentId;

    private String ownerName;
    private LandType.Type farmType = LandType.Type.Irrigated;

    private SourceOfIrrigation irrigationSource = SourceOfIrrigation.Tubewells;

    private BigDecimal landSize = BigDecimal.ZERO;
    private BigDecimal agricultureLandSize = BigDecimal.ZERO;
    private FieldSize.FieldSizeType landSizeType = FieldSize.FieldSizeType.Acre;
    private OwnershipType ownershipType = OwnershipType.Inherited_From_Ancestors;
  //  private SoilType soilType = SoilType;

    @Indexed
    private String khasraNo;

    // Location Fields
    @DBRef
    @Indexed
    private State state;
    
    @DBRef
    @Indexed
    private City city;
    
    @DBRef
    @Indexed
    private Tehsil tehsil;
    
    @DBRef
    @Indexed
    private Block block;
    
    @DBRef
    @Indexed
    private Village villageModel;

    @DBRef
    private Soil soil;

    private String lattitude;
    private String longitude;

    private boolean organicLandEligible;

    //if already generated OC from Annadata
    private boolean alreadyGeneratedOc = false;

    //if already recevied any OC from any Agency
    private boolean alreadyReceivedOC = false;

    private String landMapId;

   // @Deprecated
    private String village;     //agentapp
    
    private String blockName;
    
    private String tehsilName;
  
    @Deprecated
    private String soilId;
    
    @Deprecated
    private String cityId;

    // store land detail id if same khasra already exist
    private String relativeLandDetailId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKhasraNo() {
        return khasraNo;
    }

    public void setKhasraNo(String khasraNo) {
        this.khasraNo = khasraNo;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LandType.Type getFarmType() {
        return farmType;
    }

    public void setFarmType(LandType.Type farmType) {
        this.farmType = farmType;
    }

    public BigDecimal getLandSize() {
        return landSize;
    }

    public void setLandSize(BigDecimal landSize) {
        this.landSize = landSize;
    }

    public FieldSize.FieldSizeType getLandSizeType() {
        return landSizeType;
    }

    public void setLandSizeType(FieldSize.FieldSizeType landSizeType) {
        this.landSizeType = landSizeType;
    }

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSoilId() {
        return soilId;
    }

    public void setSoilId(String soilId) {
        this.soilId = soilId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public SourceOfIrrigation getIrrigationSource() {
        return irrigationSource;
    }

    public void setIrrigationSource(SourceOfIrrigation irrigationSource) {
        this.irrigationSource = irrigationSource;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Soil getSoil() {
        return soil;
    }

    public void setSoil(Soil soil) {
        this.soil = soil;
    }

/*    public Tehsil getTehsil() {
        return tehsil;
    }

    public void setTehsil(Tehsil tehsil) {
        this.tehsil = tehsil;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Village getVillageModel() {
        return villageModel;
    }

    public void setVillageModel(Village villageModel) {
        this.villageModel = villageModel;
    } */

    public boolean isOrganicLandEligible() {
        return organicLandEligible;
    }

    public void setOrganicLandEligible(boolean organicLandEligible) {
        this.organicLandEligible = organicLandEligible;
    }

    public boolean isAlreadyReceivedOC() {
        return alreadyReceivedOC;
    }

    public boolean isAlreadyGeneratedOc() {
        return alreadyGeneratedOc;
    }

    public void setAlreadyGeneratedOc(boolean alreadyGeneratedOc) {
        this.alreadyGeneratedOc = alreadyGeneratedOc;
    }

    public void setAlreadyReceivedOC(boolean alreadyReceivedOC) {
        this.alreadyReceivedOC = alreadyReceivedOC;
    }

    public String getLandMapId() {
        return landMapId;
    }

    public void setLandMapId(String landMapId) {
        this.landMapId = landMapId;
    }

    public String getRelativeLandDetailId() {
        return relativeLandDetailId;
    }

    public void setRelativeLandDetailId(String relativeLandDetailId) {
        this.relativeLandDetailId = relativeLandDetailId;
    }

	public BigDecimal getAgricultureLandSize() {
		return agricultureLandSize;
	}

	public void setAgricultureLandSize(BigDecimal agricultureLandSize) {
		this.agricultureLandSize = agricultureLandSize;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Tehsil getTehsil() {
		return tehsil;
	}

	public void setTehsil(Tehsil tehsil) {
		this.tehsil = tehsil;
	}

	public String getTehsilName() {
		return tehsilName;
	}

	public void setTehsilName(String tehsilName) {
		this.tehsilName = tehsilName;
	}

	public Village getVillageModel() {
		return villageModel;
	}

	public void setVillageModel(Village villageModel) {
		this.villageModel = villageModel;
	}
    
}
