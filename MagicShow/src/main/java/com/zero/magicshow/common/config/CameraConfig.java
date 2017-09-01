package com.zero.magicshow.common.config;

/**
 * Created by hongli on 2017/9/1.
 */

public class CameraConfig {

    public static int orientation;

    public static int pictureWidth = 1280;

    public static int pictureHeight = 720;

    public static void setCameraConfig(int pictureWidth,int pictureHeight){
        CameraConfig.pictureWidth = pictureWidth;
        CameraConfig.pictureHeight = pictureHeight;
    }
}
