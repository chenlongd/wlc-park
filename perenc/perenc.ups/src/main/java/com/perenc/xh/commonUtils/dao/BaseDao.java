package com.perenc.xh.commonUtils.dao;

import com.perenc.xh.commonUtils.DBRelevant.DeleteParam;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.DBRelevant.UpdateParam;

import java.util.List;

/**
 * 基础DAO层
 * @author Edward
 * @param <T> 对应的实体类
 */
public interface BaseDao<T> {//extends YpMapper<T>
	// ---------------------增删改查 start---------------------
	/** 通用分页查询 */
	public List<T> list(QueryParam params);

	/** 查询总数 */
	public Integer count(QueryParam params);

	/** 通过主键查询 */
	public T getById(Object id);

	/** 查询单条记录 */
	public T getOne(QueryParam param);

	/** 新增 */
	public Integer add(InsertParam param);
	
	/** 删除 */
	public Integer delete(Object[] ids);
	 
	public Integer deleteWhere(DeleteParam param);

	/** 修改 */
	public Integer update(UpdateParam param);
	
	
	/** 通过主键查询 */
	public List<T> getByIds(Object[] ids);

}
