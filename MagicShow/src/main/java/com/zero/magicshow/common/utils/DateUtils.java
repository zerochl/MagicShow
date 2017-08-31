package com.zero.magicshow.common.utils;

import com.zero.zerolib.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hongli on 2017/8/30.
 */

public class DateUtils extends DateUtil{
    /**
     * 获取时间戳
     * @return 20180521172011
     */
    public static String getTimeStamp(){
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return sim.format(new Date());
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
