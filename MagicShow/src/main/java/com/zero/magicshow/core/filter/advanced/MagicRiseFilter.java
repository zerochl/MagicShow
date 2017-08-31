package com.zero.magicshow.core.filter.advanced;

import android.opengl.GLES20;

import com.zero.magicshow.R;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;
import com.zero.magicshow.common.utils.MagicParams;
import com.zero.magicshow.common.utils.OpenGlUtils;

public class MagicRiseFilter extends GPUImageFilter{
	private int[] inputTextureHandles = {-1,-1,-1};
	private int[] inputTextureUniformLocations = {-1,-1,-1};
	
	public MagicRiseFilter(){
		super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.rise));
	}
	
	public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        for(int i = 0; i < inputTextureHandles.length; i++)
        	inputTextureHandles[i] = -1;
    }
	
	protected void onDrawArraysAfter(){
		for(int i = 0; i < inputTextureHandles.length
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3));
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}
	  
	protected void onDrawArraysPre(){
		for(int i = 0; i < inputTextureHandles.length 
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3) );
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
			GLES20.glUniform1i(inputTextureUniformLocations[i], (i+3));
		}
	}
	
	public void onInit(){
		super.onInit();
		for(int i=0; i < inputTextureUniformLocations.length; i++){
			inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture"+(2+i));
		}
	}
	
	public void onInitialized(){
		super.onInitialized();
	    runOnDraw(new Runnable(){
		    public void run(){
		    	inputTextureHandles[0] = OpenGlUtils.loadTexture(MagicParams.context, "filter/blackboard1024.png");
				inputTextureHandles[1] = OpenGlUtils.loadTexture(MagicParams.context, "filter/overlaymap.png");
				inputTextureHandles[2] = OpenGlUtils.loadTexture(MagicParams.context, "filter/risemap.png");
		    }
	    });
	}
}