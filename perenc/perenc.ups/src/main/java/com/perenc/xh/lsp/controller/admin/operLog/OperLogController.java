package com.perenc.xh.lsp.controller.admin.operLog;


import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.operLog.OperLogRecord;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.operLog.OperLogRecordService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/23 10:47
 **/
@Aspect
public class OperLogController {

    private static Logger logger = Logger.getLogger("OperLogController.class");

    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间
    private User user = null;
    private HttpServletRequest request = null;

    @Autowired(required = false)
    private OperLogRecordService operLogRecordService;

    /**
     * 方法调用前触发 记录数据
     * @param joinPoint
     * @param log
     */
    @Before("@annotation(log)")
    public void before(JoinPoint joinPoint, OperLog log){
        request = getHttpServletRequest();
        user = (User)request.getSession().getAttribute("user");
        startTimeMillis = System.currentTimeMillis(); //记录方法开始执行的时间
    }

    /**
     * 方法调用后触发   记录结束时间
     * @param joinPoint
     * @param log
     */
    @After("@annotation(log)")
    public  void after(JoinPoint joinPoint,OperLog log) {
        request = getHttpServletRequest();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] methods = targetClass.getMethods();
        String operationName = "";
        String operationType = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs!=null&&clazzs.length == arguments.length&&method.getAnnotation(OperLog.class)!=null) {
                    operationName = method.getAnnotation(OperLog.class).operationName();
                    operationType = method.getAnnotation(OperLog.class).operationType();
                    break;
                }
            }
        }
        endTimeMillis = System.currentTimeMillis();
        //格式化开始时间
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
        //格式化结束时间
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeMillis);
        if(user != null){
            OperLogRecord record = new OperLogRecord();
            record.setOperationUser(user.getUsername());
            record.setOperationType(operationType);
            record.setOperationName(operationName);
            record.setOperationStartTime(startTime);
            record.setOperationEndTime(endTime);
            try{
                PhoneReturnJson json = operLogRecordService.insert(record);
                logger.info("操作日志添加是否成功"+json.isSuccess()+"添加日志"+json.getMsg());
            }catch (Exception e){
                logger.info("-----------操作日志添加失败-------------");
            }
        }
    }

    /**
     * 获取request
     * @return
     */
    public HttpServletRequest getHttpServletRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    /**
     * 方法环绕触发
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        return null;
    }

}
