package com.perenc.xh.commonUtils.utils.wxUtil;

import com.perenc.xh.commonUtils.utils.StringOrDate.XhrtStringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.*;

public class XmlUtil {

	private static final Logger logger = Logger.getLogger(XmlUtil.class);
	/**
	 * 将MAP转化为xml
	 */
	public static String mapToXml(Map<String, Object> map, String alias) {
		StringBuffer xml = new StringBuffer("<" + alias + ">");
		if(map != null){
			Set<String> keySet = map.keySet();
			List<String> list = new ArrayList<String>(keySet);
			Collections.sort(list);
			for(String key : list){
				if("class".equals(key)) {
					continue;
				}
				Object value = map.get(key);
				if(value != null && !"".equals(value.toString())) {
					xml.append("<" + key + ">"  + value.toString() + "</" + key + ">");
				}
			}
		}
		xml.append("</" + alias + ">");
		return xml.toString();
	}
	
	/**
	 * 将POJO转化为xml
	 */
	public static String pojoToXml(Object bean, String alias) {
		Map<String, Object> map = BaseUtil.describe(bean);
		return XmlUtil.mapToXml(map, alias);
	}
	
	/**
	 * 将xml转化为map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> xmlToMap(String xml) {

//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		String FEATURE = null;
//
//			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
//			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
//			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
//		try {
//			dbf.setFeature(FEATURE, true);
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}

		Document doc = null;
		SAXReader reader = new SAXReader();
		try {
			reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
			InputSource source = new InputSource(new StringReader(xml));
			source.setEncoding(xml);
			doc = reader.read(source);

//		try {
//			Document doc = DocumentHelper.parseText(xml);
			Element rootEle = doc.getRootElement();
			Map<String,String> map = new HashMap<String,String>();
			for(Iterator<Element> it = rootEle.elementIterator(); it.hasNext();) {
				Element ele = it.next();
				if(XhrtStringUtils.isNotEmpty(ele.getText())){
					String name = ele.getName();
					String value = ele.getText();
					map.put(name, value);
				}
			}
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * xml转成map
	 * 支持多层
	 * @param xmlstr xml报文
	 * @return
	 */
	public static Map<String, Object> xmlToMap2(String xmlstr){
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlstr);
		} catch (DocumentException e) {
			//logger.error("parse text error : " + e);
		}
		Element rootElement = doc.getRootElement();
		Map<String,Object> mapXml = new HashMap<String,Object>();
		elementToMap(mapXml,rootElement);
		return mapXml;
	}



	/**
	 * 遍历子节点
	 * @param map
	 * @param rootElement
	 */
	public static void elementToMap(Map<String, Object> map, Element rootElement){
		//获得当前节点的子节点
		List<Element> elements = rootElement.elements();
		Map<String,Object> childMap = new HashMap<String,Object>();
		//如果还存在子节点，就考虑list情况，继续递归
		for (Element element : elements) {
			List<Element> es = element.elements();
			if(es.size()>0){
				//获取当前节点下的子节点
				ArrayList<Map> list = new ArrayList<Map>();
				for(Element e:es){
					elementChildToList(list,e);
				}
				map.put(element.getName(), list);
			}else{
				map.put(element.getName(),element.getText());
			}

		}
	}



	/**
	 * 递归子节点
	 * @param arrayList
	 * @param rootElement
	 */
	public static void elementChildToList(ArrayList<Map> arrayList, Element rootElement){
		//获得当前节点的子节点
		List<Element> elements = rootElement.elements();
		if(elements.size()>0){
			ArrayList<Map> list = new ArrayList<Map>();
			Map<String,Object> sameTempMap = new HashMap<String,Object>();
			for(Element element:elements){
				elementChildToList(list,element);
				sameTempMap.put(element.getName(), element.getText());
			}
			System.out.println(elements.size());
			arrayList.add(sameTempMap);
		}

	}

	/**
	 * 将XML转化为pojo
	 */
	public static <T> T xmlToPojo(String xml, Class<T> clz){
		try {
			T pojo = clz.newInstance();
			Map<?, ?> map = XmlUtil.xmlToMap(xml);
			BaseUtil.populate(pojo, map);
			return pojo;
		} catch (Exception e) {
			return null;
		}
	}
}
