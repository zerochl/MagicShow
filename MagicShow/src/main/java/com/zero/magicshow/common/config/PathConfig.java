package com.zero.magicshow.common.config;

import android.os.Environment;

/**
 * Created by hongli on 2017/8/30.
 */

public class PathConfig {
    private static String SD_DIRECTORY = "/Android/data/com.zero.lib/cache";
    private static String tempCache = Environment.getExternalStorageDirectory().getAbsolutePath() + SD_DIRECTORY;
    public static void setTempCache(String path){
        tempCache = path;
    }

    public static String getTempPath(){
        return tempCache;
    }
}
