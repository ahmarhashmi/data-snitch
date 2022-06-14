package com.vroozi.datastitch.service;

import com.vroozi.datasnitch.model.Budget;
import java.util.Date;
import java.util.List;

public interface BudgetService {
  List<Budget> findAllByLastModifiedDate(String unitId, Date lastModifiedDate);
}
