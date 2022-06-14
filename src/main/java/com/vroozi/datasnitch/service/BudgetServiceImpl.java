package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.repository.BudgetRepository;
import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import com.vroozi.datasnitch.service.rest.RestClient;
import com.vroozi.datasnitch.util.FileUtils;
import com.vroozi.datasnitch.util.JsonUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

  public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("ddMMyyyyhhmmss");

  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Autowired
  private RestClient restClient;

  @Autowired
  private RestServiceUrl restServiceUrl;

  @Override
  public void readAndPost(String unitId, CollectionName collectionName) {
    String bucketName = restServiceUrl.getBucketName();
    String folderName = restServiceUrl.getFolderName();
    syncTrackerRepository.findFirstByUnitIdAndCollectionNameOrderByLastReadDateDesc(
        unitId, collectionName).map(syncTracker -> {
      ZonedDateTime zdt = syncTracker.getLastReadDate().atZone(ZoneId.systemDefault());
      Date convertedDate = Date.from(zdt.toInstant());
      List<Budget> budgets = budgetRepository.findByLastModifiedDateGreaterThan(convertedDate);
      return true;
    }).orElse(uploadAll(unitId, bucketName, folderName));
  }

  boolean uploadAll(String unitId, String bucketName, String folderName) {
    List<Budget> budgets = budgetRepository.findAllByUnitId(unitId);
    budgets.forEach(budget -> {
      String fileName =
          "Budget_" + budget.getName() + "_" + DATE_TIME_FORMAT.format(new Date()) + ".json";
      try {
        String jsonString = JsonUtils.toSafeJsonString(budget);
        if (StringUtils.isNotBlank(jsonString)) {
          File jsonFile = generateJsonFile(jsonString, fileName);
          if (Objects.nonNull(jsonFile)) {
            restClient.upload(jsonFile, bucketName, budget.getUnitId(), folderName);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    return true;
  }

  protected File generateJsonFile(String data, String fileName) throws IOException {
    return FileUtils.writeStringToFile(data, restServiceUrl.getReportPath(), fileName);
  }
}
