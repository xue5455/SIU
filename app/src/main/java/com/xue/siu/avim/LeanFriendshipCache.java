package com.xue.siu.avim;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by XUE on 2016/3/23.
 */
public class LeanFriendshipCache {
    private static LeanFriendshipCache instance;
    private List<AVUser> followerList;
    private List<AVUser> followeeList;
    private HashSet<String> followerSet;
    private HashSet<String> followeeSet;

    private LeanFriendshipCache() {

    }

    public static LeanFriendshipCache getInstance() {
        if (instance == null) {
            synchronized (LeanFriendshipCache.class) {
                if (instance == null)
                    instance = new LeanFriendshipCache();
            }
        }
        return instance;
    }

    public synchronized void setFollowerCache(List<AVUser> followerList) {
        if (this.followerList == null) {
            this.followerList = new ArrayList<>();
            this.followerSet = new HashSet<>();
        }
        this.followerList.clear();
        this.followerList.addAll(followerList);
        for (AVUser avUser : this.followerList) {
            followerSet.add(avUser.getUsername());
        }
    }

    public synchronized void setFolloweeCache(List<AVUser> followeeList) {
        if (this.followeeList == null) {
            this.followeeList = new ArrayList<>();
            this.followeeSet = new HashSet<>();
        }
        this.followeeList.clear();
        this.followeeList.addAll(followeeList);
        for (AVUser avUser : this.followeeList) {
            followeeSet.add(avUser.getUsername());
        }
    }

    public boolean isFollowed(String userName) {
        return followeeSet.contains(userName);
    }

    public int getFollowerCount() {
        return followerList == null ? 0 : followerList.size();
    }

    public int getFolloweeCount() {
        return followeeList == null ? 0 : followeeList.size();
    }

    public boolean needQuery() {
        return followerList != null || followeeList == null;
    }
}
