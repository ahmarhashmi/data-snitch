package com.vroozi.datasnitch.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vroozi.datastitch.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class SnitchDataController {

  @Autowired
  private BudgetService budgetService;

  @PostMapping(value = "/data-snitch/organization/{unitId}/upload-record-to-s3")
  public void sendLatestRecordsToS3(@PathVariable("unitId") String unitId) {

  }

  @PostMapping(value = "/data-snitch/organization/{unitId}/upload-budget-to-s3")
  public void sendLatestBudgetToS3(@PathVariable("unitId") String unitId) {

  }

}
