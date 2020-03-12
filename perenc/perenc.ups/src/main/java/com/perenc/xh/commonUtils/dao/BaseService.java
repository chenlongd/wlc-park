package com.perenc.xh.commonUtils.dao;

import com.perenc.xh.commonUtils.DBRelevant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseService<T> {

	public Map<String, Object> getModel() {
		return new HashMap<String, Object>();
	}

	public abstract BaseDao<T> getDao();

	// ---------------------增删改查 start---------------------
	/** 通用分页查询 */
	public List<T> list(QueryParam params) {
		return getDao().list(params);
	}

	/** 查询总数 */
	public Integer count(QueryParam params) {
		return getDao().count(params);
	}

	/** 通过主键查询 */
	public T getById(Object id) {
		return getDao().getById(id);
	}
	
	public List<T> getByIds(Object... ids) {
		return getDao().getByIds(ids); 
	}

	
	public Pagination listPage(QueryParam params) {
		List<T> data = getDao().list(params);
		int total = getDao().count(params);

		return new Pagination(total, data);
	}
	
	/** 查询单条记录 */
	public T getOne(QueryParam param) {
		return getDao().getOne(param);
	}

	/** 新增 */
	public Integer add(T param) {
		InsertParam insertParam = DBUtil.toInsertParam(param);
		return getDao().add(insertParam); 
	}
	
	public String addRet(T param) {
		InsertParam insertParam = DBUtil.toInsertParam(param);
		getDao().add(insertParam);
		return insertParam.getId();
	}

	/** 删除 */
	public Integer delete(Object... ids) {
		return getDao().delete(ids);
	}

	public Integer delete(Object id) {
		return getDao().delete(new Object[] { id });
	}

	/** 修改 */
	public Integer update(T param, String... keys) {
		UpdateParam upParams = DBUtil.toUpdateParam(param, keys);
		return getDao().update(upParams);
	}
	
	public Integer update(UpdateParam params){
		return getDao().update(params);
	}

	/** 修改 */
	public Integer update(T param) {
		return update(param, "id");
	}
}
