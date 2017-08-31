package com.zero.magicshow.core.beautify;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * Created by why8222 on 2016/2/29.
 */
public class MagicJni {
    static{
        System.loadLibrary("MagicJni");
    }
    //初始化
    public static native void jniInitMagicBeautify(ByteBuffer handler);
    public static native void jniUnInitMagicBeautify();
    //局部均方差磨皮
    public static native void jniStartSkinSmooth(float denoiseLevel);
    //log曲线美白
    public static native void jniStartWhiteSkin(float whitenLevel);
    //Bitmap操作
    public static native ByteBuffer jniStoreBitmapData(Bitmap bitmap);
    public static native void jniFreeBitmapData(ByteBuffer handler);
    public static native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);
}
