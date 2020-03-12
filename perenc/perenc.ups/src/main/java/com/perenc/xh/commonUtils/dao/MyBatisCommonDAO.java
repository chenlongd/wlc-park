package com.perenc.xh.commonUtils.dao;

import java.util.List;

/**
 * 数据持久层共通DAO
 * 
 * @author Administrator
 */
public interface MyBatisCommonDAO {

	/**
	 * 查询单条数据
	 * 
	 * @param sql
	 * @param param
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <E> E executeForObject(String sql, Object param, Class<?> cls);

	/**
	 * 多条数据查询
	 * 
	 * @param sql
	 * @param bindParams
	 * @return
	 */
	public <E> List<E> executeForObjectList(String sql, Object bindParams);
	
	/**
	 * 多条数据查询总条数
	 * 
	 * @param sql
	 * @param bindParams
	 * @return
	 */
	public <E> int executeForObjectListCount(String sql, Object bindParams);

	/**
	 * 增加数据有参
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public int executeInsert(String sql, Object param) throws Exception;

	/**
	 * 增加数据无参
	 * 
	 * @param sql
	 * @return
	 */
	public int executeInsert(String sql) throws Exception;

	/**
	 * 批量增加数据
	 * 
	 * @param sql
	 * @param listParam
	 * @return
	 */
	public int executeInserts(String sql, List<?> listParam) throws Exception ;

	/**
	 * 有参更新数据
	 * 
	 * @param sql
	 * @param param
	 */
	public int executeUpdate(String sql, Object param) throws Exception;
	
	/**
	 * 有参更新数据
	 * 
	 * @param sql
	 * @param param
	 */
	public int executeUpdates(String sql, List<?> param) throws Exception;

	/**
	 * 无参更新数据
	 * 
	 * @param sql
	 */
	public int executeUpdate(String sql) throws Exception;
	
	/**
	 * 删除语句执行无参�?
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int executeDelete(String sql) throws Exception;
	
	/**
	 * 删除语句执行
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int executeDelete(String sql, Object param) throws Exception;
	
	/**
	 * 批量删除语句执行
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int executeDeletes(String sql, List<?> param) throws Exception;

}
