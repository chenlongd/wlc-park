package com.perenc.xh.commonUtils.utils.StringOrRsa;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAGenerator {
    /**
     * 算法
     */
    private String ALGORITHM_RSA = "RSA";
    private String DEFAULT_ENCODING = "UTF-8";

    public static final String KEY_TYPE_PUBLIC = "PUBLIC";
    public static final String KEY_TYPE_PRIVATE = "PRIVATE";

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    //private String publicKeyStr;

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    //private String privateKeyStr;

    // RSA公钥字符串
    public static final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmmkjStk9+jS3iGiHcQa9+Y927\n" +
            "iwLKjc6A3/9yTT13OIj02TGLCcamJFxBoThz55hAL2c5BgczdlhuAjpwRDfQwXa0\n" +
            "G1+yAYC5ICSjRVg2bDPf/OxuRMa3torU3wsptTtg0K7UcWr+TUevNsJXw4y5TO6Q\n" +
            "JTLBW1mXVYWFbIEAOQIDAQAB";
    // RSA私钥字符串
    public static final String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKaaSNK2T36NLeIa\n" +
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

    /**
     * 用于加解密
     */
    private Cipher cipher;

    /**
     * 明文块的长度 它必须小于密文块的长度 - 11
     */
    private int originLength = 128;
    /**
     * 密文块的长度
     */
    private int encrytLength = 256;

    /**
     * 生成密钥对
     * @return
     */
    public RSAGenerator generateKeyPair() {
        try {
            // RSA加密算法
            /*KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(ALGORITHM_RSA);
            // 创建密钥对，长度采用2048
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 分别得到公钥和私钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 使用 Base64编码
            publicKeyStr = Base64Util.encode(publicKey.getEncoded());
            privateKeyStr = Base64Util.encode(privateKey.getEncoded());*/


            byte[] decodedPublic = Base64.decodeBase64(publicKeyStr);
            publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPublic));

            byte[] decodedPrivate = Base64.decodeBase64(privateKeyStr);
            privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedPrivate));


            //将BASE64编码的结果保存到文件内
            /*String classPath = this.getClass().getClassLoader().getResource("").toString();
            String prefix = classPath.substring(classPath.indexOf(":") + 1);
            String publicFilePath = prefix+"public.txt";
            File publicFile= new File(publicFilePath);
            saveBase64KeyToFile(publicFile, publicKeyStr);

            String privateFilePath = prefix+"private.txt";
            File privateFile= new File(privateFilePath);
            saveBase64KeyToFile(privateFile, privateKeyStr);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }



    /**
     * 用公钥加密
     * @param content
     * @return 加密后的16进制字符串
     */
    public String encryptByPublic(String content) {
        String encode = "";
        try {
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 该密钥能够加密的最大字节长度
            int splitLength = publicKey.getModulus().bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content.getBytes(), splitLength);
            // 加密
            StringBuffer buffer = new StringBuffer();
            for (byte[] array : arrays) {
                buffer.append(bytesToHexString(cipher.doFinal(array)));
            }
            encode = buffer.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    /**
     * 用私钥加密
     *
     * @param content
     * @return 加密后的16进制字符串
     */
    public String encryptByPrivate(String content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 该密钥能够加密的最大字节长度
            int splitLength = ((RSAPrivateKey) privateKey).getModulus()
                    .bitLength() / 8 - 11;
            byte[][] arrays = splitBytes(content.getBytes(), splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(bytesToHexString(cipher.doFinal(array)));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用私钥解密
     * @param content
     * @return 解密后的原文
     */
    public String decryptByPrivate(String content) {
        String decode = "";
        try {
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 该密钥能够加密的最大字节长度
            int splitLength = privateKey.getModulus().bitLength() / 8;
            byte[] contentBytes = hexStringToBytes(content);

            byte[][] arrays = splitBytes(contentBytes, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(new String(cipher.doFinal(array)));
            }
            decode = stringBuffer.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 用私钥解密
     *
     * @param content
     * @return 解密后的原文
     */
    public String decryptByPublic(String content) {

        String decode = "";
        try {
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            // 该密钥能够加密的最大字节长度
            int splitLength = publicKey.getModulus().bitLength() / 8;
            byte[] contentBytes = hexStringToBytes(content);

            byte[][] arrays = splitBytes(contentBytes, splitLength);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte[] array : arrays) {
                stringBuffer.append(new String(cipher.doFinal(array)));
            }
            decode = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 根据限定的每组字节长度，将字节数组分组
     * @param bytes 等待分组的字节组
     * @param splitLength 每组长度
     * @return 分组后的字节组
     */
    public static byte[][] splitBytes(byte[] bytes, int splitLength) {
        // bytes与splitLength的余数
        int remainder = bytes.length % splitLength;
        // 数据拆分后的组数，余数不为0时加1
        int quotient = remainder > 0 ? bytes.length / splitLength + 1
                : bytes.length / splitLength;
        byte[][] arrays = new byte[quotient][];
        byte[] array = null;
        for (int i = 0; i < quotient; i++) {
            // 如果是最后一组（quotient-1）,同时余数不等于0，就将最后一组设置为remainder的长度
            if (i == quotient - 1 && remainder != 0) {
                array = new byte[remainder];
                System.arraycopy(bytes, i * splitLength, array, 0, remainder);
            } else {
                array = new byte[splitLength];
                System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
            }
            arrays[i] = array;
        }
        return arrays;
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param bytes  即将转换的数据
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() < 2) {
                sb.append(0);
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换成字节数组
     *
     * @param hex
     *            16进制字符串
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hex) {
        int len = (hex.length() / 2);
        hex = hex.toUpperCase();
        byte[] result = new byte[len];
        char[] chars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
        }
        return result;
    }

    /**
     * 将char转换为byte
     *
     * @param c
     *            char
     * @return byte
     */
    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 保存公钥到文件
     *
     * @param file
     * @return
     */
    public boolean savePublicKey(File file) {
        return saveKeyToFile(publicKey, file);
    }

    /**
     * 保存私钥到文件
     *
     * @param file
     * @return
     */
    public boolean savePrivateKey(File file) {
        return saveKeyToFile(privateKey, file);
    }

    /**
     * 保存密钥到文件
     * @param key 密钥
     * @param file 文件
     * @return
     */
    private boolean saveKeyToFile(Key key, File file) {
        boolean result = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // 公钥默认使用的是X.509编码，私钥默认采用的是PKCS #8编码
            byte[] encode = key.getEncoded();
            // 注意，此处采用writeObject方法，读取时也要采用readObject方法
            oos.writeObject(encode);
            oos.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean saveBase64KeyToFile(File file, String key) {

        boolean result = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(key.getBytes());
            fos.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 从BASE64文件中读取KEY值
     * @param fileName
     * @param keyType
     */
    public void getKeyFromBase64File(String fileName,String keyType) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResource(fileName).openStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] subByte = new byte[1024];
            int len = 0;
            while((len=inputStream.read(subByte))>0) {
                outStream.write(subByte,0,len);
            }
            inputStream.close();
            outStream.close();
            String base64Key = new String(outStream.toByteArray(), DEFAULT_ENCODING);
            byte[] keybyte = Base64Util.decode(base64Key);

            // 默认编码
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);

            if (KEY_TYPE_PUBLIC.equals(keyType)) {
                X509EncodedKeySpec x509eks = new X509EncodedKeySpec(keybyte);
                publicKey = (RSAPublicKey) keyFactory.generatePublic(x509eks);
                System.out.println(publicKey.getAlgorithm());
            } else {
                PKCS8EncodedKeySpec pkcs8eks = new PKCS8EncodedKeySpec(keybyte);
                privateKey = (RSAPrivateKey) keyFactory
                        .generatePrivate(pkcs8eks);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中得到公钥
     *
     * @param file
     */
    public void getPublicKey(File file) {
        getKey(file, KEY_TYPE_PUBLIC);
    }

    /**
     * 从文件中得到私钥
     *
     * @param file
     */
    public void getPrivateKey(File file) {
        getKey(file, KEY_TYPE_PRIVATE);
    }

    /**
     * 从文件中得到密钥
     *
     * @param file
     * @param keyType
     */
    private void getKey(File file, String keyType) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] keybyte = (byte[]) ois.readObject();
            // 关闭资源
            ois.close();
            // 默认编码
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            if (KEY_TYPE_PUBLIC.equals(keyType)) {
                X509EncodedKeySpec x509eks = new X509EncodedKeySpec(keybyte);
                publicKey = (RSAPublicKey) keyFactory.generatePublic(x509eks);
                System.out.println(publicKey.getAlgorithm());
            } else {
                PKCS8EncodedKeySpec pkcs8eks = new PKCS8EncodedKeySpec(keybyte);
                privateKey = (RSAPrivateKey) keyFactory
                        .generatePrivate(pkcs8eks);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
