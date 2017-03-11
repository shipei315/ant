package com.pine.ant.service.redis;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Pool;

@Service
public class RedisOperatorService {

    private Logger logger = LoggerFactory.getLogger(RedisOperatorService.class);

    private Pool<? extends BinaryJedisCommands> pool = null;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    //reid url name://:password@host:port/dbIndex
    @Value("${redisUris}")
    private String redisUris;

    @Value("${redis.minIdle}")
    private int minIdle;

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${redis.maxTotal}")
    private int maxTotal;

    @Value("${redis.maxWaitMills}")
    private int maxWaitMills;

    @PostConstruct
    private void init() {
        GenericObjectPoolConfig config = getPoolConfig();
        Assert.isTrue(StringUtils.isNotBlank(redisUris), "redis config uri should not be null!");
        String[] uriArray = StringUtils.split(redisUris, ",");
        if (uriArray.length == 1) {
            pool = new JedisPool(config, URI.create(uriArray[0]));
        } else {
            List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
            for (String uri : uriArray) {
                JedisShardInfo info = new JedisShardInfo(URI.create(uriArray[0]));
                list.add(info);
            }
            pool = new ShardedJedisPool(config, list);
        }
    }

    private GenericObjectPoolConfig getPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setTestWhileIdle(testWhileIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMills);
        return config;
    }

    public String set(String key, String value, boolean compress) {
        BinaryJedisCommands cmd = pool.getResource();
        try {
            if (compress) {

                return "";
            } else {
                // 插入成功返回ok
                return cmd.set(key.getBytes(DEFAULT_CHARSET), value.getBytes(DEFAULT_CHARSET));
            }
        } finally {
            closeJedis(cmd);
        }

    }
    
    /**
     * 该方法的主要目的是为了使用一个cmd进行多次操作
     * @param keys
     * @return
     */
    public Long mdel(String [] keys){
        BinaryJedisCommands cmd = pool.getResource();
        try {
            if (null == keys ||keys.length <=0) 
                return 1l;
            for(String key : keys){
                del(key);
            }
        }catch(Exception e){
            return 0l;
        }finally{
            closeJedis(cmd);
        }
        return 1l;
    }

    public byte[] get(byte[] key) {
        BinaryJedisCommands cmd = pool.getResource();
        try {
            return cmd.get(key);
        } finally {
            closeJedis(cmd);
        }
    }

    public String getString(String key) {
        return new String(get(key.getBytes()));
    }

    public <T extends Serializable> T get(byte[] key, Class<T> clazz) {
        byte[] res = get(key);
        if (null == res) {
            return null;
        }
        return (T) SerializationUtils.deserialize(res);
    }

    public <T extends Serializable> String setEx(byte[] key, T t, int seconds) {
        return setEx(key, SerializationUtils.serialize(t), seconds);
    }

    public String setEx(byte[] key, byte[] value, int seconds) {
        BinaryJedisCommands cmd = pool.getResource();
        try {
            return cmd.setex(key, seconds, value);
        } finally {
            closeJedis(cmd);
        }
    }

    public String setNx(byte[] key, byte[] value, int seconds) {
        BinaryJedisCommands cmd = pool.getResource();
        try {
            return cmd.set(key, value, "NX".getBytes(DEFAULT_CHARSET), "EX".getBytes(DEFAULT_CHARSET), seconds);
        } finally {
            closeJedis(cmd);
        }
    }
    
    public Long del(byte [] key){
        BinaryJedisCommands cmd = pool.getResource();
        try {
            return cmd.del(key);
        } finally {
            closeJedis(cmd);
        }
    }
    
    public Long del(String key){
        return del(key.getBytes());
    }
    
    public Boolean exists(byte [] key){
        BinaryJedisCommands cmd = pool.getResource();
        try {
            return cmd.exists(key);
        } finally {
            closeJedis(cmd);
        }
    }
    
    public Boolean exists(String key){
        return exists(key.getBytes());
    }
    
    public <T extends Serializable> T get (byte [] key, int seconds, Callable<T> callback) {
        byte [] res = get(key);
        if (res != null) {
            return (T)SerializationUtils.deserialize(res);
        }
        
        T obj = null;
        try {
            obj = callback.call();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        if (obj !=null){
            setEx(key, obj, seconds);
        }
        return obj;
    }

    private void closeJedis(BinaryJedisCommands cmd) {
        if (cmd instanceof Jedis) {
            ((Jedis) cmd).close();
        } else if (cmd instanceof ShardedJedisPool) {
            ((ShardedJedisPool) cmd).close();
        } else {
            throw new RuntimeException("class type not supported! type is " + cmd.getClass());
        }
    }

}
