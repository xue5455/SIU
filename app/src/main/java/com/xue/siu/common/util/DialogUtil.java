package com.xue.siu.common.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.xue.siu.common.view.progressbar.CustomProgressDialog;
import java.util.HashMap;

/**
 * Created by XUE on 2016/1/8.
 */
public class DialogUtil {
    private static final HashMap<Activity,CustomProgressDialog> dialogMap = new HashMap<>();

    private DialogUtil(){}

    //没有文字,默认物理返回键禁用
    public static void showProgressDialog(@NonNull Activity activity) {
        showProgressDialog(activity, -1);
    }

    //没有文字
    public static void showProgressDialog(@NonNull Activity activity, @NonNull boolean cancelable) {
        showProgressDialog(activity, -1, cancelable);
    }

    //有文字,默认物理返回键禁用
    public static void showProgressDialog(@NonNull Activity activity, @StringRes int msgId) {
        showProgressDialog(activity, -1, false);
    }

    public static void showProgressDialog(@NonNull final Activity activity,
                                          @StringRes int msgId,
                                          @NonNull boolean cancelable) {
        CustomProgressDialog progressDlg = dialogMap.get(activity);
        if (progressDlg == null || progressDlg.isCancelable() != cancelable) {
            if (progressDlg != null && progressDlg.isCancelable() != cancelable) {
                final CustomProgressDialog tmpProgressDlg = progressDlg;
                activity.runOnUiThread(new ProgressDialogRunnable(tmpProgressDlg, false));
            }
            progressDlg = new CustomProgressDialog(activity);
            progressDlg.setCancelable(cancelable);
            progressDlg.setCanceledOnTouchOutside(false);
            dialogMap.put(activity, progressDlg);

        }
        if (msgId < 0) {
            progressDlg.hideMessage();
        } else {
            progressDlg.setMessage(ResourcesUtil.getString(msgId));
        }
        activity.runOnUiThread(new ProgressDialogRunnable(progressDlg, true));
    }

    public static void hideProgressDialog(@NonNull Activity activity) {
        CustomProgressDialog progressDlg = dialogMap.get(activity);
        if (progressDlg != null) {
            activity.runOnUiThread(new ProgressDialogRunnable(progressDlg, false));
        }
    }

    private static class ProgressDialogRunnable implements Runnable {
        CustomProgressDialog progressDialog;
        boolean isShow;

        public ProgressDialogRunnable(CustomProgressDialog dialog, boolean isShow) {
            this.progressDialog = dialog;
            this.isShow = isShow;
        }

        @Override
        public void run() {
            if (progressDialog != null) {
                if (isShow && !progressDialog.isShowing()) {
                    progressDialog.show();
                    progressDialog.startRotate();
                } else if (!isShow && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog.stopRotate();
                }
            }
        }
    }
}
