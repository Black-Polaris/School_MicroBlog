package com.example.util;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.CacheConstant;
import com.example.entity.Message;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket 的具体实现类
 */

@Component
@ServerEndpoint(value = "/websocket",
encoders = {MessageEncoder.class},
decoders = {MessageDecode.class})
public class MyWebSocket {

    private static RedisTemplate redisTemplate;

    private static MessageService messageService;

    @Autowired
    public void setService(RedisTemplate redisTemplate, MessageService messageService) {
        MyWebSocket.redisTemplate = redisTemplate;
        MyWebSocket.messageService = messageService;
    }

    // 存放每个客户端的websocket对象
    private static CopyOnWriteArrayList<MyWebSocket> webSockets = new CopyOnWriteArrayList<MyWebSocket>();

    // 与某个客户端的连接会话，通过它向客户端发送数据
    private Session session;

    // 连接建立成功调用方法
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        webSockets.add(this);
        System.out.println("新连接加入,在線人數為：" + webSockets.size());
    }

    // 连接关闭调用方法
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        System.out.println("当前用户连接关闭");
    }

    // 发送错误时调用
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("出错");
        error.printStackTrace();
    }

    // 接受客户端消息后调用方法
    @OnMessage
    public void onMessage(Message message, Session session) throws IOException {
        System.out.println("客户端信息：" + message.getMessage());
        messageService.save(message);
        Message newMessage = messageService.getOne(new QueryWrapper<Message>().eq("user_id", message.getUserId()).orderByDesc("create_date").last("Limit 1"));
        this.redisTemplate.opsForList().rightPush(CacheConstant.Message, newMessage);

        // 群发消息
        for (MyWebSocket item : webSockets) {
            // 异步发送消息
            item.session.getBasicRemote().sendText(JSONUtil.toJsonStr(newMessage));
        }
    }

}
