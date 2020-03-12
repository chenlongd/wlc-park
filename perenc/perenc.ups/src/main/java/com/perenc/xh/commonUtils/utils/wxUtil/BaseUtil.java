package com.perenc.xh.commonUtils.utils.wxUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类的基础类
 * @author Edward
 */
public class BaseUtil {
	/**
	 * 解析pojo为map
	 * @param pojo
	 */
	public static Map<String,Object> describe(Object pojo) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		Field[] fields = pojo.getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				Method get = BaseUtil.getGetFunc(pojo.getClass(), field.getName());
				Object val = get.invoke(pojo);
				if(val != null) {
					map.put(field.getName(), val);
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		Field[] superFields = pojo.getClass().getSuperclass().getDeclaredFields();
		for(Field field : superFields) {
			try {
				Method get = BaseUtil.getGetFunc(pojo.getClass().getSuperclass(), field.getName());
				Object val = get.invoke(pojo);
				if(val != null) {
					map.put(field.getName(), val);
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		return map;
	}
	
	public static void populate(Object pojo, Map<?,?> map) {
		for(Object key : map.keySet()) {
			Object value = map.get(key);
			
			Method set = null;
			try {
				Field field = null;
				try {
					field = pojo.getClass().getDeclaredField(key.toString());
				} catch (NoSuchFieldException e) {
					field = pojo.getClass().getSuperclass().getDeclaredField(key.toString());
				}
				if(field == null) {
					
				}
				if(field == null) {
					continue;
				}
				
				if(field.getType().equals(value.getClass())) {//类型符合
					set = BaseUtil.getSetFunc(pojo.getClass(), key.toString(), value.getClass());
					if(set == null) {
						set = BaseUtil.getSetFunc(pojo.getClass().getSuperclass(), key.toString(), value.getClass());
					}
					set.invoke(pojo, value);
				} else {//类型不符
					//兼容性
					if(field.getType().equals(Integer.class) && value instanceof String) {//string 兼容 Integer
						Integer val = Integer.parseInt((String)value);
						set = BaseUtil.getSetFunc(pojo.getClass(), key.toString(), Integer.class);
						if(set == null) {
							set = BaseUtil.getSetFunc(pojo.getClass().getSuperclass(), key.toString(), Integer.class);
						}
						if(set != null) {
							set.invoke(pojo, val);
						}
					} else if(field.getType().equals(Double.class) && value instanceof String) {//string 兼容 Double
						Double val = Double.parseDouble((String)value);
						set = BaseUtil.getSetFunc(pojo.getClass(), key.toString(), Double.class);
						if(set == null) {
							set = BaseUtil.getSetFunc(pojo.getClass().getSuperclass(), key.toString(), Double.class);
						}
						if(set != null) {
							set.invoke(pojo, val);
						}
					} else if(field.getType().equals(Boolean.class) && value instanceof String) {//string 兼容 Boolean
						Boolean val = Boolean.parseBoolean((String)value);
						set = BaseUtil.getSetFunc(pojo.getClass(), key.toString(), Boolean.class);
						if(set == null) {
							set = BaseUtil.getSetFunc(pojo.getClass().getSuperclass(), key.toString(), Boolean.class);
						}
						if(set != null) {
							set.invoke(pojo, val);
						}
					}
				}
			}  catch (Exception e) {
				
			}
		}
	}
	
	public static Method getSetFunc(Class<?> pojoClz, String propName, Class<?> propClz) {
		String funcName = "set" + propName.substring(0,1).toUpperCase() + propName.substring(1);
		try {
			return pojoClz.getDeclaredMethod(funcName, propClz);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Method getGetFunc(Class<?> pojoClz, String propName) {
		String funcName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1);
		try {
			return pojoClz.getDeclaredMethod(funcName);
		} catch (Exception e) {
			return null;
		}
	}
}
