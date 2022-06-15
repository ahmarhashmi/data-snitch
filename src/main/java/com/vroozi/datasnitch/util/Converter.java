package com.vroozi.datasnitch.util;

import com.google.gson.JsonArray;
import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.DurationType;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.model.OverBudgetAction;
import com.vroozi.datasnitch.model.PeriodicAllocationType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

public class Converter {

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
      Field[] fields = objClass.getDeclaredFields();
      Field[] parentFields = supperClass.getDeclaredFields();
      String jsonStr = JsonUtils.toSafeJsonString(budget);
      JSONObject jsonObject = new JSONObject(jsonStr);
      // check fields that are initialized in the Model class
      jsonObject.keySet().forEach(key -> {
        Object value = jsonObject.get(key);
        MetaData metaData = new MetaData();
        metaData.setValue(value);
        metaData.setType(value.getClass());
        if (metaData.getType() == String.class) {
          metaData.setLength(((String) value).length());
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

  static void putInMap(Field[] fields, Map<String, MetaData> dataMap, JSONObject jsonObject) {
    if (fields != null && fields.length > 0) {
      for (Field field : fields) {
        Object value = jsonObject.get(field.getName());
        MetaData metaData = new MetaData();
        metaData.setValue(value);
        metaData.setType(field.getType());
        if (metaData.getType() == String.class && Objects.nonNull(value)) {
          metaData.setLength(((String) value).length());
        }
        dataMap.put(field.getName(), metaData);
      }
    }
  }

  public static Map<String, Object> convertToObjectMap(Budget budget) {
    Map<String, Object> dataMap = new HashMap<>();
    String jsonStr = JsonUtils.toSafeJsonString(budget);
    JSONObject jsonObject = new JSONObject(jsonStr);
    jsonObject.keySet().forEach(key -> {
      dataMap.put(key, jsonObject.get(key));
    });
    return dataMap;
  }

  public static String concatenateColumns(Map<String, MetaData> dataMap) {
    StringJoiner joiner = new StringJoiner(",");
    dataMap.keySet().forEach(key -> {
      joiner.add(key + " " + getType(dataMap.get(key).getType()));
    });
    return joiner.toString();
  }

  public static Pair<String, List<Object>> getColumns(Map<String, MetaData> dataMap) {
    StringJoiner keyJoiner = new StringJoiner(",");
    List<Object> values = new LinkedList<>();
    dataMap.forEach((key, value) -> {
      String type = getType(value.getType());
      if (NVARCHAR.equals(type)) {
        if (!(Objects.isNull(value.getValue()) || "null".equals(value.getValue().toString()))) {
          keyJoiner.add(key);
          values.add( value.getValue());
        }
      } else if (BOOL.equals(type)) {
        keyJoiner.add(key);
        values.add(new Boolean(value.getValue().toString()));
      } else if (INT.equals(type) || INT8.equals(type)) {
        keyJoiner.add(key);
        values.add(new Integer(value.getValue().toString()));
      }  else if (DATE.equals(type)) {
        keyJoiner.add(key);
        values.add(new Timestamp(new Long(value.getValue().toString())).toString());
      }
    });
    return Pair.of(keyJoiner.toString(), values);
  }
  public static String getQuestionMarks(List<Object> values) {
    StringJoiner joiner = new StringJoiner(",");
    values.forEach(value -> {
      joiner.add("?");
    });
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
    } else if (type == List.class) {
      return LIST;
    } else if (type == JSONArray.class) {
      return LIST;
    }
    return NVARCHAR;
  }

  public static void main(String[] args) {
    String abc = "abc";

    System.out.println("'" + abc + "'");
  }
}
