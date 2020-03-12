package com.perenc.xh.commonUtils.utils.StringOrRsa;


import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {
    // RSA公钥
    public static final String KEY_TYPE_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmmkjStk9+jS3iGiHcQa9+Y927\n" +
            "iwLKjc6A3/9yTT13OIj02TGLCcamJFxBoThz55hAL2c5BgczdlhuAjpwRDfQwXa0\n" +
            "G1+yAYC5ICSjRVg2bDPf/OxuRMa3torU3wsptTtg0K7UcWr+TUevNsJXw4y5TO6Q\n" +
            "JTLBW1mXVYWFbIEAOQIDAQAB";
    // RSA私钥
    public static final String KEY_TYPE_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKaaSNK2T36NLeIa\n" +
            "IdxBr35j3buLAsqNzoDf/3JNPXc4iPTZMYsJxqYkXEGhOHPnmEAvZzkGBzN2WG4C\n" +
            "OnBEN9DBdrQbX7IBgLkgJKNFWDZsM9/87G5Exre2itTfCym1O2DQrtRxav5NR682\n" +
            "wlfDjLlM7pAlMsFbWZdVhYVsgQA5AgMBAAECgYEAh9AEB4nJGgAa5FOrQLNnTaA5\n" +
            "lHYI63hYXCS+R76BmdvDgd5AjfiFMNE5R+MWcx3bto8uFYU9f409H0i/vN9Po5Qe\n" +
            "R2rXjcooPwAijy5AxqPGHbQ49/OwPXsR7TJTiToOzqc49k+FtK9SzFxYwKAD6vyc\n" +
            "sOQsWC7VKnNV3HiGO1ECQQDSXcpvTsvBSJbPecgsgD3jglY/N2AYsz5/1vw3oskx\n" +
            "JaoEWE4f9vuU2jN15D0n4qZwkkVCAIaktluNhBKecDAtAkEAyr4tm/OSH4yBtS1A\n" +
            "q0GWLPJfhOl7MPU8lP4vOIIncDt9KaeW1UPS2vgcVFpOfVbnK/Z9NNn9+hqfrq86\n" +
            "PbmLvQJAQEWFjuJdIE2EPhlwDiCUSAlPPns1sfY5lydg4cKrg4eFXVR5cgysqrrL\n" +
            "1C4KfUAjJ+uYm2S7vmE0sZTzd1LBdQJAKfOqSl/fN6oCWPQRMp6yrYBFpaIOfLXy\n" +
            "ID5slPvRn1af8pOcEWskYWz/p7C8FxF5ak5p4BrPhAYoTqQxn/4Z3QJAXsbr13VV\n" +
            "U597xrZRg6sY9HGufwwGBtdtOLNQKaigYxZUkMcSHFvyI1WSX+gqFM7b3BQQel72\n" +
            "/Db8fe79SRm3xA==";


    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        //genKeyPair();
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + KEY_TYPE_PUBLIC);
        System.out.println("随机生成的私钥为:" + KEY_TYPE_PRIVATE);
        String messageEn = encrypt(message,KEY_TYPE_PUBLIC);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,KEY_TYPE_PRIVATE);
        System.out.println("还原后的字符串为1:" + messageDe);
        String messageDe1 = decrypt("U2zvs2RbJRVR4z+KHuMLCyQz+rer4Hd5UVNxrzQiKsUT+Oa3jWrzsm8dpLIInQ/S+lTf+hj2lpeoxhah/y1wqbwE7HL1spBcodTkzm1gjkRRe1p2Y+oYrLQAff42iTsAM+XaETzZ6j+sSMcnXoTNoljNrw56ftKGsBEdvACpb1o=",KEY_TYPE_PRIVATE);
        System.out.println("还原后的字符串为2:" + messageDe1);
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}
