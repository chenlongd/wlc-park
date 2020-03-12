package com.perenc.xh.commonUtils.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 * @author Cloud
 * @data   2016-12-15
 * QRCode
 */

public class QRCodeUtil {
    
    //二维码颜色
    private static final int BLACK = 0xFF000000;
    //二维码颜色
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     * @param text    <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height    二维码高
     * @param outPutPath    二维码生成保存路径
     * @param imageType        二维码生成格式
     */
    public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType){
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
            
            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();
            
            //3、将二维码放入缓冲流
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
            File outPutImage = new File(outPutPath);
            //如果图片不存在创建图片
            if(!outPutImage.exists())
                outPutImage.createNewFile();
            //5、将二维码写入图片
            ImageIO.write(image, imageType, outPutImage);
        } catch (WriterException e) {
            e.printStackTrace();
            System.out.println("二维码生成失败");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成二维码图片失败");
        }
    }



    /**
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     * @param text    <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height    二维码高
     * @param imageType        二维码生成格式
     */
    public static BufferedImage zxingCodeImageCreate(String text, int width, int height,String imageType){
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BufferedImage image=null;
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();

            //3、将二维码放入缓冲流
            image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成二维码图片失败");
        }
        return image;
    }



    /**
     * 去掉白边
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     * @param text    <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height    二维码高
     * @param imageType        二维码生成格式
     */
    public static BufferedImage zxingQRImageCreate(String text, int width, int height,String imageType){
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BufferedImage image=null;
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
            encode = deleteWhite(encode);//删除白边
            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();

            //3、将二维码放入缓冲流
            image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成二维码图片失败");
        }
        return image;
    }

    /**
     * 删除白边
     * */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    
    
    
    public static void main(String[] args) {

        /**
         *    QRcode 二维码生成测试
         *    QRCodeUtil.QRCodeCreate("http://blog.csdn.net/u014266877", "E://qrcode.jpg", 15, "E://icon.png");
         */
        /**
         *     QRcode 二维码解析测试
         *    String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("D://logs//qcode//zxingcode.png");
         */
        /**
         * ZXingCode 二维码生成测试*/
         //QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "D://logs//qcode//zxingcode.png", "png");
       //QRCodeUtil.zxingQRImageCreate("18285383669", 300, 300, "D://logs//qcode//zxingcode.png", "png");

        /**
         * ZxingCode 二维码解析
         *    String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("E://zxingcode.jpg").toString();
         */
        System.out.println("success");
	}

}


