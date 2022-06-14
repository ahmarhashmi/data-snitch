package com.vroozi.datastitch.service;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datastitch.repository.BudgetRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

  @Autowired
  private BudgetRepository budgetRepository;

  @Override
  public List<Budget> findAllByLastModifiedDate(String unitId, Date lastModifiedDate) {
    return budgetRepository.findByUnitIdAndLastModifiedDateGreaterThan(unitId, lastModifiedDate);
  }
}
