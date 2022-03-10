package com.example.service;

import com.example.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RankService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BlogService blogService;

    /**
     *  1.定时5分钟，刷新微博热度
     *  2.定时1小时合并统计 天、周、月的排行榜
     */
    public void init(){
        // 1.定时5分钟，刷新微博热度
        new Thread(() -> this.refreshDataHour()).start();
        // 2.定时1小时合并统计 天、周、月的排行榜
        new Thread(() -> this.refreshData()).start();
    }

    // 定时5分钟，刷新微博热度
    private void refreshDataHour() {
        while (true) {
            this.refreshHour();
            try {
                Thread.sleep(5 * 1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // 刷新小时热度
    private void refreshHour() {
        // 计算当前的小时key
        long hour = System.currentTimeMillis()/(1000*60*60);
        // TODO
        blogService.list().forEach(blog -> {
            String relayKey = "relay:" + blog.getId();
            Map relayMap = new HashMap();
            relayMap.put("relaySize", this.redisTemplate.opsForSet().size(relayKey));
            relayMap.put("isRelay", this.redisTemplate.opsForSet().isMember(relayKey, ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
            blog.setRelay(relayMap);
            String commentKey = "comment:" + blog.getId();
            blog.setComment(this.redisTemplate.opsForValue().size(commentKey));
            String loveKey = "love:" + blog.getId();
            Map loveMap = new HashMap();
            loveMap.put("loveSize", this.redisTemplate.opsForSet().size(loveKey));
            loveMap.put("isLove", this.redisTemplate.opsForSet().isMember(loveKey, ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
            blog.setLove(loveMap);
            this.redisTemplate.opsForZSet().incrementScore("rank:hour:" + hour, blog,
                    (this.redisTemplate.opsForSet().size("relay:" + blog.getId())  == 0 ? 0 : this.redisTemplate.opsForSet().size("relay:" + blog.getId()) )+
                          this.redisTemplate.opsForValue().size("comment:" + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForValue().size("comment:" + blog.getId()) +
                          this.redisTemplate.opsForSet().size("love:" + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size("love:" + blog.getId()));
        });

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
            String key = "rank:hour:" + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的世界key，并且向后推23个小时，共计近24小时，求出并集并存入key:“rank:hour"中
        // redis ZUNIONSTORE 求并集
        this.redisTemplate.opsForZSet().unionAndStore("rank:hour:" + hour, otherKeys, "rank:day");
        // 设置当天的key 40 天过期，避免历史内存浪费
        for (int i= 0; i<24; i++) {
            String key = "rank:hour:" + (hour - i);
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
            String key = "rank:hour:" + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的时间key,并且向后推24*7-1个小时，共计近24*7个小时，求出并集并存入key:“rank:week"中
        this.redisTemplate.opsForZSet().unionAndStore("rank:hour:" + hour, otherKeys, "rank:week");
        System.out.println("周刷新完成--------------------");
    }

    private void refreshMonth() {
        long hour = System.currentTimeMillis()/(1000*60*60);
        List<String> otherKeys = new ArrayList<>();
        // 算出近30天内的key
        for (int i = 1; i<24*30-1; i++) {
            String key = "rank:hour:" + (hour - i);
            otherKeys.add(key);
        }
        // 把当前的时间key,并且向后推24*30-1个小时，共计近24*30个吓死，求出并集并存入key:"rank:week"中
        this.redisTemplate.opsForZSet().unionAndStore("rank:hour:" + hour, otherKeys, "rank:month");
        System.out.println("月刷新完成--------------------");
    }

}
