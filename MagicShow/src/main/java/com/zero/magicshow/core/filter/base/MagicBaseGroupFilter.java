package com.zero.magicshow.core.filter.base;


import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.zero.magicshow.common.utils.OpenGlUtils;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;


public class MagicBaseGroupFilter extends GPUImageFilter{
	protected static int[] frameBuffers = null;
    protected static int[] frameBufferTextures = null;
    private int frameWidth = -1;
    private int frameHeight = -1;
    protected List<GPUImageFilter> filters;
    
    public MagicBaseGroupFilter(List<GPUImageFilter> filters){
    	this.filters = filters;
    }
    
	@Override
    public void onDestroy() {
        for (GPUImageFilter filter : filters) {
            filter.destroy();
        }
        destroyFramebuffers();
    }
    
    @Override
    public void init() {
        for (GPUImageFilter filter : filters) {
            filter.init();
        }
    }
    
    @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).onInputSizeChanged(width, height);
        }
        if(frameBuffers != null && (frameWidth != width || frameHeight != height || frameBuffers.length != size-1)){
			destroyFramebuffers();
			frameWidth = width;
			frameHeight = height;
		}
        if (frameBuffers == null) {
        	frameBuffers = new int[size];
            frameBufferTextures = new int[size];
            for (int i = 0; i < size; i++) {
                GLES20.glGenFramebuffers(1, frameBuffers, i);

                GLES20.glGenTextures(1, frameBufferTextures, i);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }

//    public void onDisplaySizeChanged(final int width, final int height) {
//        super.onDisplaySizeChanged(width, height);
//        int size = filters.size();
//        for (int i = 0; i < size; i++) {
//            filters.get(i).onDisplaySizeChanged(width, height);
//        }
//    }
    @Override
    public int onDrawFrame(final int textureId, final FloatBuffer cubeBuffer,
    		final FloatBuffer textureBuffer) {
    	if (frameBuffers == null || frameBufferTextures == null) {
            return OpenGlUtils.NOT_INIT;
        }
    	int size = filters.size();
        int previousTexture = textureId;
        for (int i = 0; i < size; i++) {
        	GPUImageFilter filter = filters.get(i);
            boolean isNotLast = i < size - 1;
            if (isNotLast) {
            	GLES20.glViewport(0, 0, mIntputWidth, mIntputHeight);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glClearColor(0, 0, 0, 0);
                filter.onDrawFrame(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                previousTexture = frameBufferTextures[i];
            }else{
            	GLES20.glViewport(0, 0, mOutputWidth, mOutputHeight);
            	filter.onDrawFrame(previousTexture, cubeBuffer, textureBuffer);
            }
        }
    	return OpenGlUtils.ON_DRAWN;
    }
    
    @Override
    public int onDrawFrame(final int textureId) {
//        return onDrawFrame(textureId,mGLCubeBuffer,mGLTextureBuffer);
    	if (frameBuffers == null || frameBufferTextures == null) {
            return OpenGlUtils.NOT_INIT;
        }
        int size = filters.size();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//        Log.e("HongLi","filters size:" + size + "mOutputWidth:" + mOutputWidth + ";mOutputHeight:" + mOutputHeight + ";mIntputWidth:" + mIntputWidth + ";mIntputHeight:" + mIntputHeight);
        int previousTexture = textureId;
        GLES20.glViewport(0,0,mOutputWidth,mOutputHeight);
        for (int i = 0; i < size; i++) {
        	GPUImageFilter filter = filters.get(i);
            boolean isNotLast = i < size - 1;
            if (isNotLast) {
                //绑定FrameBuffer
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                //为FrameBuffer挂载Texture[1]来存储颜色
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);
                //绑定FrameBuffer后的绘制会绘制到frameBufferTextures[i]上了
                filter.onDrawFrame(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
                //解除绑定位置很重要，如果下面没有纹理绘制了不能解除，否则获取纹理会出问题
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                previousTexture = frameBufferTextures[i];
            }else{
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                //为FrameBuffer挂载Texture[1]来存储颜色
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);
            	filter.onDrawFrame(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
            }
        }
        //费时费力，每次都获取bitmap
//        GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
//        GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
//        int nextTextureId = textureId;
//        IntBuffer ib = IntBuffer.allocate(mOutputWidth * mOutputHeight);
//        Bitmap mBitmap = Bitmap.createBitmap(mOutputWidth, mOutputHeight, Bitmap.Config.ARGB_8888);
//        for(GPUImageFilter imageFilter:filters){
//            if(!imageFilter.hasChange()){
//                continue;
//            }
//            imageFilter.onDrawFrame(nextTextureId,mGLCubeBuffer, mGLTextureBuffer);
//            GLES20.glReadPixels(0, 0, mOutputWidth, mOutputHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
//            mBitmap.copyPixelsFromBuffer(IntBuffer.wrap(ib.array()));
//            nextTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE, false);
//        }
//        mBitmap.recycle();
    	return OpenGlUtils.ON_DRAWN;
    }

    public void onDrawFrameNormal(final int textureId,int width,int height){
        int nextTextureId = textureId;
        IntBuffer ib = IntBuffer.allocate(width * height);
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(GPUImageFilter imageFilter:filters){
            imageFilter.onDrawFrame(nextTextureId,mGLCubeBuffer, mGLTextureBuffer);
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
            mBitmap.copyPixelsFromBuffer(IntBuffer.wrap(ib.array()));
            nextTextureId = OpenGlUtils.loadTexture(mBitmap, OpenGlUtils.NO_TEXTURE, false);
        }
        mBitmap.recycle();
    }
    
    private void destroyFramebuffers() {
        if (frameBufferTextures != null) {
            GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
            frameBufferTextures = null;
        }
        if (frameBuffers != null) {
            GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffers = null;
        }
    }
    
    public int getSize(){
    	return filters.size();
    }

    public GPUImageFilter getFristFilter(){
        return filters.get(0);
    }
}
