package com.perenc.xh.commonUtils.model;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/9 17:15
 **/
public final class DataCodeUtil  {

    private DataCodeUtil() {
    }
    //未登录状态
    public static final int NOT_LOGON = -1;
    //支付输入密码错误，找回密码
    public static final int INPUT_ERROR_PASSWORD_FORGET_PASSWORD = -2;
    // 轮询等待码
    public static final int POLLING_WAITING = -3;
    //成功
    public static final int SUCCESS = 0;
    //失败
    public static final int FALSE = 1008;
    //session不存在
    public static final int NONEXISTENTSESSIONE = 10001;
    //参数不合法
    public static final int PARAM_ERROR = 10002;
    //插入数据库失败
    public static final int INSERT_DATABASE = 10003;
    //修改数据库失败
    public static final int UPDATE_DATABASE = 10004;
    //删除数据库失败
    public static final int DELETE_DATABASE = 10005;
    //查询数据库失败
    public static final int SELECT_DATABASE = 10006;
    //查询数据库失败
    public static final int UPLOAD_FILE = 10007;

}
