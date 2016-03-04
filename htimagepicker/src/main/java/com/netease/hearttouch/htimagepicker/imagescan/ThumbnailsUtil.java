package com.netease.hearttouch.htimagepicker.imagescan;

import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.constants.C;

import java.io.File;
import java.util.HashMap;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ThumbnailsUtil {
    private static HashMap<Integer, String> sId2Path = new HashMap<>();
    private static ThumbnailsUtil sInstance;

    private ThumbnailsUtil(){}

    public static ThumbnailsUtil getInstance() {
        if (sInstance == null) {
            sInstance = new ThumbnailsUtil();
        }
        return sInstance;
    }

    public static String getThumbnailWithImageID(int key, String defalt){
        if(sId2Path == null || !sId2Path.containsKey(key)) {
            return defalt;
        }

        try{
            String thumbFilePath = sId2Path.get(key);
            if (TextUtils.isEmpty(thumbFilePath)){
                return defalt;
            }

            String thumbPath = thumbFilePath.substring(C.UrlPrefix.FILE.length());
            File file = new File(thumbPath);
            if(file.exists()){
                return thumbFilePath;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

//		Log.e("PICKER", key + ":NO MAPPING FILE NOT EXIST");
        return defalt;
    }

    public static void put(Integer key, String value){
        sId2Path.put(key, value);
    }

    public static void clear(){
        sId2Path.clear();
    }
}
