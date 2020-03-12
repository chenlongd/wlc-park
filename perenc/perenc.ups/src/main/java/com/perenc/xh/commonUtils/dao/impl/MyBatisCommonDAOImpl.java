package com.perenc.xh.commonUtils.dao.impl;

import com.perenc.xh.commonUtils.dao.MyBatisCommonDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


@SuppressWarnings("all")
@Repository("myBatisCommonDAO")
public class MyBatisCommonDAOImpl extends SqlSessionDaoSupport implements MyBatisCommonDAO {

	
	/**
	 * Log
	 */
	private static Log log = LogFactory.getLog(MyBatisCommonDAOImpl.class);

	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	/**
	 * 查询单条数据
	 * 
	 * @param sql
	 * @param param
	 * @param cls
	 * @return
	 */
	public <E> E executeForObject(String sql, Object param, Class<?> cls) {

		if (log.isDebugEnabled()) {
			log.debug("executeForObject start");
		}

		SqlSession sqlSession = getSqlSession();

		// 发出SQL语句
		Object obj = sqlSession.selectOne(sql, param);

		E rObj = null;
		try {

			// 转换返回结果类型
			if (cls != null) {
				rObj = (E) cls.cast(obj);
			}
		} catch (Exception e) {
			log.error("cast error!");
		}

		if (log.isDebugEnabled()) {
			log.debug("executeForObject End");
		}
		return rObj;
	}

	/**
	 * 多条数据查询
	 */
	
	public <E> List<E> executeForObjectList(String sqlID, Object bindParams) {

		if (log.isDebugEnabled()) {
			log.debug("executeForObjectList Start.");
		}

		SqlSession sqlSession = getSqlSession();

		// 发出SQL语句
		List<E> list = sqlSession.selectList(sqlID, bindParams);

		if (log.isDebugEnabled()) {
			log.debug("executeForObjectList End.");
		}

		return list;
	}
	
	/**
	 * 多条数据查询总数
	 */
	public int executeForObjectListCount(String sqlID, Object bindParams) {

		if (log.isDebugEnabled()) {
			log.debug("executeForObjectList Start.");
		}

		SqlSession sqlSession = getSqlSession();

		// 发出SQL语句
		List list = sqlSession.selectList(sqlID, bindParams);

		if (log.isDebugEnabled()) {
			log.debug("executeForObjectList End.");
		}

		return Integer.parseInt(list.get(0).toString());
	}

	/**
	 * 插入数据有参
	 */
	public int executeInsert(String sql, Object param) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Insert Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		try {
			// 发出SQL语句
			row = sqlSession.insert(sql, param);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}

		if (log.isDebugEnabled()) {
			log.debug("executeInsert End");
		}
		return row;
	}

	/**
	 * 插入数据无参
	 */
	public int executeInsert(String sql) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Insert Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		try {
			// 发出SQL语句
			row = sqlSession.insert(sql);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}

		if (log.isDebugEnabled()) {
			log.debug("executeInsert End");
		}
		return row;
	}

	/**
	 * 批量增加数据
	 * 
	 * @throws Exception
	 */
	public int executeInserts(String sql, List<?> listParam) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Insert Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int i = 0;

		for (Object obj : listParam) {
			// 发出SQL语句
			try {
				sqlSession.insert(sql, obj);
				i++;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception("数据异常");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("executeInsert End");
		}

		return i;
	}

	/**
	 * 更新语句有参�?
	 */
	public int executeUpdate(String sql, Object param) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		try {
			row = sqlSession.update(sql, param);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}
		return row;
	}

	/**
	 * 批量更新语句执行
	 */
	public int executeUpdates(String sql, List<?> params) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		for (Object obj : params) {
			try {
				int i = sqlSession.update(sql, obj);
				row = row + i;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception("数据异常");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Execute Update End.");
		}
		return row;
	}

	/**
	 * 更新语句无参�?
	 */
	public int executeUpdate(String sql) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}
		SqlSession sqlSession = getSqlSession();
		int row = 0;
		try {
			row = sqlSession.update(sql);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}

		if (log.isDebugEnabled()) {
			log.debug("Execute Update End.");
		}
		return row;
	}

	/**
	 * 执行删除语句有参�?
	 */
	public int executeDelete(String sql, Object param) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		try {
			row = sqlSession.delete(sql, param);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}

		if (log.isDebugEnabled()) {
			log.debug("Execute Update End.");
		}
		return row;
	}

	/**
	 * 执行删除无参�?
	 */
	public int executeDelete(String sql) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		try {
			row = sqlSession.delete(sql);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("数据异常");
		}

		if (log.isDebugEnabled()) {
			log.debug("Execute Update End.");
		}
		return row;
	}

	/**
	 * 批量删除
	 */
	public int executeDeletes(String sql, List<?> params) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Execute Update Start.");
		}

		SqlSession sqlSession = getSqlSession();

		int row = 0;

		for (Object param : params) {
			try {
				int i = sqlSession.delete(sql, param);
				row = row + i;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception("数据异常");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Execute Update End.");
		}
		return row;
	}
}