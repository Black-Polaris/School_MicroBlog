package com.example.service;

import com.example.entity.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RankService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *  定时1小时合并统计 天、周、月的排行榜
     */
    @PostConstruct
    public void init(){
        // 定时1小时合并统计 天、周、月的排行榜
        new Thread(() -> this.refreshData()).start();
    }


    // 定时1小时合并统计 天、周、月的排行榜
    private void refreshData() {
        while (true) {
            // 刷新当天的统计数据
            this.refreshDay();
            // 刷新7天的统计数据
            this.refreshWeek();
            // 刷新30天的统计数据
            this.refreshMonth();
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 刷新当天的统计数据
    private void refreshDay() {
        long hour = System.currentTimeMillis() / (1000 *60 *60);
        List<String> otherKeys = new ArrayList<>();
        // 算出近24小时内的key
        for (int i = 1; i < 23; i++) {
            String key = CacheConstant.HOUR_KEY + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的世界key，并且向后推23个小时，共计近24小时，求出并集并存入key:“rank:hour"中
        // redis ZUNIONSTORE 求并集
        this.redisTemplate.opsForZSet().unionAndStore(CacheConstant.HOUR_KEY + hour, otherKeys, CacheConstant.DAY_KEY);
        // 设置当天的key 40 天过期，避免历史内存浪费
        for (int i= 0; i<24; i++) {
            String key = CacheConstant.HOUR_KEY + (hour - i);
            this.redisTemplate.expire(key, 40 , TimeUnit.DAYS);
        }
        System.out.println("天刷新完成--------------------");
    }

    // 刷新7天的统计数据
    private void refreshWeek() {
        long hour = System.currentTimeMillis()/(1000*60*60);
        List<String> otherKeys = new ArrayList<>();
        // 算出近7天内的key
        for (int i = 1; i<24*7-1; i++) {
            String key = CacheConstant.HOUR_KEY + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的时间key,并且向后推24*7-1个小时，共计近24*7个小时，求出并集并存入key:“rank:week"中
        this.redisTemplate.opsForZSet().unionAndStore(CacheConstant.HOUR_KEY + hour, otherKeys, CacheConstant.WEEK_KEY);
        System.out.println("周刷新完成--------------------");
    }

    private void refreshMonth() {
        long hour = System.currentTimeMillis()/(1000*60*60);
        List<String> otherKeys = new ArrayList<>();
        // 算出近30天内的key
        for (int i = 1; i<24*30-1; i++) {
            String key = CacheConstant.HOUR_KEY + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的时间key,并且向后推24*30-1个小时，共计近24*30个吓死，求出并集并存入key:"rank:week"中
        this.redisTemplate.opsForZSet().unionAndStore(CacheConstant.HOUR_KEY + hour, otherKeys, CacheConstant.MONTH_KEY);
        System.out.println("月刷新完成--------------------");
    }

}
