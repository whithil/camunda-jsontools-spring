package org.camunda.bpm.getstarted.demo.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClassGen {
    public static <firstElemClass> BeanMap generateBean(JSONObject json) {
        BeanGenerator generator = new BeanGenerator();
    
        Iterator keys = json.keys();
    
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = null;
            try{
                value = json.getBoolean((String) key);
            } catch (JSONException e) {
                value = json.get((String) key);
            }
            Class keyClass = json.isNull(key.toString()) ? String.class : guessValueClass(value); //null-safe type-guessing
            generator.addProperty(key.toString(), keyClass);
        }
    
        Object result = generator.create();
        BeanMap bean = BeanMap.create(result);
        keys = json.keys();
    
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = null;
            try{
                value = json.getBoolean((String) key);
            } catch (JSONException e) {
                value = json.get((String) key);
            }
            Class keyClass = json.isNull(key.toString()) ? String.class : guessValueClass(value);
                        
            switch (keyClass.getName()) {
                case "java.lang.Double":
                    try {
                        bean.put(key, ((BigDecimal) value).doubleValue());
                    } catch (IllegalArgumentException e) {}
                    break;
                case "java.lang.Boolean":
                    try {
                        bean.put(key,(Boolean) value);
                    } catch (IllegalArgumentException e) {}
                    break;
                case "java.util.Date":
                    try {
                        String[] acceptedFormats = { "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ss.SSS" };
                        bean.put(key, DateUtils.parseDate(value.toString(), acceptedFormats));
                    } catch (IllegalArgumentException e) {} catch (ParseException e) {}
                    break;
                case "org.springframework.cglib.beans.BeanMap":
                    try {
                        bean.put(key, generateBean((JSONObject) value));
                    } catch (IllegalArgumentException e) {}
                    break;
                case "java.util.List":
                    if (((JSONArray) value).length() >0 ){
                        Class firstElemClass = guessValueClass((((JSONArray) value).get(0)));
                        
                        List<Object> results = new ArrayList<>(((JSONArray) value).length());
                        for (int i = 0, size = ((JSONArray) value).length(); i < size; i++) {
                            results.add(
                                (
                                    ((JSONArray) value).get(i) instanceof JSONObject
                                    ? (BeanMap) generateBean( (JSONObject) ((JSONArray) value).get(i) )
                                    : ((firstElemClass) ((JSONArray) value).get(i))
                                )
                            );
                        }
                        bean.put(key, results);
                    } else{
                        List<Object> emptyArray = new ArrayList<>();
                        bean.put(key, emptyArray);
                    }

                    //bean.put(key, value);
                    break;
                default:
                    bean.put(key, json.isNull(key.toString()) ? (String) null : value);
                    break;
            }
        }
    
        return bean;
    }
    
    /**
     * TODO fix guess
     */
    static Class guessValueClass(Object value) {

    //Guess Int
        try {
            Integer.parseInt(value.toString());
            return Integer.class;
        } catch (NumberFormatException e1) {}
    //Guess Float/Double
        try {
            Double.parseDouble(value.toString());
            return Double.class;
        } catch (NumberFormatException e1) {}
    //Guess timestamp [With/Without] timezone
        try {
            String[] acceptedFormats = { "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ss.SSS" };
            DateUtils.parseDate(value.toString(), acceptedFormats);
            return java.util.Date.class;
        } catch (ParseException e) {}
    //Guess Boolean
        if ( value instanceof Boolean ) {
            return Boolean.class;
        }
    //Guess recursive/nested object     
        if ( value instanceof JSONObject ) {
           return BeanMap.class;
        }
        if ( value instanceof JSONArray ) {
            return List.class;
        }
    //No special types, then it might just be a string
        return String.class;
    }
}