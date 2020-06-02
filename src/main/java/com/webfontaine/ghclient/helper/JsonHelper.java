package com.webfontaine.ghclient.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper class for converting json to object and json to object
 */
public class JsonHelper {

    /**
     * Parse object to json
     * @param obj object
     * @return json string
     */
    public static String parseToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse", e);
        }
    }

    /**
     * Parse json to object
     * @param json json string
     * @param clazz class to be converted
     * @param <T> class to be converted
     * @return object
     */
    public static <T> T parseFromJson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse", e);
        }
    }
}
