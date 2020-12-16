package com.bharuwa.haritkranti.repositories;

import com.bharuwa.haritkranti.models.ReportHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anuragdhunna
 */
@Repository
public interface ReportHistoryRepo extends MongoRepository<ReportHistory, Long> {

}
