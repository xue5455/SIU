package com.xue.siu.avim.util;

import com.avos.avoscloud.AVObject;
import com.xue.siu.common.util.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by XUE on 2016/3/4.
 */
public class ModelParser {
    public static Object parse(AVObject avObject, Class clazz) {
        Field[] filed = clazz.getDeclaredFields();
        try {
            Object result = clazz.newInstance();
            LogUtil.i("xxj","parse");
            for (int i = 0; i < filed.length; i++) {
                filed[i].setAccessible(true);
                String name = filed[i].getName();
                LogUtil.i("xxj","fileName is " + name);
                String methodName = "set" + name;
                Class paramType = filed[i].getType();
                Method method = clazz.getMethod(methodName, new Class[]{paramType});
                Object object = avObject.get(name);
                if (object != null)
                    method.invoke(result, new Object[]{avObject.get(name)});
            }
            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static AVObject parseToAV(Object object, String avObjectName) {
        AVObject avObject = new AVObject(avObjectName);

        return avObject;
    }
}
