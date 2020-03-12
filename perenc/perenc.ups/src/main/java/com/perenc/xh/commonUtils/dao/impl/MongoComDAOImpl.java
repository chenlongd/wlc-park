package com.perenc.xh.commonUtils.dao.impl;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能描述： mongo 数据持久层操作实现类
 *
 */
@Repository("mongoDAOImpl")
public class MongoComDAOImpl implements MongoComDAO {

	private static final String NETWORK_EOF = "com.mongodb.MongoException$Network: Read operation to server";
	/**
	 * Log
	 */
	private static Log LOGGER = LogFactory.getLog(MongoComDAOImpl.class);

	/**
	 * mongo 模板对象
	 */
	private MongoTemplate mongoTemplate;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public <T> List<T> executeForObjectAll(Class<?> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.findAll(cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.findAll(cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectAll(Class<?> cls, List<Order> order) {
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Query query = new Query();
				Sort sort = new Sort(order);
				query.with(sort);
				mongoTemplate.find(query, cls);
			} else {
				list = (List<T>) mongoTemplate.findAll(cls);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.findAll(cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectAll(String collection, Class<?> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.findAll(cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.findAll(cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectAll(String collection, Class<?> cls, List<Order> order) {
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Query query = new Query();
				Sort sort = new Sort(order);
				query.with(sort);
				mongoTemplate.find(query, cls, collection);
			} else {
				list = (List<T>) mongoTemplate.findAll(cls, collection);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.findAll(cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条查询");
		List<T> list = new ArrayList<T>();
		Query query = query(map);
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				LOGGER.error(e.getMessage());
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条查询");
		List<T> list = new ArrayList<T>();
		Query query = query(map);
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, String collection, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条查询");
		Query query = query(map);
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, String collection, Class<?> cls, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条查询");
		Query query = query(map);
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, PageHelper page) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();

		Query query = query(map);
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Class<?> cls, PageHelper page, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();

		Query query = query(map);
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, Map<String, Object> map, Class<?> cls, PageHelper page) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();
		Query query = query(map);
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		try {
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, Map<String, Object> map, Class<?> cls, PageHelper page,
			List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();
		Query query = query(map);
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条条件查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条条件查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条条件查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条条件查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, PageHelper page) {
		LOGGER.debug(cls.getName() + "---多条条件分页查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());

		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(List<Criteria> criterias, Class<?> cls, PageHelper page, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条条件分页查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());

		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, PageHelper page) {
		LOGGER.debug(cls.getName() + "---多条条件分页查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, List<Criteria> criterias, Class<?> cls, PageHelper page,
			List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条条件分页查询查询");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());
		List<T> list = new ArrayList<T>();
		try {
			if (order != null && order.size() != 0) {
				Sort sort = new Sort(order);
				query.with(sort);
			}
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public long executeForObjectListCount(Map<String, Object> map, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---数量查询");
		Long count = 0L;
		try {
			count = mongoTemplate.count(query(map), cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count = mongoTemplate.count(query(map), cls);
			} else {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public long executeForObjectListCount(Map<String, Object> map, Class<?> cls, Map<String, Object> regx) {
		LOGGER.debug(cls.getName() + "---数量查询");
		Long count = 0L;

		Query query = new Query();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
		}

		for (Map.Entry<String, Object> entry : regx.entrySet()) {
			query.addCriteria(Criteria.where(entry.getKey()).regex(".*?" + entry.getValue() + ".*"));
		}

		try {
			count = mongoTemplate.count(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count = mongoTemplate.count(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public <T> List<T> executeForObjectList(Query query, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---条件查询");
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(String collection, Query query, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---条件查询");
		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) mongoTemplate.find(query, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls, collection);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int executeInsert(Object obj) throws Exception {
		LOGGER.debug(obj.getClass().getName() + "---对象数据插入");
		try {
			mongoTemplate.insert(obj);
		} catch (Exception e) {
			LOGGER.error("add error :" + e.getMessage());
			return 0;
		}
		return 1;
	}

	@Override
	public int executeInserts(List<?> listParam, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---批量插入");
		try {
			mongoTemplate.insert(listParam, cls);
		} catch (Exception e) {
			LOGGER.error("add error :" + e.getMessage());
			return 0;
		}
		return 1;
	}

	@Override
	public int executeUpdate(Map<String, Object> condition, Map<String, Object> content, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---数据更新");

		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			update.set(entry.getKey(), entry.getValue());
		}

		WriteResult r = mongoTemplate.updateMulti(query(condition), update, cls);
		return r == null ? 0 : r.getN();
	}

	/**
	 * 有参数更新 条例大于,小于
	 * 批量更改
	 * @param criterias
	 * @param content
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	@Override
	public int executeUpdate(List<Criteria> criterias, Map<String, Object> content, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---数据更新");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			update.set(entry.getKey(), entry.getValue());
		}

		WriteResult r = mongoTemplate.updateMulti(query, update, cls);
		return r == null ? 0 : r.getN();
	}

	@Override
	public int executeUpdate(Map<String, Object> condition, Map<String, Object> content, Object cls) throws Exception {
		LOGGER.debug(cls.getClass().getName() + "---数据更新");

		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			update.set(entry.getKey(), entry.getValue());
		}

		WriteResult r = mongoTemplate.updateMulti(query(condition), update, cls.getClass());
		return r == null ? 0 : r.getN();
	}

	@Override
	public int executeUpdate(Object cls) throws Exception {
		LOGGER.debug(cls.getClass().getName() + "---数据更新");
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> content = toMap(cls);
		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), "id")) {
				condition.put(entry.getKey(), entry.getValue());
			} else {
				if (entry.getValue() != null && !"".equals(entry.getValue())) {
					update.set(entry.getKey(), entry.getValue());
				}
			}
		}
		WriteResult r = mongoTemplate.updateMulti(query(condition), update, cls.getClass());
		return r == null ? 0 : r.getN();
	}

	/**
	 * 可以更改某些字段为空
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	@Override
	public int executeUpdateEmpty(Object cls) throws Exception {
		LOGGER.debug(cls.getClass().getName() + "---数据更新");
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> content = toMap(cls);
		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), "id")) {
				condition.put(entry.getKey(), entry.getValue());
			} else {
				update.set(entry.getKey(), entry.getValue());
			}
		}
		WriteResult r = mongoTemplate.updateMulti(query(condition), update, cls.getClass());
		return r == null ? 0 : r.getN();
	}

	@Override
	public int executeUpdateAll(Map<String, Object> content, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---级联更新/删除数据（针对表之间的关联是数组类型）");

		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			update.pull(entry.getKey(), entry.getValue());
		}
		WriteResult r = mongoTemplate.updateMulti(new Query(Criteria.where("id").ne(null)), update, cls);
		return r == null ? 0 : r.getN();
	}

	@Override
	public int executeUpdateByIds(Map<String, Object> content, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getClass().getName() + "---数据更新,根据多个id");
		Map<String, Object> condition = new HashMap<String, Object>();
		// 更新内容处理
		Update update = new Update();
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), "ids")) {
				condition.put(entry.getKey(), entry.getValue());
			} else {
				if (entry.getValue() != null && !"".equals(entry.getValue())) {
					update.set(entry.getKey(), entry.getValue());
				}
			}
		}
		String[] ids= (String[]) condition.get("ids");
		WriteResult r = mongoTemplate.updateMulti(new Query(Criteria.where("id").in(ids)), update, cls);
		return r == null ? 0 : r.getN();
	}

	@Override
	public int executeDelete(Map<String, Object> map, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---数据删除");
		Query query = query(map);
		WriteResult r = mongoTemplate.remove(query, cls);
		if (r == null) {
			return 0;
		}
		return r.getN();
	}

	/**
	 * 根据时间删除
	 * @param criterias
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	@Override
	public int executeDelete(List<Criteria> criterias, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---数据删除");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		WriteResult r = mongoTemplate.remove(query, cls);
		if (r == null) {
			return 0;
		}
		return r.getN();
	}

	@Override
	public <T> T executeForObjectById(String id, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---根据_id获取数据");
		try {
			return (T) mongoTemplate.findById(id, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				return (T) mongoTemplate.findById(id, cls);
			} else {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public <T> T executeForObjectById(String collection, String id, Class<?> cls) throws Exception {
		LOGGER.debug(cls.getName() + "---根据_id获取数据");
		try {
			return (T) mongoTemplate.findById(id, cls, collection);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				return (T) mongoTemplate.findById(id, cls, collection);
			} else {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public <T> T executeForObjectByCon(Map<String, Object> condition, Class<T> cls) {
		LOGGER.debug(cls.getName() + "---根据其他条件获取单条数据");
		Query query = query(condition);
		Long count = 0L;
		try {
			count = mongoTemplate.count(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count = mongoTemplate.count(query, cls);
			} else {
				e.printStackTrace();
			}
		}

		if (count < 1) {
			return null;
		}

		return mongoTemplate.findOne(query, cls);
	}

	@Override
	public <T> T executeForObjectByCon(String collection, Map<String, Object> condition, Class<T> cls) {
		LOGGER.debug(cls.getName() + "---根据其他条件获取单条数据");
		Query query = query(condition);
		Long count = 0L;
		try {
			count = mongoTemplate.count(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count = mongoTemplate.count(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		if (count < 1) {
			return null;
		}
		return mongoTemplate.findOne(query, cls, collection);
	}

	/**
	 * 封装查询参数
	 * 
	 * @param params
	 * @return 返回已经封装好的对象
	 */
	private static Query query(Map<String, Object> params) {
		Query query = new Query();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
		}
		return query;
	}

	private static Map<String, Object> toMap(Object javaBean) {
		Map<String, Object> result = new HashMap<>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	@Override
	public long executeForObjectListCount(List<Criteria> criterias, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条条件查询查询（条件列表查询）");
		Query query = new Query();
		for (Criteria c : criterias) {
			query.addCriteria(c);
		}
		try {
			return mongoTemplate.count(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				return mongoTemplate.count(query, cls);
			} else {
				e.printStackTrace();
				return 0;
			}
		}
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Map<String, Object> regex, PageHelper page,
			Class<?> cls, List<Order> order) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();

		Query query = new Query();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
			}
		}

		if (regex != null) {
			for (Map.Entry<String, Object> entry : regex.entrySet()) {
				query.addCriteria(Criteria.where(entry.getKey()).regex(".*?" + entry.getValue() + ".*"));
			}
		}

		if (order != null && order.size() != 0) {
			Sort sort = new Sort(order);
			query.with(sort);
		}

		if (page != null) {
			query.skip((page.getPage() - 1) * page.getRows());
			query.limit(page.getRows());
		}
		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public <T> List<T> executeForObjectList(Map<String, Object> map, Map<String, Object> regex, PageHelper page,
			Class<?> cls) {
		LOGGER.debug(cls.getName() + "---多条分页查询查询");
		List<T> list = new ArrayList<T>();

		Query query = new Query();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
		}

		for (Map.Entry<String, Object> entry : regex.entrySet()) {
			query.addCriteria(Criteria.where(entry.getKey()).regex(".*?" + entry.getValue() + ".*"));
		}

		query.skip((page.getPage() - 1) * page.getRows());
		query.limit(page.getRows());

		try {
			list = (List<T>) mongoTemplate.find(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				list = (List<T>) mongoTemplate.find(query, cls);
			} else {
				LOGGER.error(e.getMessage());
			}
		}
		return list;
	}

	/**
	 * 分组查询
	 * @param agg
	 * @param claName
	 * @param cls
	 * @param <T>
	 */
	@Override
	public <T> List<T> executeForObjectAggregateList(Aggregation agg, String claName, Class<?> cls) {
		List<T> list = new ArrayList<T>();
		try {
			AggregationResults<T> outputType= (AggregationResults<T>) mongoTemplate.aggregate(agg,claName,cls);
			 list=outputType.getMappedResults();
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				AggregationResults<T> outputType= (AggregationResults<T>) mongoTemplate.aggregate(agg,claName,cls);
				list=outputType.getMappedResults();
			} else {
				LOGGER.error(e.getMessage());
			}
		}
		return list;
	}


	/**
	 * 去掉某个重复，统计总条数
	 * @param query
	 * @param claName
	 * @param fieldName
	 * @param cls
	 * @return
	 */
	@Override
	public long executeForObjectDistinctCount(DBObject query, String claName, String fieldName, Class<?> cls) {
		long count = 0L;
		try {
			count=mongoTemplate.getCollection(claName).distinct(fieldName,query).size();
		 } catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count=mongoTemplate.getCollection(claName).distinct(fieldName,query).size();
			} else {
				LOGGER.error(e.getMessage());
			}
		}
		return count;
	}



	@Override
	public long executeForObjectListCount(Query query, Class<?> cls) {
		LOGGER.debug(cls.getName() + "---数据数量查询");
		long count = 0L;
		try {
			count = mongoTemplate.count(query, cls);
		} catch (Exception e) {
			if (e.getMessage().contains(NETWORK_EOF)) {
				count = mongoTemplate.count(query, cls);
			} else {
				LOGGER.error(e.getMessage());
			}
		}
		return count;
	}
	
	/**
	 * 执行命令返回结果
	 */
	@Override
	public CommandResult executeCommand(String jsonCommand) {
		return mongoTemplate.executeCommand(jsonCommand);
	}

	@Override
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

}
