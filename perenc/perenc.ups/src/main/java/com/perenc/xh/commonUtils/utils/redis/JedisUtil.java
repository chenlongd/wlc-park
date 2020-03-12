package com.perenc.xh.commonUtils.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/2/21 15:20
 **/
public class JedisUtil {

    private static Logger logger = Logger.getLogger(JedisUtil.class);

    protected static ReentrantLock lockPool = new ReentrantLock();

    private  static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    private static void initialPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(8);
            config.setMaxIdle(8);
            config.setMaxWaitMillis(0);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config,"47.112.152.124",6281,0,"wlc_park_6688*");
        } catch (Exception e) {
            logger.info("初始化连接池失败！");
        }
    }

    /**
     * 在多线程环境同步初始化，
     * redisPool只要一个就好了，所以要设置锁，保证只能初始化一个
     */
    private static void poolInit() {
        lockPool.lock();
        try {
            if (jedisPool == null) {
//                initialConfig();
                initialPool();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockPool.unlock();
        }
    }


    /**
     * 这里可以使用多线程进行加载，所以将锁去掉,
     * 保证了只有一个jedisPool，
     * @return
     */
    public static Jedis getJedis() {
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.info("getJedis() 方法出错：" + e.getMessage());
        } finally {
            close(jedis);
        }
        return jedis;
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void close(final Jedis jedis) {
        try {
            if (jedis != null && jedisPool != null) {
                jedis.close();
            }
        } catch (Exception e) {
            logger.info("close() 方法出错：" + e.getMessage());
        }
    }

    /**
     * 设置 String
     *
     * @param key
     * @param value
     */
    public static boolean setString(String key, String value) {
        try {
            getJedis().set(key, value);
            return true;
        } catch (Exception e) {
            logger.info("setString() 方法出错：" + e.getMessage() + "key = " + key + "value = " + value);
            return false;
        }
    }

    /**
     * 设置 过期时间
     *
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static boolean setString(String key, int seconds, String value) {
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().setex(key, seconds, value);
            return true;
        } catch (Exception e) {
            logger.info("setString() 方法出错：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取String值
     *
     * @param key
     * @return value
     */
    public static String getString(String key) {
        try {
            return getJedis().get(key);
        } catch (Exception e) {
            logger.info("getString() 方法出错：" + e.getMessage() + "key = " + key);
            return "0$0";
        }
    }

    /**
     * 删除key的键值对
     * @param key
     * @return
     */
    public static boolean delString(String key){
        try {
            getJedis().del(key);
            return true;
        } catch (Exception e) {
            logger.info("getString() 方法出错：" + e.getMessage() + "key = " + key);
            return false;
        }
    }
}
