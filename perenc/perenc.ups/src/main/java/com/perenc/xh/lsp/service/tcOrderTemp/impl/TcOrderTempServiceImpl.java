package com.perenc.xh.lsp.service.tcOrderTemp.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.tcOrder.TcOrderTempDao;
import com.perenc.xh.lsp.entity.data.CreateOrderData;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderInvoice;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderTemp;
import com.perenc.xh.lsp.service.order.SysOrderService;
import com.perenc.xh.lsp.service.tcOrderTemp.TcOrderTempService;
import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tcOrderTempService")
@Transactional(rollbackFor = Exception.class)
public class TcOrderTempServiceImpl implements TcOrderTempService {
    private static final Logger logger = Logger.getLogger(TcOrderTempServiceImpl.class);
    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private TcOrderTempDao tcOrderTempDao;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private SysOrderService sysOrderService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Override
    public ReturnJsonData insert(TcOrderTemp tcOrderTemp) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcOrderTemp.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcOrderTemp.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcOrderTemp);
        int flag = tcOrderTempDao.add(insertParam);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcOrderTemp tcOrderTemp) throws Exception {
        QueryParam param = new QueryParam();
        param.addCondition("id","=",tcOrderTemp.getId());
        TcOrderTemp returnTcOrderTemp = tcOrderTempDao.getOne(param);
        if(returnTcOrderTemp != null){

            //returnTcSeller.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = tcOrderTempDao.update(DBUtil.toUpdateParam(returnTcOrderTemp, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
        Map<String,Object> condition=new HashMap<>();
        condition.put("ids",ids);
        condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcOrderInvoice.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 批量修改状态删除
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData deleteBatch(List list) throws Exception {
        int flag= tcOrderTempDao.deleteBatch(list);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(Integer id) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        TcOrderTemp tcOrderTemp = tcOrderTempDao.getById(id);
        if (tcOrderTemp!=null) {
            condition.put("id",tcOrderTemp.getId());
            condition.put("status",tcOrderTemp.getStatus());
            condition.put("createTime",tcOrderTemp.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String phone =  MapUtils.getString(map, "phone");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        param.addCondition("status","=",1);
        long count = tcOrderTempDao.count(param);
        List<TcOrderTemp> tcOrderTemps = tcOrderTempDao.list(param);
        List<Map<String,Object>> tcSellerlist = new ArrayList<>();
        for(TcOrderTemp tcOrderTemp : tcOrderTemps){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcOrderTemp.getId());
            condition.put("status",tcOrderTemp.getStatus());
            condition.put("createTime",tcOrderTemp.getCreateTime());
            tcSellerlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 用户充值
     * 支付生成定单
     * @param map
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData payCheck(Map<String, Object> map, TcOrderTemp tcOrderTemp) throws Exception {
        String carRechargeId =  MapUtils.getString(map, "carRechargeId");
        /*TcCarRecharge tRecharge = mongoComDAO.executeForObjectById(carRechargeId, TcCarRecharge.class);
        if(tRecharge==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的充值项错误", null);
        }*/
        //查询用户
        ExtendUser extendUser = extendUserDao.getById(tcOrderTemp.getExtendId());
        if(extendUser != null){
            //支付金额
            //double payPrice=ToolUtil.getIntToDouble(tRecharge.getOldPrice()+tRecharge.getGetPrice());
            //充值金额走微信
            //生成定单
            CreateOrderData createOrder=new CreateOrderData();
            //定单类型：1：商城，2:停车场客户户充值，3：停车场客户购卡充值，3：停车场用户支付停车费
            createOrder.setType(2);
            createOrder.setCustomerId(extendUser.getCustomerId());
            createOrder.setExtendId(extendUser.getId());
            createOrder.setPhone(extendUser.getPhone());
            //createOrder.setTotalPrice(payPrice);
            createOrder.setObjId(carRechargeId);
            /*List<GoodsInfo> goodsInfoList =new ArrayList<GoodsInfo>();
            GoodsInfo goodsInfo =new GoodsInfo();
            goodsInfo.setGoodsAttributeId(tRecharge.getId());
            goodsInfo.setNumber(1);
            goodsInfo.setStoreId(ConstantType.SYS_ORDER_TC_STORE_ID);
            goodsInfo.setPrice(payPrice);
            goodsInfo.setGoodsName("停车场充值");
            goodsInfoList.add(goodsInfo);
            createOrder.setGoodsInfoList(goodsInfoList);*/
            return sysOrderService.createOrder(createOrder);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加订单失败", null);

    }



    /**
     * 查询车辆停车缴费信息
     * 创建临时定单
     * @param map
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData addOrderTempPay(Map<String, Object> map, TcOrderTemp tcOrderTemp) throws Exception {
        //条件筛选
        //查询用户
        ExtendUser extendUser = extendUserDao.getById(tcOrderTemp.getExtendId());
        if(extendUser==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户ID错误",null);
        }
        //生成定单
        CreateOrderData orderData=new CreateOrderData();
        //定单类型：1：商城，2:停车场客户户充值，3：停车场客户购卡充值，4：停车场用户支付停车费
        orderData.setType(4);
        orderData.setCustomerId(extendUser.getCustomerId());
        orderData.setExtendId(extendUser.getId());
        orderData.setBalance(extendUser.getBalance());
        orderData.setPhone(extendUser.getPhone());
        //orderData.setTotalPrice(parkPrice);
        orderData.setCarId(tcOrderTemp.getCarId());

        /*List<GoodsInfo> goodsInfoList =new ArrayList<GoodsInfo>();
        GoodsInfo goodsInfo =new GoodsInfo();
        //添加捷顺支付编号
        goodsInfo.setGoodsAttributeId(payNo);
        goodsInfo.setNumber(1);
        goodsInfo.setPrice(parkPrice);
        goodsInfo.setStoreId(tcOrderTemp.getCarId());
        goodsInfo.setGoodsName("停车场出场缴费");
        goodsInfoList.add(goodsInfo);
        orderData.setGoodsInfoList(goodsInfoList);*/
        //添加到临时定单结束
        return sysOrderService.createOrder(orderData);
    }





}
