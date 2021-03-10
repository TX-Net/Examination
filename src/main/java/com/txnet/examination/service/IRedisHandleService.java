package com.txnet.examination.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: redis库操作接口
 * @Author: wangkun
 * @Date: 2021/3/10 14:32
 */
public interface IRedisHandleService {
    boolean insert(String key,String value);
    void delete(String... key);
    void update(String key,String value);
    Object select(String key);
    String qps(HttpServletRequest request);
}
