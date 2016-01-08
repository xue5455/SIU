package com.xue.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 9/14/15.
 */
public class HTBaseEvent {
    /* 软规范 */
    String desc; // 事件的描述信息
    /* 硬规范 */
    String from; // 事件是从哪里发出来的
    List<String> paths = new ArrayList<String>(); // 事件已经被传递的处理的各个处理函数所在类名+函数签名

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getPaths() {
        return paths;
    }
}
