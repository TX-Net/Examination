package com.txnet.examination.service.impl;

import com.txnet.examination.service.IRedisHandleService;
import com.txnet.examination.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @Description: redis库操作接口实现
 * @Author: wangkun
 * @Date: 2021/3/10 14:36
 */
@Service
public class RedisHandleServiceImpl implements IRedisHandleService {
    //qps key
    private static final String QPS_KEY = "requestIp";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean insert(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public void update(String key, String value) {
        insert(key, value);
    }

    @Override
    public Object select(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    @Override
    public String qps(HttpServletRequest request) {
        String remoteAddr = IPUtil.getIpAddr(request);
        if (redisTemplate.opsForHash().hasKey(QPS_KEY, remoteAddr)) {
            return String.format("ip[%s]只能访问1次", remoteAddr);
        }
        redisTemplate.opsForHash().put(QPS_KEY,remoteAddr,"access");
        return "接口访问成功";
    }
}
