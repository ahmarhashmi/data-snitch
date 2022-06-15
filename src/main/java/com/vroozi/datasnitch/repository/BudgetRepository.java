package com.vroozi.datasnitch.repository;

import com.vroozi.datasnitch.model.Budget;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends MongoRepository<Budget, String> {

  List<Budget> findByLastModifiedDateGreaterThan(Date lastModifiedDate);

  List<Budget> findByUnitId(String unitId);
}
