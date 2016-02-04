package com.xue.siu.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.xue.siu.R;
import com.xue.siu.common.view.imagespan.OSImageSpan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XUE on 2016/2/1.
 */
public class TextUtil {
    private static final String TAG = "TextUtil";
    public static final String PATTERN_FACE = "\\[(\\w|\\d|_)+\\]";
    public static final int FACE_SIZE = ResourcesUtil.getDimenPxSize(R.dimen.chat_face_size);
    public static final Pattern FACE_PATTERN = Pattern.compile(PATTERN_FACE);

    public static SpannableString replaceTextWithImage(Context context, String src, int resId, int width, int height) {
        Bitmap bitmap = EmojiUtil.getSpanFromCache(src);
        if (bitmap == null) {
            bitmap = ResourcesUtil.getBitmap(resId, width, height);
            EmojiUtil.addFaceToCache(src, bitmap);
        }
        OSImageSpan imageSpan = new OSImageSpan(context, bitmap);
        SpannableString spannableString = new SpannableString(src);
        spannableString.setSpan(imageSpan, 0, src.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static OSImageSpan getImageSpan(Context context, String src) {
        Bitmap bitmap = EmojiUtil.getSpanFromCache(src);
        if (bitmap == null) {
            bitmap = ResourcesUtil.getBitmap(EmojiUtil.getDrawableRes(src), FACE_SIZE, FACE_SIZE);
            EmojiUtil.addFaceToCache(src, bitmap);
        }
        OSImageSpan imageSpan = new OSImageSpan(context, bitmap);
        return imageSpan;
    }

    public static SpannableString replaceTextWithImage(Context context, String src, int resId) {
        return replaceTextWithImage(context, src, resId, FACE_SIZE, FACE_SIZE);
    }

    public static SpannableStringBuilder generateSpannableString(Context context, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        Matcher matcher = FACE_PATTERN.matcher(content);
        while (matcher.find()) {
            String faceStr = matcher.group();
            if (EmojiUtil.contains(faceStr)) {
                spannableStringBuilder.setSpan(getImageSpan(context, faceStr), matcher.start(),
                        matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder test(Context context, String content) {
        int i = 0;
        int length = content.length();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        while (i < length) {
            int j = i;
            if (content.charAt(j) == '[' && j < length - 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(content.charAt(j));
                j++;
                while (j < length && content.charAt(j) != ']' && content.charAt(j) != '[') {
                    sb.append(content.charAt(j));
                    j++;
                }
                if (j < length) {
                    if (content.charAt(j) == ']') {
                        sb.append(content.charAt(j));
                        if (EmojiUtil.contains(sb.toString())) {
                            SpannableString spannableString = replaceTextWithImage(context, sb.toString()
                                    , EmojiUtil.getDrawableRes(sb.toString()));
                            spannableStringBuilder.append(spannableString);
                            i = j + 1;
                            continue;
                        } else {
                            spannableStringBuilder.append(sb.toString());
                            i = j + 1;
                            continue;
                        }
                    } else {
                        spannableStringBuilder.append(sb.toString());
                        i = j;
                        continue;
                    }
                } else {
                    spannableStringBuilder.append(sb.toString());
                    i = j;
                    continue;
                }
            } else {
                spannableStringBuilder.append(content.charAt(i));
                i++;
                continue;
            }
        }
        return spannableStringBuilder;
    }
}
