package com.perenc.xh.commonUtils.utils.wxUtil;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * JSON工具类
 * 转对象为json，或解析json为对象
 * @author Edward 2016-06-16
 */
public class JsonUtil {
	/**
	 * 转对象为JSON
	 * @param obj
	 */
	public static String toJson(Object obj) {
		if(obj == null) {
			return "null";
		}
		return new JSONObject(obj).toString();
	}
	
	/**
	 * 转map为JSON
	 * @param map
	 */
	public static String toJson(Map<?,?> map) {
		if(map == null) {
			return "null";
		}
		return new JSONObject(map).toString();
	}
	
	/**
	 * 转list为JSON
	 * @param coll
	 */
	public static String toJson(Collection<?> coll) {
		if(coll == null) {
			return "null";
		}
		return new JSONArray(coll).toString();
	}
	
	/**
	 * 解析json为map
	 * @param json
	 */
	public static Map<String,Object> parseMap(String json) {
		return JOToMap(new JSONObject(json));
	}
	
	/**
	 * 解析json为实体类
	 * @param json
	 * @param clz
	 */
	public static <T> T parsePojo(String json, Class<T> clz) throws Exception {
		T pojo = clz.newInstance();
		BaseUtil.populate(pojo, parseMap(json));
		return pojo;
	}
	
	/**
	 * 解析json为 map list
	 * @param json
	 */
	public static List<Map<String,Object>> parseMapList(String json) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		JSONArray ja = new JSONArray(json);
		for(int i=0; i<ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			list.add(JOToMap(jo));
		}
		return list;
	}
	
	/**
	 * 解析json为实体类list
	 * @param json
	 * @param clz
	 */
	public static <T> List<T> parsePojoList(String json, Class<T> clz) throws Exception {
		List<T> list = new ArrayList<T>();
		JSONArray ja = new JSONArray(json);
		for(int i=0; i<ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			list.add(JOToPojo(jo, clz));
		}
		return list;
	}
	
	/**
	 * 解析json为基础数据类型的list
	 * @param json
	 * @param clz
	 */
	public static <T> List<T> parseBaseList(String json, Class<T> clz) {
		List<T> list = new ArrayList<T>();
		JSONArray ja = new JSONArray(json);
		for(int i=0; i<ja.length(); i++) {
			list.add(clz.cast(ja.get(i)));
		}
		return list;
	}
	
	//--------------private methods-------------------
	/**
	 * 转JSONObject为map
	 */
	private static Map<String,Object> JOToMap(JSONObject jo) {
		Map<String,Object> map = new HashMap<String,Object>();
		for(Object key : jo.keySet()) {
			map.put(key.toString(), jo.get(key.toString()));
		}
		return map;
	}
	
	/**
	 * 转JSONObject为实体类
	 * @param jo
	 * @param clz
	 */
	private static <T> T JOToPojo(JSONObject jo, Class<T> clz) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		for(Object key : jo.keySet()) {
			map.put(key.toString(), jo.get(key.toString()));
		}
		T pojo = clz.newInstance();
		BaseUtil.populate(pojo, map);
		return pojo;
	}
	
}
