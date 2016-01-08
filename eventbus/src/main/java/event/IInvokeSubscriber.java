package event;

import java.lang.reflect.Method;

/**
 * Created by zyl06 on 9/15/15.
 */
public interface IInvokeSubscriber {
    void invokeSubscriberBefore(Method method, Object subscriber, Object event);
    void invokeSubscriberAfter(Method method, Object subscriber, Object event);
}
