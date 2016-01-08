package com.xue.eventbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import event.EventBus;

/**
 * Created by zyl06 on 9/11/15.
 */
public class HTEventBus {
    private EventBus mEventBus = null;
    static volatile HTEventBus defaultInstance = null;
    private final Set<Class<?>> mRegisterClazz = new HashSet<Class<?>>();
    private final Map<Object, Integer> mIntPriorities = new HashMap<Object, Integer>();
    private final Map<Object, HTPriority> mEnumPriorities = new HashMap<Object, HTPriority>();

    static final String TAG = "HTEventBus";

    public HTEventBus() {
        mEventBus = new EventBus();
        mEventBus.setInvokeSubscriber(new InvokeSubscriber());
    }

    HTEventBus(@NonNull EventBus eventBus) {
        mEventBus = eventBus;
        mEventBus.setInvokeSubscriber(new InvokeSubscriber());
    }

    public static HTEventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (HTEventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new HTEventBus();
                }
            }
        }
        return defaultInstance;
    }

    public static HTEventBusBuilder builder() {
        return new HTEventBusBuilder(EventBus.builder());
    }

    /** For unit test primarily. */
    public static void clearCaches() {
        EventBus.clearCaches();
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the {@link ThreadMode}
     * enums. For example, if a method is to be called in the UI/main thread by HTEventBus, it would be called
     * "onEventMainThread".
     */
    public void register(Object subscriber) {
        checkReceiverClass(subscriber.getClass());
        mEventBus.register(subscriber);
    }

    /**
     * Like {@link #register(Object)} with an additional subscriber priority to influence the order of event delivery.
     * Within the same delivery thread ({@link ThreadMode}), higher priority subscribers will receive events before
     * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
     * delivery among subscribers with different {@link ThreadMode}s!
     */
    public void register(Object subscriber, HTPriority priority) {
        checkReceiverClass(subscriber.getClass());
        checkPriority(subscriber, priority);

        mEventBus.register(subscriber, priority.value());
    }

    /**
     * Like {@link #register(Object)} with an additional subscriber priority to influence the order of event delivery.
     * Within the same delivery thread ({@link ThreadMode}), higher priority subscribers will receive events before
     * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
     * delivery among subscribers with different {@link ThreadMode}s!
     */
    public void register(Object subscriber, int priority) {
        checkReceiverClass(subscriber.getClass());
        checkPriority(subscriber, priority);

        mEventBus.register(subscriber, priority);
    }

    /**
     * Like {@link #register(Object)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(HTBaseEvent)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber) {
        checkReceiverClass(subscriber.getClass());

        mEventBus.register(subscriber);
    }

    /**
     * Like {@link #register(Object, int)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(HTBaseEvent)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber, HTPriority priority) {
        checkReceiverClass(subscriber.getClass());
        checkPriority(subscriber, priority);

        mEventBus.register(subscriber, priority.value());
    }

    /**
     * Like {@link #register(Object, int)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(HTBaseEvent)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber, int priority) {
        checkReceiverClass(subscriber.getClass());
        checkPriority(subscriber, priority);

        mEventBus.register(subscriber, priority);
    }

    public synchronized boolean isRegistered(Object subscriber) {
        return mEventBus.isRegistered(subscriber);
    }

    /** Unregisters the given subscriber from all event classes. */
    public synchronized void unregister(Object subscriber) {
        mIntPriorities.remove(subscriber);
        mEnumPriorities.remove(subscriber);

        mEventBus.unregister(subscriber);
    }

    /** Posts the given event to the event bus. */
    public void post(@NonNull HTBaseEvent event) {
        recordEventFromInfo(event);

        mEventBus.post(event);
    }

    /**
     * Called from a subscriber's event handling method, further event delivery will be canceled. Subsequent
     * subscribers
     * won't receive the event. Events are usually canceled by higher priority subscribers (see
     * {@link #register(Object, int)}). Canceling is restricted to event handling methods running in posting thread
     * {@link ThreadMode#PostThread}.
     */
    public void cancelEventDelivery(HTBaseEvent event) {
        mEventBus.cancelEventDelivery(event);
    }

    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access. This can be {@link #registerSticky(Object)} or
     * {@link #getStickyEvent(Class)}.
     */
    public void postSticky(@NonNull HTBaseEvent event) {
        recordEventFromInfo(event);

        mEventBus.postSticky(event);
    }

    /**
     * Gets the most recent sticky event for the given type.
     *
     * @see #postSticky(HTBaseEvent)
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        return mEventBus.getStickyEvent(eventType);
    }

    /**
     * Remove and gets the recent sticky event for the given event type.
     *
     * @see #postSticky(HTBaseEvent)
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        return mEventBus.removeStickyEvent(eventType);
    }

    /**
     * Removes the sticky event if it equals to the given event.
     *
     * @return true if the events matched and the sticky event was removed.
     */
    public boolean removeStickyEvent(Object event) {
        return mEventBus.removeStickyEvent(event);
    }

    /**
     * Removes all sticky events.
     */
    public void removeAllStickyEvents() {
        mEventBus.removeAllStickyEvents();
    }

    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        return mEventBus.hasSubscriberForEvent(eventClass);
    }

    ///////////////////////////////////////////////////////////
    // Additional functions
    /**
     * Register receiver class. Only registered receiver class, it's instance can register subscriber
     */
    public void registerReceiverClass(@NonNull Class<?> receiverClass) {
        mRegisterClazz.add(receiverClass);
    }

    /**
     * UnRegister receiver class. After that, its instance can not register subscriber.
     * Before that, the registered instance can still work
     */
    public void unregisterReceiverClass(@NonNull Class<?> receiverClass) {
        mRegisterClazz.remove(receiverClass);
    }

    /**
     * Gets if receiverClass is registered
     *
     * @return true if the receiverClass is registered before
     */
    public synchronized boolean isReceiverClassRegistered(@NonNull Class<?> receiverClass) {
        for (Iterator<Class<?>> iter = mRegisterClazz.iterator(); iter.hasNext(); ) {
            Class<?> clazz = iter.next();
            if (clazz.isAssignableFrom(receiverClass)) {
                return true;
            }
        }

        return false;
    }

    private void checkReceiverClass(@NonNull Class<?> receiverClass) {
        if (!isReceiverClassRegistered(receiverClass)) {
            throw new HTEventBusException("cannot register subscriber without regiser receiver class. \nPlease call regiserReceiverClass");
        }
    }

    private void checkPriority(Object subscriber, int priority) {
        if (priority < 0) {
            throw new HTEventBusException("Cannot register with priority < 0");
        }
        if (mEnumPriorities.size() > 0) {
            throw new HTEventBusException("Cannot register with both int and enum HTPriority");
        }
        if (mIntPriorities.containsValue(priority)) {
            Log.w(TAG, "Please donot register with same int priority");
        }
        mIntPriorities.put(subscriber, priority);
    }

    private void checkPriority(Object subscriber, HTPriority priority) {
        if (mIntPriorities.size() > 0) {
            throw new HTEventBusException("Cannot register with both int and enum HTPriority");
        }
        if (mEnumPriorities.containsValue(priority)) {
            Log.w(TAG, "Please donot register with same enum priority");
        }
        mEnumPriorities.put(subscriber, priority);
    }

    private void recordEventFromInfo(HTBaseEvent event) {
        try {
            event.from = Thread.currentThread().getStackTrace()[4].toString();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Sets if we will check whether inmutable event is modified or not
     *
     * @param checkInmutableEvent we will check whether inmutable event is modified or not
     */
    public void setCheckInmutableEvent(boolean checkInmutableEvent) {
        mEventBus.setCheckInmutableEvent(checkInmutableEvent);
    }

    /**
     * Gets if we will check whether inmutable event is modified or not
     *
     * @return true if we will check
     */
    public boolean isCheckInmutableEvent() {
        return mEventBus.isCheckInmutableEvent();
    }
}
