package com.xue.siu.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;

import com.xue.siu.R;
import com.xue.siu.application.AppProfile;

import java.util.HashMap;

/**
 * Created by XUE on 2016/2/3.
 */
public class StaticLayoutManager {
    private HashMap<String, StaticLayout> layoutMap = new HashMap<>();
    //
    private TextPaint textPaint;
    private TextDirectionHeuristic textDir;
    private Layout.Alignment alignment;

    private Canvas dummyCanvas;

    private int hardCodeWidth;

    private StaticLayoutManager() {
        initLayout(AppProfile.getContext());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initLayout(Context context) {

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = context.getResources().getDisplayMetrics().density;
        textPaint.setTextSize(ResourcesUtil.getDimenPxSize(R.dimen.chat_content_text_size));
        textDir = TextDirectionHeuristics.LTR;
        alignment = Layout.Alignment.ALIGN_NORMAL;
        hardCodeWidth = ScreenUtil.getDisplayWidth() - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_send_item_padding_small) - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_send_item_padding_big) - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_text_default_padding) - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_text_send_padding_right) - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_portrait_size) - ResourcesUtil.
                getDimenPxSize(R.dimen.chat_portrait_content_margin);
        dummyCanvas = new Canvas();
    }

    public StaticLayout getLayout(String content, int color,long timeStamp) {
        StringBuilder key = new StringBuilder(content);
        key.append(timeStamp);
        if (layoutMap.containsKey(key))
            return layoutMap.get(key);
        else {
            SpannableStringBuilder ssb = TextUtil.generateSpannableString(AppProfile.getContext(), content);
            StaticLayout layout = generateLayout(ssb, color);
            layout.draw(dummyCanvas);
            layoutMap.put(content, layout);
            return layout;
        }
    }

    private StaticLayout generateLayout(SpannableStringBuilder ssb, int color) {
        textPaint.setColor(color);
        float width = Math.min(Layout.getDesiredWidth(ssb, 0, ssb.length(), textPaint), hardCodeWidth);
        TextPaint paint = new TextPaint(textPaint);
        paint.setColor(color);
        StaticLayout layout = new StaticLayout(ssb, paint, (int) width, alignment, 1.0f, 0f, true);
        return layout;
    }

    public void clear() {
        layoutMap.clear();
    }


    private static StaticLayoutManager INSTANCE = null;

    public static StaticLayoutManager getInstance() {
        if (INSTANCE == null) {
            synchronized (StaticLayoutManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new StaticLayoutManager();
            }
        }
        return INSTANCE;
    }
}
