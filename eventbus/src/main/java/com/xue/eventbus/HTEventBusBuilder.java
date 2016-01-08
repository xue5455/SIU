package com.xue.eventbus;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import event.EventBus;
import event.EventBusBuilder;


/**
 * Created by zyl06 on 9/11/15.
 */
public class HTEventBusBuilder {
    EventBusBuilder mEventBusBuilder = null;
    List<Class<?>> skipMethodVerificationForClasses;

    HTEventBusBuilder(EventBusBuilder eventBusBuilder) {
        if (mEventBusBuilder == null) {
            throw new NullPointerException("eventBusBuilder cannot be null in HTEventBusBuilder's constructor");
        }
        mEventBusBuilder = eventBusBuilder;
    }

    /** Default: true */
    public HTEventBusBuilder logSubscriberExceptions(boolean logSubscriberExceptions) {
        mEventBusBuilder.logSubscriberExceptions(logSubscriberExceptions);
        return this;
    }

    /** Default: true */
    public HTEventBusBuilder logNoSubscriberMessages(boolean logNoSubscriberMessages) {
        mEventBusBuilder.logNoSubscriberMessages(logNoSubscriberMessages);
        return this;
    }

    /** Default: true */
    public HTEventBusBuilder sendSubscriberExceptionEvent(boolean sendSubscriberExceptionEvent) {
        mEventBusBuilder.sendSubscriberExceptionEvent(sendSubscriberExceptionEvent);
        return this;
    }

    /** Default: true */
    public HTEventBusBuilder sendNoSubscriberEvent(boolean sendNoSubscriberEvent) {
        mEventBusBuilder.sendNoSubscriberEvent(sendNoSubscriberEvent);
        return this;
    }

    /**
     * Fails if an subscriber throws an exception (default: false).
     * <p/>
     * Tip: Use this with BuildConfig.DEBUG to let the app crash in DEBUG mode (only). This way, you won't miss
     * exceptions during development.
     */
    public HTEventBusBuilder throwSubscriberException(boolean throwSubscriberException) {
        mEventBusBuilder.throwSubscriberException(throwSubscriberException);
        return this;
    }

    /**
     * By default, EventBus considers the event class hierarchy (subscribers to super classes will be notified).
     * Switching this feature off will improve posting of events. For simple event classes extending Object directly,
     * we measured a speed up of 20% for event posting. For more complex event hierarchies, the speed up should be
     * >20%.
     * <p/>
     * However, keep in mind that event posting usually consumes just a small proportion of CPU time inside an app,
     * unless it is posting at high rates, e.g. hundreds/thousands of events per second.
     */
    public HTEventBusBuilder eventInheritance(boolean eventInheritance) {
        mEventBusBuilder.eventInheritance(eventInheritance);
        return this;
    }


    /**
     * Provide a custom thread pool to EventBus used for async and background event delivery. This is an advanced
     * setting to that can break things: ensure the given ExecutorService won't get stuck to avoid undefined behavior.
     */
    public HTEventBusBuilder executorService(ExecutorService executorService) {
        mEventBusBuilder.executorService(executorService);
        return this;
    }

    /**
     * Method name verification is done for methods starting with onEvent to avoid typos; using this method you can
     * exclude subscriber classes from this check. Also disables checks for method modifiers (public, not static nor
     * abstract).
     */
    public HTEventBusBuilder skipMethodVerificationFor(Class<?> clazz) {
        // do the same things to mEventBusBuilder.skipMethodVerificationFor(clazz)
        if (skipMethodVerificationForClasses == null) {
            skipMethodVerificationForClasses = new ArrayList<Class<?>>();
        }
        skipMethodVerificationForClasses.add(clazz);

        mEventBusBuilder.skipMethodVerificationFor(clazz);
        return this;
    }

    /**
     * Installs the default HTEventBus returned by {@link HTEventBus#getDefault()} using this builders' values. Must be
     * done only once before the first usage of the default HTEventBus.
     *
     * @throws HTEventBusException if there's already a default HTEventBus instance in place
     */
    public HTEventBus installDefaultTouchEvent() {
        synchronized (HTEventBus.class) {
            if (HTEventBus.defaultInstance != null) {
                throw new HTEventBusException("Default instance already exists." +
                        " It may be only set once before it's used the first time to ensure consistent behavior.");
            }
            HTEventBus.defaultInstance = build();
            return HTEventBus.defaultInstance;
        }
    }

    /** Builds an EventBus based on the current configuration. */
    public HTEventBus build() {
        EventBus eventBus = mEventBusBuilder.build();
        return new HTEventBus(eventBus);
    }
}
