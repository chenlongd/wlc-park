package com.perenc.xh.commonUtils.utils.StringOrDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密
 * @author 王岑
 *
 */
public class MD5 {
   private MessageDigest md5;
   static char hexDigits[] = {       // 用来将字节转换成 16 进制表示的字符
           '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
   public MD5() {
       try {
           md5 = MessageDigest.getInstance("MD5");
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("no such md5 algorithm!", e);
       }
   }

   public String hexString(byte[] source) {
       byte[] bs = md5.digest(source);
       char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
       // 所以表示成 16 进制需要 32 个字符
       int k = 0;                                // 表示转换结果中对应的字符位置
       for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
           // 转换成 16 进制字符的转换
           byte byte0 = bs[i];                 // 取第 i 个字节
           str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
           // >>> 为逻辑右移，将符号位一起右移
           str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
       }
       return new String(str);
   }

   //文件生成校验码
   public static String getFileCheckSum(String fileName) {
        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;

            //long s = System.currentTimeMillis();
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            byte[] b = md.digest();
            return byteToHexString(b);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }

   public String hexString(String source, Charset charset) {
       String md5Str = "";
       md5Str = hexString(source.getBytes(charset));
       return md5Str;
   }
}
