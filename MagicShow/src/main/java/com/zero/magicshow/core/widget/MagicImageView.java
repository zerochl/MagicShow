package com.zero.magicshow.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;

import com.zero.magicshow.core.beautify.MagicJni;
import com.zero.magicshow.core.filter.advanced.MagicImageAdjustFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;
import com.zero.magicshow.core.filter.utils.MagicFilterType;
import com.zero.magicshow.common.utils.SavePictureTask;
import com.zero.magicshow.common.utils.OpenGlUtils;
import com.zero.magicshow.common.base.MagicBaseView;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by why8222 on 2016/2/25.
 */
public class MagicImageView extends MagicBaseView{

    private final GPUImageFilter imageInput;

    private ByteBuffer _bitmapHandler = null;

    private Bitmap originBitmap = null;

    public MagicImageView(Context context) {
        this(context, null);
    }

    public MagicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageInput = new GPUImageFilter();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        imageInput.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        adjustSize(0, false, false);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        if(textureId == OpenGlUtils.NO_TEXTURE)
            textureId = OpenGlUtils.loadTexture(getBitmap(), OpenGlUtils.NO_TEXTURE);
        if(filter == null)
            imageInput.onDrawFrame(textureId, gLCubeBuffer, gLTextureBuffer);
        else
            filter.onDrawFrame(textureId, gLCubeBuffer, gLTextureBuffer);
    }

    @Override
    public void savePicture(SavePictureTask savePictureTask) {
//        String tempFilePath = BaseUtil.getRandomTempImagePath();
//        BaseUtil.saveBitmap(originBitmap, BaseUtil.getRandomTempImagePath());
        savePictureTask.execute(originBitmap);
    }

    //还原
    public void restore(){
        if(filter != null){
            setFilter(MagicFilterType.NONE);
        }else{
            setImageBitmap(originBitmap);
        }
    }

    //应用
    public void commit(){
        if(filter != null){
            getBitmapFromGL(originBitmap, false);
            deleteTextures();
            setFilter(MagicFilterType.NONE);
        }else if(null != _bitmapHandler){
            originBitmap.recycle();
            originBitmap = MagicJni.jniGetBitmapFromStoredBitmapData(_bitmapHandler);
        }
    }

    protected void onGetBitmapFromGL(Bitmap bitmap){
        originBitmap = bitmap;
        storeBitmap(originBitmap, false);
    }

    protected void storeBitmap(Bitmap bitmap,boolean recyle){
//        if(_bitmapHandler != null)
//            freeBitmap();
//        _bitmapHandler = MagicJni.jniStoreBitmapData(bitmap);
        setImageBitmap(bitmap);
        if(recyle)
            bitmap.recycle();
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled())
            return;
        Log.e("HongLi","imageWidth:" + bitmap.getWidth() + ";imageHeight:" + bitmap.getHeight());
        setBitmap(bitmap);
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();
        adjustSize(0, false, false);
        requestRender();
    }

    public void initMagicBeautify(){
        if(_bitmapHandler == null){
            Log.e("MagicSDK", "please storeBitmap first!!");
            return;
        }
        MagicJni.jniInitMagicBeautify(_bitmapHandler);
    }

    public void uninitMagicBeautify(){
        if(_bitmapHandler == null){
            Log.e("MagicSDK", "please storeBitmap first!!");
            return;
        }
        MagicJni.jniUnInitMagicBeautify();
    }

    public void setBitmap(Bitmap bitmap){
        if(_bitmapHandler != null)
            freeBitmap();
        _bitmapHandler = MagicJni.jniStoreBitmapData(bitmap);
        originBitmap = bitmap;
    }

    public void freeBitmap(){
        if(_bitmapHandler == null)
            return;
        MagicJni.jniFreeBitmapData(_bitmapHandler);
        _bitmapHandler = null;
    }

    public Bitmap getBitmap(){
        if(_bitmapHandler == null)
            return null;
        return MagicJni.jniGetBitmapFromStoredBitmapData(_bitmapHandler);
    }

    public void setSkinSmooth(float level){
        if(_bitmapHandler == null)
            return;
        if(level > 10 || level < 0){
            Log.e("MagicSDK","Skin Smooth level must in [0,10]");
            return;
        }
        MagicJni.jniStartSkinSmooth(level);
        reftreshDisplay();
        if(magicListener != null)
            magicListener.onEnd();
    }

    public void setWhiteSkin(float level){
        if(_bitmapHandler == null)
            return;
        if(level > 5 || level < 0){
            Log.e("MagicSDK","Skin white level must in [1,5]");
            return;
        }
//        long startTime = System.nanoTime() / 1000000;
//        Log.e("HongLi","before jni:" + 0);
        MagicJni.jniStartWhiteSkin(level);
//        BaseUtil.saveBitmap(originBitmap,"/sdcard/DCIM/test2.jpg");
//        Log.e("HongLi","end jni:" + (System.nanoTime() / 1000000 - startTime));
        reftreshDisplay();
//        Log.e("HongLi","end refresh:" + (System.nanoTime() / 1000000 - startTime));
        if(magicListener != null)
            magicListener.onEnd();
    }

    public void adjustFilter(float range,MagicFilterType type){
        if(!(filter instanceof MagicImageAdjustFilter)){
            return;
        }
        ((MagicImageAdjustFilter)filter).setImageFilter(range,type);
        requestRender();
//        reftreshDisplay();
    }
}
