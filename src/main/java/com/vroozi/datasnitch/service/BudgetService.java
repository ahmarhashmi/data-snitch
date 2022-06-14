package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.CollectionName;

public interface BudgetService {

  void readAndPost(String unitId, CollectionName collectionName);
}
