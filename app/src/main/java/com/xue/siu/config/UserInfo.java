package com.xue.siu.config;

/**
 * Created by XUE on 2016/1/18.
 */
public class UserInfo {
    public static String userId;
    public static String password;
    public static String portraitUrl;
    public static boolean isLogged = false;//是否已登录

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserInfo.userId = userId;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserInfo.password = password;
    }

    public static String getPortraitUrl() {
        return portraitUrl;
    }

    public static void setPortraitUrl(String portraitUrl) {
        UserInfo.portraitUrl = portraitUrl;
    }

    public static boolean isLogged() {
        return isLogged;
    }

    public static void setIsLogged(boolean isLogged) {
        UserInfo.isLogged = isLogged;
    }
}
