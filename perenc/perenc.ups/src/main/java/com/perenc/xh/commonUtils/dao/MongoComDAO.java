package com.perenc.xh.commonUtils.dao;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * 
 * 功能描述：mongo 数据库操作通用类
 *
 */
public interface MongoComDAO {

	/**
	 * 获取该集合的所有对象
	 * 
	 * @return
	 */
	<T> List<T> executeForObjectAll(Class<?> cls);

	/**
	 * 获取该集合的所有对象,带排序参数
	 * 
	 * @return
	 */
	<T> List<T> executeForObjectAll(Class<?> cls, List<Order> order);

	/**
	 * 获取该集合的所有对象 返回对象映射
	 * 
	 * @return
	 */
	<T> List<T> executeForObjectAll(String collection, Class<?> cls);

	/**
	 * 获取该集合的所有对象 返回对象映射,带排序参数
	 * 
	 * @return
	 */
	<T> List<T> executeForObjectAll(String collection, Class<?> cls, List<Order> order);

	/**
	 * 多条数据查询
	 * 
	 * @param map
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls);

	/**
	 * 多条数据查询,带排序参数
	 * 
	 * @param map
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, List<Order> order);

	/**
	 * 多条数据查询 返回对象映射
	 * 
	 * @param map
	 * @param cls
	 * @param collection
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, String collection, Class<?> cls);

	/**
	 * 多条数据查询 返回对象映射,带排序参数
	 * 
	 * @param map
	 * @param cls
	 * @param collection
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, String collection, Class<?> cls, List<Order> order);

	/**
	 * 多条数据查询
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls);

	/**
	 * 多条数据查询,带排序参数
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, List<Order> order);

	/**
	 * 多条数据查询
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls);

	/**
	 * 多条数据查询 ,带排序参数
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, List<Order> order);

	/**
	 * 特殊条件,多条数据查询
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, PageHelper page);

	/**
	 * 特殊条件,多条数据查询,带排序参数
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, PageHelper page, List<Order> order);

	/**
	 * 特殊条件,多条数据查询
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, PageHelper page);

	/**
	 * 特殊条件,多条数据查询,排序参数
	 * 
	 * @param criterias
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, PageHelper page,
                                     List<Order> order);

	/**
	 * 特殊条件,多条数据分页查询
	 *
	 * @param map
	 * @param cls
	 * @param page
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, PageHelper page);

	/**
	 * 特殊条件,多条数据分页查询
	 *
	 * @param map
	 * @param cls
	 * @param page
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, PageHelper page, List<Order> order);

	/**
	 * 特殊条件,多条数据分页查询
	 *
	 * @param map
	 * @param cls
	 * @param page
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, Map<String, Object> map, Class<?> cls, PageHelper page);

	/**
	 * 特殊条件,多条数据分页查询
	 *
	 * @param map
	 * @param cls
	 * @param page
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, Map<String, Object> map, Class<?> cls, PageHelper page,
                                     List<Order> order);

	/**
	 * 多条数据查询总条数
	 *
	 * @param map
	 * @param cls
	 * @return
	 */
	long executeForObjectListCount(Map<String, Object> map, Class<?> cls);

	/**
	 * 多条数据查询总条数
	 *
	 * @param map
	 * @param cls
	 * @param regx
	 * @return
	 */
	long executeForObjectListCount(Map<String, Object> map, Class<?> cls, Map<String, Object> regx);

	/**
	 * 多条数据查询总条数(其他条件参数)
	 *
	 * @param criterias
	 * @param cls
	 * @return
	 */
	long executeForObjectListCount(List<Criteria> criterias, Class<?> cls);

	/**
    * 求数据总和
    * @param query
    * @return
    */
	long executeForObjectListCount(Query query, Class<?> cls);

