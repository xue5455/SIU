package com.xue.siu.common.util.string;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by zyl06 on 6/6/15.
 */
public class StringUtil {
    public static boolean isMatch(String expression, String input) {
        CharSequence inputStr = input;
        /*创建Pattern*/
        Pattern pattern = Pattern.compile(expression);
        /*将Pattern 以参数传入Matcher作Regular expression*/
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static boolean isEmailPrefix(String email) {
        String expression = "^([a-zA-Z0-9_\\-]+)$";
        return isMatch(expression, email);
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String expression = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return isMatch(expression, email);
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        return isMatch("[0-9]*", str);
    }

    //判断手机格式是否正确
    /*
    130 1 2 3 4 5 6 7 8 9
    145 7
    150 1 2 3 5 6 7 8 9 
    170 5 6 7 8
    18 0 1 2 3 4 5 6 7 8 9
    * */
    public static boolean isMobileNO(String mobiles) {
        String expression = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0,5-8])|(14[5,7]))\\d{8}$";
        return isMatch(expression, mobiles);
    }

    public boolean isChinese(String str) {
        return false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int getLength(String s) {
        int valueLength = 0;
        String chinese = "^([\u4E00-\u9FA5\uF900-\uFA2D]+)$";

        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为2
                valueLength += 2;
            } else {
                // 其他字符长度为1
                valueLength += 1;
            }
        }
        //进位取整
        return valueLength;
    }

    /**
     * 四舍五入获取真实字数
     */
    public static int getRealLength(String s) {
        int realLenght = (int) (getLength(s) / 2.0f + 0.5f);
        return realLenght;
//        if (length > maxLength) { length = (int) ((length - maxLength) / 2.0f + 0.5f); 
//            mIntroContentLength.setText("" + (-length)); 
//        }

    }

    /**
     * 如果memberCount大于一万，如11000，返回1.1万；110100，返回11万
     */
    public static String getFormatCount(int memberCount) {
        String memberCountStr;
        DecimalFormat df = new DecimalFormat("#.#");
        if (memberCount < 10000)
            memberCountStr = memberCount + "";
        else
            memberCountStr = df.format(memberCount / 10000.0) + "万";
        return memberCountStr;
    }
    
    public static String format(String format, Object... args) {
        return String.format(format, args);
    }

    /**
     * 格式化时间戳为字符串
     * @param dataFormat 时间格式
     * @param timeStamp 时间戳
     * @return
     */
    public static String formatDate(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        return new SimpleDateFormat(dataFormat, Locale.getDefault()).format(new Date(timeStamp));
    }

    /**
     * yyyy-MM-dd   HH:mm
     *
     * @param timeStamp 时间戳
     */
    public static String formatDate(long timeStamp) {
        return formatDate("yyyy-MM-dd HH:mm", timeStamp);
    }

    /**
     * 过滤掉特殊字符
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        if (str == null) return "";
        String regEx = "[/\\:*?<>|\"\n\t]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String add(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<strings.length; i++) {
            if (!TextUtils.isEmpty(strings[i])) {
                sb.append(strings[i]);
            }
        }
        return sb.toString();
    }

    public static String[] split(String value, char delim) {
        int end = value.length();
        ArrayList res = new ArrayList();
        int start = 0;

        int i;
        for(i = 0; i < end; ++i) {
            if(value.charAt(i) == delim) {
                if(start == i) {
                    res.add("");
                } else {
                    res.add(value.substring(start, i));
                }

                start = i + 1;
            }
        }

        if(start == 0) {
            res.add(value);
        } else if(start != end) {
            res.add(value.substring(start, end));
        } else {
            for(i = res.size() - 1; i >= 0 && ((String)res.get(i)).isEmpty(); --i) {
                res.remove(i);
            }
        }

        return (String[])res.toArray(new String[res.size()]);
    }

    /**
     * 去掉float中无意义的0, 返回String
     * @param origin 输入的float
     * @return result
     */
    public static String filterZero(float origin) {
        //最大精度
        final double MAX_PRECISION = 1.0E-6;

        return origin - (int)origin < MAX_PRECISION ? Integer.toString((int) origin) : Float.toString(origin);
    }
}
