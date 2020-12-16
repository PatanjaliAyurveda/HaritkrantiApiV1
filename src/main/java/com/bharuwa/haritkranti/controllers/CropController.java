package com.bharuwa.haritkranti.controllers;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.bharuwa.haritkranti.models.crops.CropGroup;
import com.bharuwa.haritkranti.models.crops.FruitVariety;
import com.bharuwa.haritkranti.models.newmodels.CropYield;
import com.bharuwa.haritkranti.models.newmodels.FarmCordinate;
import com.bharuwa.haritkranti.models.requestModels.CropGroupReq;
import com.bharuwa.haritkranti.models.requestModels.FilterByPCandSC;
import com.bharuwa.haritkranti.models.FilterByPrimaryCategoryReqModel;

import io.swagger.annotations.ApiOperation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author anuragdhunna
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CropController extends BaseController {

    @RequestMapping(value = "/getCropGroupsByType", method = RequestMethod.GET)
    public List<CropGroup> getCropGroupsByType(@Nullable @RequestParam String type) {
        return cropService.getCropGroupsByType(type);
    }

    @RequestMapping(value = "/getCropGroupsByNameAndType", method = RequestMethod.GET)
    public CropGroup getCropGroupsByNameAndType(@RequestParam String name, @RequestParam String type) {
        return cropService.getCropGroupsByNameAndType(name,type);
    }

    @RequestMapping(value = "/getFruitsByGroup", method = RequestMethod.GET)
    public List<FruitVariety> getFruitsByGroup(@RequestParam String cropGroupId) {
        return cropService.getFruitsByGroup(cropGroupId);
    }

    @ApiOperation(value = "Add Crop Group")
    @RequestMapping(value = "/addCropGroup", method = RequestMethod.POST)
    public CropGroup addCropGroup(@Valid @RequestBody CropGroupReq req) {
        return cropService.addCropGroup(req);
    }

    @ApiOperation(value = "Update Crop Group")
    @RequestMapping(value = "/updateCropGroup", method = RequestMethod.PUT)
    public CropGroup updateCropGroup(@RequestBody CropGroup cropGroup) {
        return cropService.updateCropGroup(cropGroup);
    }

    @ApiOperation(value = "Find crop by crop-name, cropGroup-type,cropGroup-name")
    @RequestMapping(value = "/findCropByNameAndType", method = RequestMethod.GET)
    public Crop findCropByNameAndType(@RequestParam String cropGroupName, @RequestParam String cropGroupType, @RequestParam String cropName) {
        return cropService.findCropByNameAndType(cropGroupName,cropGroupType,cropName);
    }
    
    @RequestMapping(value = "/addCropYieldData", method = RequestMethod.GET)
    public void addCropYieldData() {
    	try
        {
            FileInputStream file = new FileInputStream(new File("D:/CropYieldData/crop_yield_data.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            List<CropYield> list = new ArrayList<CropYield>();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                CropYield yield = new CropYield();
                int farmId = (int) Float.parseFloat(row.getCell(0).toString());
                yield.setFarmId(farmId);
                yield.setState(row.getCell(1).toString());
                yield.setDistrict(row.getCell(2).toString());
                yield.setTehsil(row.getCell(3).toString());
                yield.setBlock(row.getCell(4).toString());
                yield.setVillage(row.getCell(5).toString());
        //        yield.setKhasraNumber(row.getCell(6).toString());
                yield.setFarmSize(row.getCell(7).toString());
                yield.setCropName(row.getCell(8).toString());
       //         yield.setFarmType(row.getCell(9).toString());
       //         yield.setFarmingType(row.getCell(10).toString());
      //          yield.setnRequirement(row.getCell(11).toString());
      //          yield.setpRequirement(row.getCell(12).toString());
      //          yield.setkRequirement(row.getCell(13).toString());
                yield.setnValue(row.getCell(14).toString());
                yield.setpValue(row.getCell(15).toString());
                yield.setkValue(row.getCell(16).toString());
                yield.setOrganicCarbon(row.getCell(17).toString());
                yield.setPhValue(row.getCell(18).toString());
                if(row.getCell(19)!=null)
                	yield.setExpectedYeild(row.getCell(19).toString());
      //          yield.setFarmerName(row.getCell(20).toString());
                if(row.getCell(20)!=null)
     //           	yield.setMobileNumber(row.getCell(21).toString());
                list.add(yield);
                System.out.println(farmId);
            }
            file.close();
            cropService.saveCropYield(list);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/addFarmCoordinate", method = RequestMethod.GET)
    public void addFarmCoordinate() {
    	try
        {
            FileInputStream file = new FileInputStream(new File("D:/CropYieldData/crop_yield_data.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            List<FarmCordinate> list = new ArrayList<FarmCordinate>();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                FarmCordinate coordinate = new FarmCordinate();
                int farmId = (int) Float.parseFloat(row.getCell(0).toString());
                coordinate.setFarmId(farmId);
             //   coordinate.setCoordinateValue(row.getCell(2).toString());
                list.add(coordinate);
                System.out.println(farmId+"   "+row.getCell(2).toString());
            }
            file.close();
            cropService.saveFarmCoordinate(list);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/getAllCategories",method = RequestMethod.GET)
	public List<String> getAllCategories(@RequestParam String language) 
	{
		return csvServices.getAllCategories(language);
	}
    
    
    @RequestMapping(value = "/getSubCategories",method = RequestMethod.POST)
	public List<String> getSubCategoryListByPrimaryCategoryName(@RequestBody FilterByPrimaryCategoryReqModel reqBody)
	{
		return csvServices.getSubCategoryListByPrimaryCategoryName(reqBody.getPrimaryCategoryname(),reqBody.getState(),reqBody.getDistrict(),reqBody.getLanguage());
	}
    
    @RequestMapping(value = "/getVarieties",method = RequestMethod.POST)
	public List<String> getSubCategoryListByPrimaryCategoryName(@RequestBody FilterByPCandSC reqBody)
	{
		return csvServices.getVarietyList(reqBody.getPrimaryCategoryName(),reqBody.getSubCategoryName(),reqBody.getLanguage());
	}
}
