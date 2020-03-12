package com.perenc.xh.lsp.controller.phone.tcTask;

import com.perenc.xh.lsp.service.order.SysOrderService;
import com.perenc.xh.lsp.service.tcCar.TcCarService;
import com.perenc.xh.lsp.service.tcCarInpass.TcCarInpassService;
import com.perenc.xh.lsp.service.tcCarJsclientlog.TcCarJsclientlogService;
import com.perenc.xh.lsp.service.tcCarPaycheck.TcCarPaycheckService;
import com.perenc.xh.lsp.service.tcExtendCoupon.TcExtendCouponService;
import com.perenc.xh.lsp.service.tcSellerApplyc.TcSellerApplycService;
import com.perenc.xh.lsp.service.tcSellerFreecar.TcSellerFreecarService;
import com.perenc.xh.lsp.service.urlLog.UrlLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class TcTaskController implements Runnable{

    private static final Logger logger = Logger.getLogger(TcTaskController.class);

    @Autowired(required = false)
    private TcCarService tcCarService;

    @Autowired(required = false)
    private TcExtendCouponService tcExtendCouponService;

    @Autowired(required = false)
    private TcCarInpassService tcCarInpassService;

    // 免费车
    @Autowired(required = false)
    private TcSellerFreecarService tcSellerFreecarService;

    //会议宴会申请券
    @Autowired(required = false)
    private TcSellerApplycService tcSellerApplycService;

    // 停车定单
    @Autowired(required = false)
    private SysOrderService sysOrderService;

    // 访问日志
    @Autowired(required = false)
    private UrlLogService urlLogService;

    // 捷顺访问日志
    @Autowired(required = false)
    private TcCarJsclientlogService tcCarJsclientlogService;

    // 捷顺支付结果反查日志
    @Autowired(required = false)
    private TcCarPaycheckService tcCarPaycheckService;

    /**
     * 每天零晨10分定时更改状态
     */
    @Override
    @Scheduled(cron = "00 10 00 * * ?")
    public void run() {
        try {
            logger.info("每天零晨10分定时更改状态定时任务");
            //每天零晨10分定时更改过期票券
            tcExtendCouponService.updateBatchCouponUseStatusByEdate();
            // 删除临时定单
            sysOrderService.deleteTcTempOrderByEdate();
            //更改vip车辆状态
            tcCarService.updateBatchVipByedate();
            // 删除访问日志
            urlLogService.removeBatchUrlLogByEdate();
            // 删除捷顺访问日志
            tcCarJsclientlogService.removeBatchTcCarJsclientlogByEdate();
            // 删除捷顺结果反查日志
            tcCarPaycheckService.removeBatchTcCarPaycheckByEdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每半小时执行一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void runFreecar() {
        try {
            logger.info("每半小时执行一次定时更改免费车辆状态定时任务");
            //更改免费车辆状态
            tcCarService.updateBatchFreeByedate();
            //更改设置免费车状态
            tcSellerFreecarService.updateBatchFreecarUseStatusByEdate();
            // 更改申请券使用状态
            tcSellerApplycService.updateBatchSellerApplycUseStatusByEdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每天根据捷顺查询场内车辆，更改入场状态
     */
    @Scheduled(cron = "00 10 03 * * ?")
    public void runupdateBatchIsEntry() {
        try {
            logger.info("每天根据捷顺查询场内车辆，更改入场状态");
            // 根据捷顺查询场内车辆，更改入场状态
            tcCarInpassService.updateBatchIsEntryByInsideJscar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
