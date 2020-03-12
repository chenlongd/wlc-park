package com.perenc.xh.commonUtils.DBRelevant;

import javax.persistence.Transient;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据库访问相关工具
 * 
 * @author Edward
 *
 */
public class DBUtil implements Serializable{
	private static final long serialVersionUID = -271501230914052630L;
	private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static PropertyDescriptor[] propertyDescriptors(Class<?> c) {
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);

		} catch (IntrospectionException e) {
			e.printStackTrace();
			// throw new SQLException("Bean introspection failed: " +
			// e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}

	private static boolean isCompatibleType(Class<?> type) {
		// Do object check first, then primitives
	 
		if (type.equals(Integer.TYPE)||type.getClass().isInstance(Integer.class)) {
			return true;
		} else if (type.equals(Long.TYPE)||type.getClass().isInstance(Long.class)) {
			return true;

		} else if (type.equals(Double.TYPE)||type.getClass().isInstance(Double.class)) {
			return true;

		} else if (type.equals(Float.TYPE)||type.getClass().isInstance(Float.class)) {
			return true;

		} else if (type.equals(Short.TYPE)||type.getClass().isInstance(Short.class)) {
			return true;

		} else if (type.equals(Byte.TYPE)||type.getClass().isInstance(Byte.class)) {
			return true;

		} else if (type.equals(Character.TYPE)||type.getClass().isInstance(Character.class)) {
			return true;

		} else if (type.equals(Boolean.TYPE)||type.getClass().isInstance(Boolean.class)) {
			return true;
		}
		return false;

	}
	
 
	
	
	/**
	 * 转成插入参数
	 * @param <T>
	 * @param pojo 实体对象
	 */
	public static <T> InsertParam toInsertParam(T pojo) {
		String now = fmt.format(new Date());
		List<Property> properties = new ArrayList<Property>();

		List<Field> fields = new ArrayList<Field>();

		Class cls = pojo.getClass();
		for(;cls!=null;){
			Field[] fieldArr1 = cls.getDeclaredFields();
			for(Field f : fieldArr1) {
				fields.add(f);
			}
			cls = cls.getSuperclass();
		}


		for(Field field : fields) {//遍历各属性
			try {

				//去除静态变量
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				//跳过部分属性
				String fName = field.getName();
				if(fName.startsWith("_")) {
					continue;
				}
				//
				Transient f = field.getAnnotation(Transient.class);
				if(f!=null){
					continue;
				}

				Method get = null;
				try {
					get = pojo.getClass().getDeclaredMethod(toGetFuncName(fName));
				} catch (NoSuchMethodException e) {
					get = pojo.getClass().getSuperclass().getDeclaredMethod(toGetFuncName(fName));
				}
				//得到 返回类型
				Class<?> params = get.getReturnType();

				final String targetType = params.getName();

				boolean readType = false;
				if ("java.lang.String".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Date".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Time".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Timestamp".equals(targetType)) {
					readType = true;
				} else if (params.isEnum()) {
				}
				// Don't call setter if the value object isn't the right type
				if (readType || isCompatibleType(params)) {
					Object value = get.invoke(pojo);

					if(value != null) {//若有值，则赋值
						properties.add(new Property(fName, value));

					} else {//若值为空，则设置默认值
						if(fName.equalsIgnoreCase("createTime")) {//特殊处理：创建时间为当前
							properties.add(new Property(fName, now));

						} else if(fName.equalsIgnoreCase("updateTime")) {//特殊处理：更新时间为当前
							properties.add(new Property(fName, now));

						}
					}
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return new InsertParam(properties, "0");
	}

	/**
	 * 转成插入参数
	 *
	 * @param <T>
	 * @param pojo
	 *            实体对象
	 */
	public static <T> InsertParam toInsertParamV2(T pojo) {
		String now = fmt.format(new Date());
		List<Property> properties = new ArrayList<Property>();

		Class cls = pojo.getClass();

		PropertyDescriptor[] props = propertyDescriptors(cls);

		for (PropertyDescriptor prop : props) {
			Class<?> propType = prop.getPropertyType();

			// 布尔，字节，字符，短整型，长，浮点和双精度
			// if (!propType.isPrimitive()) {
			// continue;//return null;
			// }
			// 跳过部分属性
			String fName = prop.getName();
			if ("class".equalsIgnoreCase(fName)||fName.startsWith("_")) {
				continue;
			}
			// prop.getReadMethod();
			Method setter = prop.getReadMethod();

			if (setter == null) {
				continue;
			}

			Class<?> params = setter.getReturnType();// getParameterTypes();
			try {
				// convert types for some popular ones


				final String targetType = params.getName();

				boolean readType = false;
				if ("java.lang.String".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Date".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Time".equals(targetType)) {
					readType = true;
				} else if ("java.sql.Timestamp".equals(targetType)) {
					readType = true;
				} else if (params.isEnum()) {
				}
				// Don't call setter if the value object isn't the right type
				if (readType || isCompatibleType(params)) {
					// setter.invoke(target, new Object[]{value});
					Object value = setter.invoke(pojo);

					if (value != null) {// 若有值，则赋值
						properties.add(new Property(fName, value));

					} else {// 若值为空，则设置默认值
						if (fName.equalsIgnoreCase("createTime")) {// 特殊处理：创建时间为当前
							properties.add(new Property(fName, now));
						}
//						else if (fName.equalsIgnoreCase("updateTime")) {// 特殊处理：更新时间为当前
//							properties.add(new Property(fName, now));
//						}
					}
				} else {

				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		// List<Field> fields = new ArrayList<Field>();
		// for (; cls != null; cls = cls.getSuperclass()) {
		//
		// Field[] fieldArr1 = cls.getDeclaredFields();
		// for (Field f : fieldArr1) {
		// fields.add(f);
		// }
		// }
		//
		// for (Field field : fields) {// 遍历各属性
		// try {
		// // 跳过部分属性
		// String fName = field.getName();
		// if (fName.startsWith("_")) {
		// continue;
		// }
		//
		// // Method get = null;
		// // try {
		// // get =
		// // pojo.getClass().getDeclaredMethod(toGetFuncName(fName));
		// // } catch (NoSuchMethodException e) {
		// // get =
		// //
		// pojo.getClass().getSuperclass().getDeclaredMethod(toGetFuncName(fName));
		// // }
		//
		// Class methodCls = pojo.getClass();
		// Method get = null;
		//
		// for (; methodCls != null;) {
		// try {
		// get = methodCls.getDeclaredMethod(toGetFuncName(field.getName()));
		// break;
		// } catch (NoSuchMethodException e) {
		// methodCls = methodCls.getSuperclass();
		// }
		// }
		//
		// Object value = get.invoke(pojo);
		//
		// if (value != null) {// 若有值，则赋值
		// properties.add(new Property(fName, value));
		//
		// } else {// 若值为空，则设置默认值
		// if (fName.equalsIgnoreCase("createTime")) {// 特殊处理：创建时间为当前
		// properties.add(new Property(fName, now));
		// } else if (fName.equalsIgnoreCase("updateTime")) {// 特殊处理：更新时间为当前
		// properties.add(new Property(fName, now));
		//
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		return new InsertParam(properties, "0");
	}

	// private void callSetter(Object target, PropertyDescriptor prop, Object
	// value) throws SQLException {
	//
	// Method setter = prop.getWriteMethod();
	//
	// if (setter == null) {
	// return;
	// }
	//
	// Class<?>[] params = setter.getParameterTypes();
	// try {
	// // convert types for some popular ones
	// if (value instanceof java.util.Date) {
	// final String targetType = params[0].getName();
	// if ("java.sql.Date".equals(targetType)) {
	// value = new java.sql.Date(((java.util.Date) value).getTime());
	// } else if ("java.sql.Time".equals(targetType)) {
	// value = new java.sql.Time(((java.util.Date) value).getTime());
	// } else if ("java.sql.Timestamp".equals(targetType)) {
	// Timestamp tsValue = (Timestamp) value;
	// int nanos = tsValue.getNanos();
	// value = new java.sql.Timestamp(tsValue.getTime());
	// ((Timestamp) value).setNanos(nanos);
	// }
	// } else if (value instanceof String && params[0].isEnum()) {
	// value = Enum.valueOf(params[0].asSubclass(Enum.class), (String) value);
	// }
	//
	// // Don't call setter if the value object isn't the right type
	// if (this.isCompatibleType(value, params[0])) {
	// setter.invoke(target, new Object[] { value });
	// } else {
	// throw new SQLException("Cannot set " + prop.getName() + ": incompatible
	// types, cannot convert "
	// + value.getClass().getName() + " to " + params[0].getName());
	// // value cannot be null here because isCompatibleType allows
	// // null
	// }
	//
	// } catch (IllegalArgumentException e) {
	// throw new SQLException("Cannot set " + prop.getName() + ": " +
	// e.getMessage());
	//
	// } catch (IllegalAccessException e) {
	// throw new SQLException("Cannot set " + prop.getName() + ": " +
	// e.getMessage());
	//
	// } catch (InvocationTargetException e) {
	// throw new SQLException("Cannot set " + prop.getName() + ": " +
	// e.getMessage());
	// }
	// }

	/**
	 * 转成update参数
	 *
	 * @param <T>
	 * @param pojo
	 *            实体对象
	 * @param priKeys
	 *            主键名称（如:id,复合主键 情况下可传多个）
	 * @return
	 */
	public static <T> UpdateParam toUpdateParam(T pojo, String... priKeys) {
		UpdateParam result = new UpdateParam();
		List<Property> properties = new ArrayList<Property>();
		result.setProperties(properties);
		List<Property> oPriKeys = new ArrayList<Property>();
		result.setPriKeys(oPriKeys);


		List<Field> fields = new ArrayList<Field>();

		Class cls = pojo.getClass();
		for(;cls!=null;){
			Field[] fieldArr1 = cls.getDeclaredFields();
			for(Field f : fieldArr1) {
				fields.add(f);
			}
			cls = cls.getSuperclass();
		}

		for (Field field : fields) {// 遍历各属性
			try {
				// 跳过部分属性
				String fName = field.getName();
				if (fName.startsWith("_") || "createTime".equalsIgnoreCase(fName)) {
					continue;
				}

				//去除静态变量
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}

				//
				Transient f = field.getAnnotation(Transient.class);
				if(f!=null){
					continue;
				}

				Class methodCls = pojo.getClass();
				Method get = null;

				for (; methodCls != null;) {
					try {
						get = methodCls.getDeclaredMethod(toGetFuncName(field.getName()));
						break;
					} catch (NoSuchMethodException e) {
						methodCls = methodCls.getSuperclass();
					}
				}


				if (exist(priKeys, field.getName())) {// 是主键
					Object value = get.invoke(pojo);
					if (value == null) {
						throw new Exception("主键值为空");
					}
					oPriKeys.add(new Property(field.getName(), value));
				} else {// 是要修改的字段
					Object value = get.invoke(pojo);
					if (value != null) {
						properties.add(new Property(field.getName(), value));
					} else {
						if ("updateTime".equalsIgnoreCase(field.getName())) {// 特殊处理：设置修改时间为当前
							properties.add(new Property(field.getName(), fmt.format(new Date())));
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
 

	/**
	 * 属性转为get方法名
	 * 
	 * @param fieldName
	 * @return
	 */
	private static String toGetFuncName(String fieldName) {
		String result = null;
		if (fieldName != null && fieldName.length() >= 1) {
			result = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
		return result;
	}

	/**
	 * 查某字符串数组中是否包含某字符串
	 * 
	 * @param arr
	 * @param v
	 * @return
	 */
	private static Boolean exist(String[] arr, String v) {
		Boolean b = false;
		for (String str : arr) {
			if (str.equals(v)) {
				b = true;
				break;
			}
		}
		return b;
	}

}
