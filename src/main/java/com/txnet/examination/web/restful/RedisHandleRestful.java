package com.txnet.examination.web.restful;

import com.txnet.examination.service.IRedisHandleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: redis库操作
 * @Author: wangkun
 * @Date: 2021/3/10 15:04
 */
@RestController
@RequestMapping("/api/redis")
@Slf4j
public class RedisHandleRestful {
    @Autowired
    private IRedisHandleService redisHandleService;

    @GetMapping("/get")
    public Object getValue(String key){
        return redisHandleService.select(key) == null? String.format("redis中不包含key为[%s]的值",key)
                :redisHandleService.select(key);
    }

    @PostMapping("/set")
    public Object setValue(String key,String value){
        return redisHandleService.insert(key,value);
    }

    @PutMapping("/update")
    public boolean updateValue(String key,String value){
        redisHandleService.update(key,value);
        return true;
    }

    @DeleteMapping("/delete")
    public boolean deleteValue(String key){
        redisHandleService.delete(key);
        return true;
    }

    @GetMapping("/qps")
    public String qps(HttpServletRequest request){
        return redisHandleService.qps(request);
    }

}
