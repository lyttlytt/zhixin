package com.shuzhengit.zhixin.bean;

import java.io.Serializable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/11 09:37
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:rxbus发送事件,用来区分事件类型
 */

public class EventType<T> implements Serializable {
    private Integer eventType;
    private T event;
    private Integer currentItem;

    public EventType(Integer eventType, T event) {
        this.eventType = eventType;
        this.event = event;
    }

    public EventType(Integer eventType, T event, Integer currentItem) {
        this.eventType = eventType;
        this.event = event;
        this.currentItem = currentItem;
    }

    public Integer getEventType() {
        return eventType;
    }

    public T getEvent() {
        return event;
    }

    public Integer getCurrentItem() {
        return currentItem;
    }
}
