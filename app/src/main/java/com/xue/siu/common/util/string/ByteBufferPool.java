package com.xue.siu.common.util.string;

import android.os.Build.VERSION;
import android.util.SparseArray;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ByteBufferPool {

    // 最大的缓存数
    private static final int MAX_CACHE = 32 * 1024;

    private static TreeSet<Integer> mIndex = new TreeSet<Integer>();
    private static SparseArray<SoftReference<byte []>> mBufferPool = new SparseArray<SoftReference<byte[]>>();
    private static Set<Integer> mReleasedBuffer = new HashSet<Integer>();

    public synchronized static byte[] obtain(int length) {
        Integer real = higher(mIndex, length);
        if (real == null) {
            return new byte[length];
        } else {
            byte[] buffer = mBufferPool.get(real).get();
            if (buffer == null) {
                buffer = new byte[length];
            }
            // remove cache
            mIndex.remove(real);
            mBufferPool.remove(real);
            return buffer;
        }
    }

    public synchronized static void recycle(byte[] buffer) {
        if (buffer == null) {
            return;
        }

        int len = buffer.length;
        if (checkSize(len)) {
            mIndex.add(len);
            mBufferPool.put(len, new SoftReference<byte[]>(buffer));
        }
    }

    private static boolean checkSize(int len) {
        int total = 0;
        for (Integer index : mIndex) {
            byte[] buffer = mBufferPool.get(index).get();
            if (buffer == null) {
                mReleasedBuffer.add(index);
            } else {
                total += buffer.length;
            }
        }

        for (Integer index : mReleasedBuffer) {
            mIndex.remove(index);
            mBufferPool.remove(index);
        }
        mReleasedBuffer.clear();

        return total + len <= MAX_CACHE;
    }

    private static Integer higher(TreeSet<Integer> set, Integer e) {
        if (VERSION.SDK_INT >= 9) {
            return set.higher(e);
        } else {
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer i = it.next();
                if (i > e) {
                    return i;
                }
            }
        }
        return null;
    }
}
