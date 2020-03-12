package com.perenc.xh.commonUtils.utils.properties;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 公司名称：贵阳赢销网科技有限公司
 * 
 * 功能描述： 集成Spring属性文件加载工具
 * 
 * 创建人：DengXin
 * 
 * 创建时间：2015-9-9 上午11:39:01
 */
public class PropertiesGetValue extends PropertyPlaceholderConfigurer {
	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
	private static Map<String, Object> propertiesMap;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }


    @Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);

		propertiesMap = new HashMap<String, Object>();

		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
			propertiesMap.put(keyStr, value);
		}
	}

	/**
	 * 获取系统消息内容
	 * 
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		return MapUtils.getString(propertiesMap, name);
	}

	/**
	 * 获取系统消息内容,存在置换文字的情况
	 * 
	 * @param name
	 * @param replaseStr
	 * @return
	 */
	public static String getContextProperty(String name, String... replaseStr) {

		String value = "";
		// 获取消息文件中内容
		if (propertiesMap.get(name) instanceof String) {
			value = propertiesMap.get(name).toString();
		}

		// 替换其中需要置换的内容
		for (int i = 0; i < replaseStr.length; i++) {
			if (!"".equals(value)) {
				value.replaceAll("{" + i + "}", replaseStr[i]);
			}
		}
		return value;
	}

	/**
	 * 置换文字,获取属性文件值
	 * 
	 * @param name
	 * @param replaseStr
	 * @return
	 */
	public static String getContestChange(String name, String... replaseStr) {
		String value = "";
		// 获取消息文件中内容
		if (propertiesMap.get(name) instanceof String) {
			value = propertiesMap.get(name).toString();
		}

		// 替换其中需要置换的内容
		for (int i = 0; i < replaseStr.length; i++) {
			if (!"".equals(value)) {
				value = value.replaceAll("{" + i + "}", replaseStr[i]);
			}
		}
		return value;
	}


}
