package com.vroozi.datasnitch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtils {

  private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

  private final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      .create();

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private JsonUtils() {
  }

  public static Gson getGson() {
    return gson;
  }

  public static String toJsonString(Object object) {
    return getGson().toJson(object);
  }

  public static String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  public static String toSafeJsonString(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    try {
      return getGson().fromJson(json, clazz);
    } catch (Exception e) {
      LOG.error("Error parsing json : ", e);
      return null;
    }
  }

  public static <T> T prepareObject(Object object, Class<T> cls) {
    return objectMapper.convertValue(object, cls);
  }
}
