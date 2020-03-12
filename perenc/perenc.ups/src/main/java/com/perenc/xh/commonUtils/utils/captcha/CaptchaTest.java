package com.perenc.xh.commonUtils.utils.captcha;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 测试类
 * Created by
 */
public class CaptchaTest {


    public static void main(String[] args) throws FileNotFoundException {
        for (int i = 0; i < 5; i++) {
            //SpecCaptcha specCaptcha = new SpecCaptcha();
            SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
            specCaptcha.setCharType(Captcha.TYPE_ONLY_CHAR);
            System.out.println(specCaptcha.text());
            specCaptcha.out(new FileOutputStream(new File("D:/logs/code/code" + i + ".png")));
        }
    }



}
