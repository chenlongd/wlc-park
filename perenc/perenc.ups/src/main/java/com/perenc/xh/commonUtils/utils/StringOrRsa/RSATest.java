package com.perenc.xh.commonUtils.utils.StringOrRsa;

public class RSATest {


    public static void main(String args[]) {

        RSAGenerator rsaGenerator = new RSAGenerator().generateKeyPair();
       /* String str="数表的质数又称素数。指整数在一个大于1的自然数中，除了1和此整数自身外，没法被其他自然数整除的数";
        System.out.println("用公钥加密私钥解密");
        String encode = rsaGenerator.encryptByPublic(str);
        System.out.println(encode);
        System.out.println(rsaGenerator.decryptByPrivate(encode));
        System.out.println("jj:"+rsaGenerator.decryptByPrivate("79c8c8d36ca86e2772c51eb555dac0499fc034d07162ab8fc00236c127052d919147cd514864b46f675099976d0f06cf88370556c14bd29593a12910a126d04ea35863612bea1d201d7874944e28c3a9808d100f9be84134f9c638059a9d5109b97183ceec0957710558c180fe54f52a5c94d09e9dd1b1add4033d71ed8cc60cd4117bc482a020fa4aa2381b43315c7baaccee1f688f29a8688afbb085fd9935274b8821d06f2d81ca1f2520f4aaf95c7e9c147969124d811b3956fa054d6f255dbda142ae690dbaebe11ac152826219797c2f0306cb35e47c168b5e7d902dc48e110ffa3fa926fc3f5bbbdb03a4a3d4dba3a1cf340c6ef0f44bab628850def3"));
*/
        String str="数表的质数又称素数。指整数在一个大于1的自然数中，除了1和此整数自身外，没法被其他自然数整除的数";
        System.out.println("用私钥加密公钥解密");
        String encrypt = rsaGenerator.encryptByPrivate(str);
        System.out.println(encrypt);
        System.out.println(rsaGenerator.decryptByPublic(encrypt));

    }
}
