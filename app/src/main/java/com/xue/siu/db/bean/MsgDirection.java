package com.xue.siu.db.bean;

/**
 * Created by XUE on 2015/12/10.
 */
public enum MsgDirection {
    IN(1),
    OUT(2);
    private int direction;

    MsgDirection(int direction) {
        this.direction = direction;
    }
}
