package com.netease.hearttouch.htrecycleview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *recycleviewholder注解
 * Created by zhengwen on 15-6-16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TRecycleViewHolderAnnotation {
    public int resId() default 0;
}


