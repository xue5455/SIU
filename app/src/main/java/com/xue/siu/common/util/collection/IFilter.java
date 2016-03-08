package com.xue.siu.common.util.collection;

/**
 * Created by zyl06 on 11/10/15.
 */
public interface IFilter<T> {
    boolean onFilter(T item);
}
