package com.zero.magicshow.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.zero.magicshow.common.iface.MagicListener;
import com.zero.magicshow.common.utils.OpenGlUtils;
import com.zero.magicshow.common.utils.Rotation;
import com.zero.magicshow.common.utils.SavePictureTask;
import com.zero.magicshow.common.utils.TextureRotationUtil;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;
import com.zero.magicshow.core.filter.utils.MagicFilterFactory;
import com.zero.magicshow.core.filter.utils.MagicFilterType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by why8222 on 2016/2/25.
 */
public abstract class MagicBaseView extends GLSurfaceView implements GLSurfaceView.Renderer{
    /**
     * 所选择的滤镜，类型为MagicBaseGroupFilter
     * 1.mCameraInputFilter将SurfaceTexture中YUV数据绘制到FrameBuffer
     * 2.filter将FrameBuffer中的纹理绘制到屏幕中
     */
    protected GPUImageFilter filter;

    /**
     * SurfaceTexure纹理id
     */
    protected int textureId = OpenGlUtils.NO_TEXTURE;

    /**
     * 顶点坐标
     */
    protected final FloatBuffer gLCubeBuffer;

    /**
     * 纹理坐标
     */
    protected final FloatBuffer gLTextureBuffer;

    /**
     * GLSurfaceView的宽高
     */
    protected int surfaceWidth, surfaceHeight;

    /**
     * 图像宽高
     */
    protected int imageWidth, imageHeight;

    protected ScaleType scaleType = ScaleType.FIT_XY;

    protected MagicListener magicListener;

    public MagicBaseView(Context context) {
        this(context, null);
    }

    public MagicBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gLCubeBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        gLCubeBuffer.put(TextureRotationUtil.CUBE).position(0);

        gLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        gLTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glDisable(GL10.GL_DITHER);
        GLES20.glClearColor(0,0, 0, 0);
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width, height);
        surfaceWidth = width;
        surfaceHeight = height;
        Log.e("HongLi","surfaceWidth:" + surfaceWidth + ";surfaceHeight:" + surfaceHeight);
        onFilterChanged();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    protected void setMagicListener(MagicListener magicListener){
        this.magicListener = magicListener;
    }

    protected void onFilterChanged(){
        if(filter != null) {
            filter.onDisplaySizeChanged(surfaceWidth, surfaceHeight);
            filter.onInputSizeChanged(imageWidth, imageHeight);
        }
    }

    public void setGLScaleType(ScaleType scaleType){
        this.scaleType = scaleType;
    }

    public void setFilter(final MagicFilterType type){
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (filter != null)
                    filter.destroy();
                filter = null;
                filter = MagicFilterFactory.initFilters(type);
                if (filter != null)
                    filter.init();
                onFilterChanged();
            }
        });
        requestRender();
    }

    public MagicFilterType getFilterType(){
        if(null == filter){
            return MagicFilterType.NONE;
        }
        return MagicFilterFactory.getCurrentFilterType();
    }

    public void reftreshDisplay(){
        deleteTextures();
        requestRender();
    }

    protected void deleteTextures() {
        if(textureId != OpenGlUtils.NO_TEXTURE){
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    GLES20.glDeleteTextures(1, new int[]{
                            textureId
                    }, 0);
                    textureId = OpenGlUtils.NO_TEXTURE;
                }
            });
        }
    }

    public abstract void savePicture(SavePictureTask savePictureTask);

    protected void adjustSize(int rotation, boolean flipHorizontal, boolean flipVertical){
        float[] textureCords = TextureRotationUtil.getRotation(Rotation.fromInt(rotation),
                flipHorizontal, flipVertical);
        float[] cube = TextureRotationUtil.CUBE;
        float ratio1 = (float)surfaceWidth / imageWidth;
        float ratio2 = (float)surfaceHeight / imageHeight;
        float ratioMax = Math.max(ratio1, ratio2);
        int imageWidthNew = Math.round(imageWidth * ratioMax);
        int imageHeightNew = Math.round(imageHeight * ratioMax);

        float ratioWidth = imageWidthNew / (float)surfaceWidth;
        float ratioHeight = imageHeightNew / (float)surfaceHeight;

        if(scaleType == ScaleType.CENTER_INSIDE){
            cube = new float[]{
                    TextureRotationUtil.CUBE[0] / ratioHeight, TextureRotationUtil.CUBE[1] / ratioWidth,
                    TextureRotationUtil.CUBE[2] / ratioHeight, TextureRotationUtil.CUBE[3] / ratioWidth,
                    TextureRotationUtil.CUBE[4] / ratioHeight, TextureRotationUtil.CUBE[5] / ratioWidth,
                    TextureRotationUtil.CUBE[6] / ratioHeight, TextureRotationUtil.CUBE[7] / ratioWidth,
            };
        }else if(scaleType == ScaleType.FIT_XY){

        }else if(scaleType == ScaleType.CENTER_CROP){
            float distHorizontal = (1 - 1 / ratioWidth) / 2;
            float distVertical = (1 - 1 / ratioHeight) / 2;
            textureCords = new float[]{
                    addDistance(textureCords[0], distVertical), addDistance(textureCords[1], distHorizontal),
                    addDistance(textureCords[2], distVertical), addDistance(textureCords[3], distHorizontal),
                    addDistance(textureCords[4], distVertical), addDistance(textureCords[5], distHorizontal),
                    addDistance(textureCords[6], distVertical), addDistance(textureCords[7], distHorizontal),
            };
        }
        gLCubeBuffer.clear();
        gLCubeBuffer.put(cube).position(0);
        gLTextureBuffer.clear();
        gLTextureBuffer.put(textureCords).position(0);
    }

    protected void getBitmapFromGL(final Bitmap bitmap, final boolean newTexture){
        queueEvent(new Runnable() {
            @Override
            public void run() {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] mFrameBuffers = new int[1];
                int[] mFrameBufferTextures = new int[1];
                GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
                GLES20.glGenTextures(1, mFrameBufferTextures, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);
                GLES20.glViewport(0, 0, width, height);
//                filter.onInputSizeChanged(width, height);
//                filter.onOutputSizeChanged(width,height);
                filter.onDisplaySizeChanged(imageWidth, imageHeight);
//                filter.onDisplaySizeChanged(surfaceWidth, surfaceHeight);
                int textureId;
                if(newTexture){
                    textureId = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE, true);
                }else{
                    textureId = MagicBaseView.this.textureId;
                }
                GLES20.glViewport(0, 0, width, height);
//                ((MagicBaseGroupFilter)filter).getFristFilter().onDrawFrame(textureId);
                long startTime = System.nanoTime() / 1000000;
                filter.onDrawFrame(textureId);
                Log.e("HongLi","绘制消耗的时间:" + (System.nanoTime() / 1000000 - startTime));
//                filter.onDrawFrameNormal(textureId,width, height);
                IntBuffer ib = IntBuffer.allocate(width * height);
                GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
                Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                Log.e("HongLi","glReadPixels width:" + width + ";height:" + height + ";textureId:" + textureId + ";bitmapWidth:" + mBitmap.getWidth());
                mBitmap.copyPixelsFromBuffer(IntBuffer.wrap(ib.array()));
//                BaseUtil.saveBitmap(mBitmap,"/sdcard/DCIM/test2.jpg");
                if(newTexture)
                    GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
                GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
                GLES20.glDeleteTextures(1, mFrameBufferTextures, 0);
                GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
//                GLES20.glViewport(0, 0, imageWidth, imageHeight);

                filter.destroy();
                filter.init();
                filter.onInputSizeChanged(imageWidth, imageHeight);
                onGetBitmapFromGL(mBitmap);
            }
        });
    }
    protected void onGetBitmapFromGL(Bitmap bitmap){

    }
    private float addDistance(float coordinate, float distance) {
        return coordinate == 0.0f ? distance : 1 - distance;
    }

    public enum  ScaleType{
        CENTER_INSIDE,
        CENTER_CROP,
        FIT_XY;
    }
}
