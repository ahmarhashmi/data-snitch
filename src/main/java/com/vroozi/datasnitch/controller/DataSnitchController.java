package com.vroozi.datasnitch.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class DataSnitchController {

  @Autowired
  private BudgetService budgetService;

  /**
   * Posts updated records on redshift for the given unitId and collection name
   */
  @GetMapping(value = "organization/{unitId}/collections/{collectionName}/post-updated", consumes = APPLICATION_JSON_VALUE)
  public Integer postUpdatedRecord(
      @PathVariable("unitId") String unitId,
      @PathVariable("collectionName") CollectionName collectionName
  ) {
    return budgetService.readAndPost(unitId, collectionName);
  }

  /**
   * Posts all records on redshift for the given unitId and collection name. This can be used for
   * the first time when there is no record uploaded on redshift for the unitId and collection name.
   */
  @GetMapping(value = "organization/{unitId}/collections/{collectionName}/post-all", consumes = APPLICATION_JSON_VALUE)
  public Integer postAllRecords(
      @PathVariable("unitId") String unitId,
      @PathVariable("collectionName") CollectionName collectionName
  ) {
    return budgetService.readAndPostAllRecords(unitId, collectionName);
  }

}