	/**
	 * 条件数据查询
	 *
	 * @param query
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(Query query, Class<?> cls);

	/**
	 * 条件数据查询
	 *
	 * @param collection
	 * @param query
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(String collection, Query query, Class<?> cls);

	/**
	 * 多条件模糊查询--分页带排序查询
	 *
	 * @param map
	 *            普通查询条件
	 * @param regex
	 *            模糊查询条件
	 * @param cls
	 * @param order
	 *            排序
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Map<String, Object> regex, PageHelper page, Class<?> cls,
                                     List<Order> order);

	/**
	 * 多条件模糊查询--分页不排序查询
	 * 
	 * @param map
	 *            普通查询条件
	 * @param regex
	 *            模糊查询条件
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectList(Map<String, Object> map, Map<String, Object> regex, PageHelper page, Class<?> cls);


	/**
	 * 多条件模糊查询--分页不排序查询
	 *
	 * @param agg
	 *            普通查询条件
	 * @param claName
	 *            标识类
	 * @param cls
	 * @return
	 */
	<T> List<T> executeForObjectAggregateList(Aggregation agg, String claName, Class<?> cls);

	/**
	 * 去掉某个重复，统计总条数
	 * @param query
	 * @param claName
	 * @param fieldName
	 * @param cls
	 * @return
	 */
	long executeForObjectDistinctCount(DBObject query, String claName, String fieldName, Class<?> cls);

	/**
	 * 增加数据有参
	 *
	 * @return
	 */
	int executeInsert(Object obj) throws Exception;

	/**
	 * 批量增加数据
	 * 
	 * @param cls
	 * @param listParam
	 * @return
	 */
	int executeInserts(List<?> listParam, Class<?> cls) throws Exception;

	/**
	 * 有参更新数据
	 * 
	 * @param condition
	 * @param cls
	 */
	int executeUpdate(Map<String, Object> condition, Map<String, Object> Content, Class<?> cls) throws Exception;

	/**
	 * 有参数更新 条例大于,小于
	 * 批量更改
	 * @param criterias
	 * @param content
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(List<Criteria> criterias, Map<String, Object> content, Class<?> cls) throws Exception;

	/**
	 * 有参更新数据,并返回更新后的内容
	 *
	 * @param condition
	 * @param cls
	 */
	int executeUpdate(Map<String, Object> condition, Map<String, Object> Content, Object cls) throws Exception;

	/**
	 * 更新数据
	 * 
	 * @param cls
	 * @throws Exception
	 */
	int executeUpdate(Object cls) throws Exception;

	/**
	 * 可以更改某些字段为空
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public int executeUpdateEmpty(Object cls) throws Exception;

	/**
	 * 级联数据维护————相关数据全部更新（针对表之间的关联是数组类型）
	 * 
	 * @param content
	 * @param cls
	 * @throws Exception
	 */
	int executeUpdateAll(Map<String, Object> content, Class<?> cls) throws Exception;

	/**
	 * 根据多个Id更改数据
	 * @param content
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	int executeUpdateByIds(Map<String, Object> content, Class<?> cls) throws Exception;

	/**
	 * 删除语句执行
	 * 
	 * @param map
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	int executeDelete(Map<String, Object> map, Class<?> cls) throws Exception;

	/**
	 * 根据时间删除
	 * @param criterias
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	 int executeDelete(List<Criteria> criterias, Class<?> cls) throws Exception;

	/**
	 * 根据id获取记录
	 * 
	 * @param id
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	<T> T executeForObjectById(String id, Class<?> cls) throws Exception;

	/**
	 * 根据id获取记录
	 * 
	 * @param id
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	<T> T executeForObjectById(String collection, String id, Class<?> cls) throws Exception;

	/**
	 * 条件获取单条记录
	 * 
	 * @param condition
	 * @param cls
	 * @return
	 */
	<T> T executeForObjectByCon(Map<String, Object> condition, Class<T> cls);

	/**
	 * 条件获取单条记录
	 * 
	 * @param condition
	 * @param cls
	 * @return
	 */
	<T> T executeForObjectByCon(String collection, Map<String, Object> condition, Class<T> cls);

	/**
	 * 执行命令返回结果
	 * 
	 * @param command
	 * @return
	 */
	public CommandResult executeCommand(String command);

	/**
	 * 获取mongo模板对象
	 * 
	 * @return
	 */
	public MongoTemplate getMongoTemplate();
}
