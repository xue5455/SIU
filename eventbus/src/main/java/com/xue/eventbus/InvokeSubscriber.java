package com.xue.eventbus;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import event.IInvokeSubscriber;


/**
 * Created by zyl06 on 9/15/15.
 */
public class InvokeSubscriber implements IInvokeSubscriber {
    private Map<Field, Object> mEventFields = null;

    @Override
    public void invokeSubscriberBefore(Method method, Object subscriber, Object event) {
        if (!(event instanceof HTBaseEvent)) {
            throw new HTEventBusException("Only can handle event with type HTBaseEvent");
        }

        if (!(event instanceof HTMutableEvent)) {
            captureEventFieldsBefore(event);
        }
    }

    @Override
    public void invokeSubscriberAfter(Method method, Object subscriber, Object event) {
        HTBaseEvent baseEvent = (HTBaseEvent)event;
        baseEvent.paths.add(method.toString());

        if (!(event instanceof HTMutableEvent)) {
            captureEventFieldsAfter(baseEvent);
        }
    }

    private void captureEventFieldsBefore(Object event) {
        if (mEventFields == null) {
            mEventFields = new HashMap<Field, Object>();
        } else {
            mEventFields.clear();
        }

        Class<?> clazz = event.getClass();
        while (clazz != HTBaseEvent.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                try {
                    Object obj = field.get(event);
                    mEventFields.put(field, obj);
                } catch (IllegalAccessException e) {
                    Log.e(HTEventBus.TAG, e.toString());
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    private void captureEventFieldsAfter(HTBaseEvent event) {
        Class<?> clazz = event.getClass();
        while (clazz != HTBaseEvent.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                try {
                    Object obj = field.get(event);
                    Object prevObj = mEventFields.get(field);
                    if ((obj == null && prevObj != null) || (obj != null && !obj.equals(prevObj))) {
                        throw new HTEventBusException("Only MutableEvent's instance can modify it's custom fields. \nCheck you code or change HTBaseEvent to MutableEvent");
                    }
                } catch (IllegalAccessException e) {
                    Log.e(HTEventBus.TAG, e.toString());
                }
            }

            clazz = clazz.getSuperclass();
        }
    }
}
