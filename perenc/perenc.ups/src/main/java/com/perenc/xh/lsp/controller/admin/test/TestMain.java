package com.perenc.xh.lsp.controller.admin.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author chen long
 * @desc
 * @date 2019-12-07 15:21
 */
public class TestMain {

    public static void main(String args[]){

        /*String phone="18285383669";
        String password=phone.substring(5,11);
        System.out.println("======password=="+password);*/

        /*Integer num= 0;
        if(num.equals(0)) {
            System.out.println("======password=="+1);
        }


        String phone="贵GCL016";
        String code1=phone.substring(1,phone.length());
        String code2=phone.substring(phone.length()-1,phone.length());
        String ncarNum="桂FDR3P"+code2;
        String ncarNum1="民航FDR82";
        System.out.println("======password=="+ncarNum+"====="+ncarNum1);*/


        //String str="[{projectCode=p190594610, inRecordId=4af7fcdd-2703-4cfc-a2d5-c01ee8979c5d, parkId=c8973c5aa18611e994dc0894ef6502e6, inDeviceId=140406784, inDeviceName=2号岗入口控制机, inTime=2020/1/21 18:31:27, plateNumber=贵GCL016, plateColor=UNKNOW, inImage=http://10.10.10.254:9012/down/pic/20200121/park/140406784/183126_贵GCL016_BLUE_SB2.jpg, sealName=null, stationOperator=托管电脑, parkEventType=null, remark=null, offlineFlag=0, deviceIoType=0, setmealNo=null, reTrySend=null, userType=0}]";
        String str="[{projectCode=p190594610}]";
        JSONArray recordJsarry=JSONArray.parseArray(str);

        System.out.println("======password==+====="+recordJsarry);

    }
}
