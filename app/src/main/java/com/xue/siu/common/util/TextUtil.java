package com.xue.siu.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.xue.siu.common.view.imagespan.OSImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XUE on 2016/2/1.
 */
public class TextUtil {
    public static final String PATTERN_FACE = "\\[(\\w|\\d|_)+\\]";

    public static SpannableString replaceTextWithImage(Context context, String src, int resId, int width, int height) {
        Bitmap bitmap = ResourcesUtil.getBitmap(resId, width, height);
        OSImageSpan imageSpan = new OSImageSpan(context, bitmap);
        SpannableString spannableString = new SpannableString(src);
        spannableString.setSpan(imageSpan, 0, src.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString generateSpannableString(String content) {
        Pattern pattern = Pattern.compile(PATTERN_FACE);
        Matcher matcher = pattern.matcher(content);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);
        while (matcher.find()) {
            String faceStr = matcher.group();
            if (!EmojiUtil.contains(faceStr))
                continue;

        }
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//        int i = 0;
//        while (i < content.length()) {
//            if (content.charAt(i) == '[') {
//                StringBuilder sb = new StringBuilder();
//                int j = i;
//                while (content.charAt(j) != ']' && j < content.length()) {
//                    sb.append(content.charAt(j));
//                    j++;
//                }
//
//                if (j == content.length() - 1 && content.charAt(j) != ']') {
//                    sb.append(content.charAt(j));
//                } else {
//                    sb.append(content.charAt(j));
//                }
//            }
//        }
        return null;

    }

    public static boolean match(String str) {
        Pattern pattern = Pattern.compile(PATTERN_FACE);
        return pattern.matcher(str).matches();
    }
}
