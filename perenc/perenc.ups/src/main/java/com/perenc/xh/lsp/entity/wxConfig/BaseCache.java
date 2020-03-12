package com.perenc.xh.lsp.entity.wxConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单缓存
 * @author Edward
 *
 */
public class BaseCache {
	//-----单例模式
	protected BaseCache() {
	}
	//-----end
	
	private Map<String,Object> valuePool = new HashMap<String,Object>();
	private Map<String,Long> expTimePool = new HashMap<String,Long>();
	private Map<String,Long> updateTimePool = new HashMap<String,Long>();
	
	/**
	 * 取值
	 * @param key
	 */
	public Object get(String key) {
		return valuePool.get(key);
	}
	public String getString(String key) {
		return (String)get(key);
	}
	public Integer getInt(String key) {
		return (Integer)get(key);
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> getModel(String key) {
		return (Map<String, Object>)get(key);
	}
	
	/**
	 * 设值
	 * @param key
	 * @param value
	 * @param expTimeInSec 过期秒数，null表示永不过期
	 */
	public void set(String key, Object value, Long expTimeInSec) {
		if(expTimeInSec != null) {
			expTimeInSec = expTimeInSec*1000;
		}
		valuePool.put(key, value);
		expTimePool.put(key, expTimeInSec);
		updateTimePool.put(key, System.currentTimeMillis());
	}
	
	/**
	 * 判断是否过期
	 * @param key
	 */
	public boolean isExp(String key) {
//		if(5>4){
//			return true;
//		}
		if(valuePool.get(key) == null) {
			return true;
		}
		
		if(expTimePool.get(key) == null) {//不存在的情况下当作不过期
			return false;
		}
		Long now = System.currentTimeMillis();
		return now > expTimePool.get(key) + updateTimePool.get(key);
	}
	
	/**
	 * 使过期
	 * @param key
	 */
	public void makeExp(String key) {
		expTimePool.put(key, 0L);
	}
}
