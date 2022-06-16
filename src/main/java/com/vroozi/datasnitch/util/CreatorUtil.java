package com.vroozi.datasnitch.util;

import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.SyncTracker;
import java.util.Calendar;
import java.util.Date;

public class CreatorUtil {

  private CreatorUtil() {
  }
  public static SyncTracker createSyncTracker(
      String unitId, CollectionName collectionName, Integer recordReadCount
  ) {
    SyncTracker recordTracker = new SyncTracker();
    Date date = Calendar.getInstance().getTime();
    recordTracker.setLastReadDate(date);
    recordTracker.setCreatedDate(date);
    recordTracker.setReadRecordCount(recordReadCount);
    recordTracker.setCollectionName(collectionName);
    recordTracker.setUnitId(unitId);
    return recordTracker;
  }
}
