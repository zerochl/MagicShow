package com.zero.magicshow.core;

import com.zero.magicshow.core.camera.CameraEngine;
import com.zero.magicshow.core.filter.utils.MagicFilterType;
import com.zero.magicshow.common.utils.SavePictureTask;
import com.zero.magicshow.common.utils.MagicParams;
import com.zero.magicshow.core.widget.MagicCameraView;
import com.zero.magicshow.core.widget.MagicImageView;
import com.zero.magicshow.common.base.MagicBaseView;
import com.zero.zerolib.manager.PostManager;

import java.io.File;

/**
 * Created by why8222 on 2016/2/25.
 */
public class MagicEngine {
    private static MagicEngine magicEngine;
    private float skinSmoothLevel;
    private float whiteSkinLevel;

    public static MagicEngine getInstance(){
        if(magicEngine == null)
            throw new NullPointerException("MagicEngine must be built first");
        else
            return magicEngine;
    }

    private MagicEngine(Builder builder){

    }

    public void setFilter(MagicFilterType type){
        MagicParams.magicBaseView.setFilter(type);
    }

    public MagicFilterType getFilterType(){
        return MagicParams.magicBaseView.getFilterType();
    }

    public void adjustFilter(float rang, MagicFilterType type){
        if(MagicParams.magicBaseView instanceof MagicImageView) {
            ((MagicImageView) MagicParams.magicBaseView).adjustFilter(rang,type);
        }
    }

    public void savePicture(File file, SavePictureTask.OnPictureSaveListener listener){
        SavePictureTask savePictureTask = new SavePictureTask(file, listener);
        MagicParams.magicBaseView.savePicture(savePictureTask);
    }

    public void startRecord(){
        if(MagicParams.magicBaseView instanceof MagicCameraView)
            ((MagicCameraView)MagicParams.magicBaseView).changeRecordingState(true);
    }

    public void stopRecord(){
        if(MagicParams.magicBaseView instanceof MagicCameraView)
            ((MagicCameraView)MagicParams.magicBaseView).changeRecordingState(false);
    }

    public void setBeautyLevel(int level){
        if(MagicParams.magicBaseView instanceof MagicCameraView && MagicParams.beautyLevel != level) {
            MagicParams.beautyLevel = level;
            ((MagicCameraView) MagicParams.magicBaseView).onBeautyLevelChanged();
        }
    }

    public void initBeauty(){
        if(MagicParams.magicBaseView instanceof MagicImageView) {
            PostManager.getInstance().postSlow(new Runnable() {
                @Override
                public void run() {
                    ((MagicImageView) MagicParams.magicBaseView).initMagicBeautify();
                }
            },0);
        }
    }

    public void uninitBeauty(){
        if(MagicParams.magicBaseView instanceof MagicImageView) {
            PostManager.getInstance().postSlow(new Runnable() {
                @Override
                public void run() {
                    ((MagicImageView) MagicParams.magicBaseView).uninitMagicBeautify();
                }
            },0);
        }
    }

    public void setSkinSmooth(float level){
        skinSmoothLevel = level;
        PostManager.getInstance().removeSlow(setSkinSmoothRun);
        PostManager.getInstance().postSlow(setSkinSmoothRun,0);
    }

    public void setWhiteSkin(float level){
        whiteSkinLevel = level;
        PostManager.getInstance().removeSlow(setWhiteSkinRun);
        PostManager.getInstance().postSlow(setWhiteSkinRun,0);
    }

    public void commitImage(){
        if(MagicParams.magicBaseView instanceof MagicImageView) {
            ((MagicImageView) MagicParams.magicBaseView).commit();
        }
    }

    public void restoreImage(){
        if(MagicParams.magicBaseView instanceof MagicImageView) {
            ((MagicImageView) MagicParams.magicBaseView).restore();
        }
    }

    private Runnable setSkinSmoothRun = new Runnable() {
        @Override
        public void run() {
            if(MagicParams.magicBaseView instanceof MagicImageView) {
                ((MagicImageView) MagicParams.magicBaseView).setSkinSmooth(skinSmoothLevel);
            }
        }
    };

    private Runnable setWhiteSkinRun = new Runnable() {
        @Override
        public void run() {
            if(MagicParams.magicBaseView instanceof MagicImageView) {
                ((MagicImageView) MagicParams.magicBaseView).setWhiteSkin(whiteSkinLevel);
            }
        }
    };

    public void switchCamera(){
        CameraEngine.switchCamera();
    }

    public static class Builder{

        public MagicEngine build(MagicBaseView magicBaseView) {
            MagicParams.context = magicBaseView.getContext();
            MagicParams.magicBaseView = magicBaseView;
            magicEngine = new MagicEngine(this);
            return magicEngine;
        }

        public Builder setVideoPath(String path){
            MagicParams.videoPath = path;
            return this;
        }

        public Builder setVideoName(String name){
            MagicParams.videoName = name;
            return this;
        }

    }
}
