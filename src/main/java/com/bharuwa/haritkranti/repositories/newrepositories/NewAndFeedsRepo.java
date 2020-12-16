package com.bharuwa.haritkranti.repositories.newrepositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bharuwa.haritkranti.models.newmodels.NewsAndFeeds;

public interface NewAndFeedsRepo extends MongoRepository<NewsAndFeeds, Long>{

}
