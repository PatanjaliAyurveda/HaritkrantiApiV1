package com.bharuwa.haritkranti.models.newmodels;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MandiRate {
	
	private String index_name;
	private String title;
	private String desc;
	private String org_type;  
	private List<MandiRateRecord> records;
	
	public List<MandiRateRecord> getRecords() {
		return records;
	}
	public void setRecords(List<MandiRateRecord> records) {
		this.records = records;
	}
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
    
}
