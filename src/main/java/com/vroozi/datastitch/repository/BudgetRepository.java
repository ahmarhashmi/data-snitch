package com.vroozi.datastitch.repository;

import com.vroozi.datasnitch.model.Budget;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BudgetRepository extends MongoRepository<Budget, String> {

  List<Budget> findByUnitIdAndLastModifiedDateGreaterThan(String unitId, Date lastModifiedDate);
}
