package com.xue.siu.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.db.OrmDBHelper;
import com.xue.siu.db.bean.SIUMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/29.
 */
public class SIUMessageDao {
    private Context mContext;
    private Dao<SIUMessage, Integer> mMsgDaoOpe;
    private OrmDBHelper mHelper;

    public SIUMessageDao(Context context) {
        this.mContext = context;
        try {
            mHelper = OrmDBHelper.getHelper(context);
            mMsgDaoOpe = mHelper.getDao(SIUMessage.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条消息
     *
     * @param msg
     */
    public void add(SIUMessage msg) {
        try {
            LogUtil.d("ChatPresenter", "addMessage");
            mMsgDaoOpe.create(msg);
        } catch (SQLException e) {
            LogUtil.d("ChatPresenter", "error " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param conversationId
     * @return
     */
    public List<SIUMessage> query(String conversationId) {
        List<SIUMessage> msgList = new ArrayList<>();
        QueryBuilder<SIUMessage, Integer> queryBuilder = mMsgDaoOpe.queryBuilder();
        Where<SIUMessage, Integer> where = queryBuilder.where();
        try {
            where.eq(SIUMessage.COLUMN_CONVERSATION_ID, conversationId);
            msgList = queryBuilder.query();
            LogUtil.d("ChatPresenter", "query msg size " + msgList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            LogUtil.d("ChatPresenter", "query error " + e.getMessage());
        }
        return msgList;
    }

    /**
     * Delete Message
     *
     * @param msg
     */
    public void delete(SIUMessage msg) {
        try {
            mMsgDaoOpe.delete(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
