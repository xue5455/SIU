package com.xue.siu.db.bean;

/**
 * Created by XUE on 2015/12/11.
 */
public enum MsgStatus {
    SENDING(1),//正在发送
    SENT_SUCCESS(2),//发送成功
    SENT_FAIL(3),//发送失败
    RECV_NOR(4),//收到的消息还未查看
    RECV_ACKED(5);//收到的消息已查看
    private int status;

    MsgStatus(int status) {
        this.status = status;
    }

    public static MsgStatus getStatus(int status) {
        switch (status) {
            case 1:
                return SENDING;
            case 2:
                return SENT_SUCCESS;
            case 3:
                return SENT_FAIL;
            case 4:
                return RECV_NOR;
            case 5:
                return RECV_ACKED;
            default:
                return null;
        }
    }
}
