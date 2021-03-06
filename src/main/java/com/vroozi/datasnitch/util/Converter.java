package com.vroozi.datasnitch.util;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.DurationType;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.model.OverBudgetAction;
import com.vroozi.datasnitch.model.PeriodicAllocationType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Converter {

  private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);

  public static final String NVARCHAR = "NVARCHAR";
  public static final String BOOL = "BOOL";
  public static final String DATE = "DATE";
  public static final String INT = "INT";
  public static final String INT8 = "INT8";
  public static final String LIST = "LIST";

  private Converter() {
  }

  public static Map<String, MetaData> convertToMetaDataMap(Budget budget) {
    Map<String, MetaData> dataMap = new HashMap<>();
    try {
      Class objClass = Class.forName(budget.getClass().getName());
      Class supperClass = objClass.getSuperclass();
      Field[] childFields = null;
      if (CollectionUtils.isNotEmpty(budget.getBudgetAssociations())
          && Objects.nonNull(budget.getBudgetAssociations().get(0))) {
        childFields = getChildFields(budget.getBudgetAssociations().get(0).getClass());
      }
      Field[] fields = objClass.getDeclaredFields();
      Field[] parentFields = supperClass.getDeclaredFields();
      String jsonStr = JsonUtils.toSafeJsonString(budget);
      JSONObject jsonObject = new JSONObject(jsonStr);
      // check fields that are initialized in the Model class
      Field[] finalChildFields = childFields;
      jsonObject.keySet().forEach(key -> {
        Object value = jsonObject.get(key);
        MetaData metaData = new MetaData();
        metaData.setType(value.getClass());
        if (metaData.getType() == JSONArray.class && finalChildFields != null) {
          JSONArray jsonArray = jsonObject.getJSONArray(key);
          if (jsonArray.optJSONObject(0) == null) {
            List<Object> elementList = getChildrenList(jsonArray, key);
            metaData.setValue(elementList);
          } else {
            List<Map<String, MetaData>> childrenMapList = getChildrenMapList(jsonObject,
                finalChildFields, key);
            metaData.setValue(childrenMapList);
          }

        } else {
          metaData.setValue(value);
        }
        dataMap.put(key, metaData);
      });
      putInMap(parentFields, dataMap, jsonObject);
      putInMap(fields, dataMap, jsonObject);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return dataMap;
  }

  static Field[] getChildFields(Class clazz) {
    return clazz.getDeclaredFields();
  }

  static List<Map<String, MetaData>> getChildrenMapList(JSONObject jsonObject, Field[] fields,
      String key) {
    JSONArray jsonArray = jsonObject.getJSONArray(key);
    List<Map<String, MetaData>> childrenMapList = new ArrayList<>();
    for (int index = 0; index < jsonArray.length(); index++) {
      JSONObject jObject = jsonArray.getJSONObject(index);
      Map<String, MetaData> childDataMap = new HashMap<>();
      putInMap(fields, childDataMap, jObject);
      childrenMapList.add(childDataMap);
    }
    return childrenMapList;
  }

  static List<Object> getChildrenList(JSONArray jsonArray, String key) {
    List<Object> elementList = new ArrayList<>();
    for (int index = 0; index < jsonArray.length(); index++) {
      Object element = jsonArray.get(index);
      elementList.add(element);
    }
    return elementList;
  }

  static void putInMap(Field[] fields, Map<String, MetaData> dataMap, JSONObject jsonObject) {
    if (fields != null && fields.length > 0) {
      for (Field field : fields) {
        if (field.getType() == List.class) {
          continue;
        }
        Object value = jsonObject.get(field.getName());
        MetaData metaData = new MetaData();
        metaData.setValue(value);
        metaData.setType(field.getType());
        dataMap.put(field.getName(), metaData);
      }
    }
  }

  public static String concatenateColumns(Map<String, MetaData> dataMap) {
    StringJoiner joiner = new StringJoiner(",");
    dataMap.keySet().forEach(key -> {
      joiner.add(key + " " + getType(dataMap.get(key).getType()));
    });
    return joiner.toString();
  }

  /**
   * Returns Pair of comma separated column names String (Left) and List of values (Right)
   */
  public static Pair<String, List<Object>> getColumnHeadersAndValues(
      Map<String, MetaData> dataMap, String parentId, boolean isChild
  ) {
    StringJoiner keyJoiner = new StringJoiner(",");
    List<Object> values = new LinkedList<>();
    dataMap.forEach((key, value) -> {
      String type = getType(value.getType());
      if (NVARCHAR.equals(type)) {
        if (!(Objects.isNull(value.getValue()) || "null".equals(value.getValue().toString()))) {
          keyJoiner.add(key);
          values.add(value.getValue());
        }
      } else if (BOOL.equals(type)) {
        keyJoiner.add(key);
        values.add(Boolean.valueOf(value.getValue().toString()));
      } else if (INT.equals(type) || INT8.equals(type)) {
        keyJoiner.add(key);
        values.add(Integer.valueOf(value.getValue().toString()));
      } else if (DATE.equals(type)) {
        keyJoiner.add(key);
        values.add(new Timestamp(Long.parseLong(value.getValue().toString())).toString());
      }
    });
    if (isChild) {
      keyJoiner.add("parentId");
      values.add(parentId);
    }
    return Pair.of(keyJoiner.toString(), values);
  }

  public static Map<String, List<Object>> getChildren(
      Map<String, MetaData> dataMap) {
    Map<String, List<Object>> childMetaData = new HashMap<>();
    dataMap.forEach((key, value) -> {
      if (Objects.nonNull(value) && Objects.nonNull(value.getValue())) {
        String type = getType(value.getType());
        if (LIST.equals(type)) {
          List<Object> child = new ArrayList<>();
          try {
            child = (List<Object>) value.getValue();
          } catch (Exception e) {
            LOGGER.info("Exception occoured while parsing the list for field {}", key);
          }
          if (CollectionUtils.isNotEmpty(child)) {
            childMetaData.put(key, child);
          }
        }
      }
    });
    return childMetaData;
  }

  /**
   * Returns comma separated question marks (?) to be used for VALUES in the JDBC queries
   */
  public static String getQuestionMarks(List<Object> values) {
    StringJoiner joiner = new StringJoiner(",");
    values.forEach(value -> joiner.add("?"));
    return joiner.toString();
  }

  public static String getType(Class type) {
    if (type == String.class
        || type == Double.class
        || type == BigDecimal.class
        || type == DurationType.class
        || type == PeriodicAllocationType.class
        || type == OverBudgetAction.class) {
      return NVARCHAR;
    } else if (type == Boolean.class || type == boolean.class) {
      return BOOL;
    } else if (type == Date.class) {
      return DATE;
    } else if (type == Integer.class || type == int.class) {
      return INT;
    } else if (type == Long.class || type == long.class) {
      return INT8;
    } else if (type == List.class || type == JSONArray.class) {
      return LIST;
    }
    return NVARCHAR;
  }
}
