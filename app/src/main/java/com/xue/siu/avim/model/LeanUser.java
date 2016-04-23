package com.xue.siu.avim.model;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;

/**
 * Created by XUE on 2016/1/19.
 */
public class LeanUser extends AVUser {
    public static final String KEY_PORTRAIT_URL = "portraitUrl";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_NICKNAME = "nickname";
    private String portraitUrl;//头像地址
    private String gender;//性别

    public static String getCurrentUserId() {
        LeanUser currentUser = getCurrentUser(LeanUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String fetchPortraitUrl() {
        AVFile avatar = getAVFile(KEY_PORTRAIT_URL);
        if (avatar != null) {
            return avatar.getUrl();
        } else {
            return null;
        }
    }

    /**
     * @param path         图片文件硬盘地址
     * @param saveCallback
     */
    public void savePortrait(String path, final SaveCallback saveCallback) {
        final AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(getUsername(), path);
            put(KEY_PORTRAIT_URL, file);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        saveInBackground(saveCallback);
                    } else {
                        if (null != saveCallback) {
                            saveCallback.done(e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LeanUser getCurrentUser() {
        return getCurrentUser(LeanUser.class);
    }

    /**
     * 取关
     *
     * @param friendId
     * @param saveCallback
     */
    public void stopFollowFriend(String friendId, final SaveCallback saveCallback) {
        unfollowInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    public static void register(String acc, String psw,
                                String gender, String nickname,
                                SignUpCallback callback) {
        AVUser user = new AVUser();
        user.setUsername(acc);
        user.setPassword(psw);
        user.put(KEY_PORTRAIT_URL, "");
        user.put(KEY_GENDER, gender);
        user.put(KEY_NICKNAME, nickname);
        user.signUpInBackground(callback);
    }
}
