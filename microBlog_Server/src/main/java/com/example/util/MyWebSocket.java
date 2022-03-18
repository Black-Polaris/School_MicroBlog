package com.example.util;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.CacheConstant;
import com.example.entity.Message;
import com.example.entity.Relation;
import com.example.service.MessageService;
import com.example.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * WebSocket 的具体实现类
 */

@Component
@ServerEndpoint(value = "/websocket/{id}",
encoders = {MessageEncoder.class},
decoders = {MessageDecode.class})
public class MyWebSocket {

    private static RedisTemplate redisTemplate;

    private static MessageService messageService;

    private static RelationService relationService;

    @Autowired
    public void setService(RedisTemplate redisTemplate, MessageService messageService, RelationService relationService) {
        MyWebSocket.redisTemplate = redisTemplate;
        MyWebSocket.messageService = messageService;
        MyWebSocket.relationService = relationService;
    }

    // 存放每个客户端的websocket对象
    private static Map<Long, Session> webSockets = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，通过它向客户端发送数据
    private Session session;

    // 连接建立成功调用方法
    @OnOpen
    public void onOpen(@PathParam("id") Long id, Session session) {
        this.session = session;

        webSockets.put(id, session);
        System.out.println("新连接加入,在線人數為：" + webSockets.size());
    }

    // 连接关闭调用方法
    @OnClose
    public void onClose() {
        for (Map.Entry<Long, Session> entry : webSockets.entrySet()) {
            if (this.session.getId().equals(entry.getValue().getId())) {
                webSockets.remove(entry.getKey());
                return;
            }
        }
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

        String key = LongStream.of(message.getFromId(), message.getToId())
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("-"));
        messageService.save(message);

        Message newMessage = messageService.getOne(new QueryWrapper<Message>().eq("from_id", message.getFromId()).eq("to_id", message.getToId()).orderByDesc("create_date").last("Limit 1"));
        if (newMessage.getToId() == 0) {
            this.redisTemplate.opsForList().rightPush(CacheConstant.Message, newMessage);
            // 群发消息
            Collection<Session> values = webSockets.values();
            for (Session o : values) {
                // 异步发送消息
                o.getBasicRemote().sendText(JSONUtil.toJsonStr(newMessage));
            }
        } else {
            Relation relation = new Relation();
            relation.setFromId(message.getFromId());
            relation.setToId(message.getToId());
            relation.setCreateDate(new Date());
            relationService.saveOrUpdate(relation);

            this.redisTemplate.opsForList().rightPush(CacheConstant.Message + key, newMessage);
            webSockets.get(Long.valueOf(newMessage.getFromId())).getBasicRemote().sendText(JSONUtil.toJsonStr(newMessage));
            // 判断被发送用户是否在线
            if (webSockets.get(Long.valueOf(newMessage.getToId())) != null) {
                webSockets.get(Long.valueOf(newMessage.getToId())).getBasicRemote().sendText(JSONUtil.toJsonStr(newMessage));
            }

        }

    }

}
