package com.bharuwa.haritkranti.repositories.newrepositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.CropSelection;

public interface CropSelectionRepo extends MongoRepository<CropSelection, Long>{

}
