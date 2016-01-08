package com.xue.eventbus;


import event.EventBusException;

/**
 * Created by zyl06 on 9/14/15.
 */
public class HTEventBusException extends EventBusException {

    public HTEventBusException(String detailMessage) {
        super(detailMessage);
    }

    public HTEventBusException(Throwable throwable) {
        super(throwable);
    }

    public HTEventBusException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
