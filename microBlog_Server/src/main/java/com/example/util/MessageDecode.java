package com.example.util;

import com.alibaba.fastjson.JSON;
import com.example.entity.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecode implements Decoder.Text<Message> {
    @Override
    public Message decode(String message) throws DecodeException {
        return JSON.parseObject(message, Message.class);
    }

    @Override
    public boolean willDecode(String message) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
