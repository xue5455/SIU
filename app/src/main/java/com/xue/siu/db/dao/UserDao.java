package com.xue.siu.db.dao;

import android.content.Context;

import com.avos.avoscloud.AVUser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.xue.siu.db.OrmDBHelper;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.db.bean.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/29.
 */
public class UserDao {
    private Context mContext;
    private Dao<User, Integer> mUserDaoOpe;
    private OrmDBHelper mHelper;

    public UserDao(Context context) {
        this.mContext = context;
        try {
            mHelper = OrmDBHelper.getHelper(context);
            mUserDaoOpe = mHelper.getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(AVUser avUser) {
        String account = avUser.getUsername();
        String nickname = avUser.get("nickname").toString();
        User user = new User("", nickname, account);
        try {
            mUserDaoOpe.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找当前数据库中的用户
     * @param account
     * @return
     */
    public User query(String account) {
        QueryBuilder<User, Integer> queryBuilder = mUserDaoOpe.queryBuilder();
        Where<User, Integer> where = queryBuilder.where();
        try {
            where.eq(User.COLUMN_ACCOUNT, account);
            return where.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * save conversation id to database
     * @param avUser
     * @param conversationId
     */
    public void saveConversationId(AVUser avUser,String conversationId){
        String account = avUser.getUsername();
        String nickname = avUser.get("nickname").toString();
        User user = new User("", nickname, account);
        user.setConversationId(conversationId);
        try {
            mUserDaoOpe.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
