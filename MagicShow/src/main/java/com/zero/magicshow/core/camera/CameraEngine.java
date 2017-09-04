package com.zero.magicshow.core.camera;

import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceView;

import com.zero.magicshow.common.config.CameraConfig;
import com.zero.magicshow.common.utils.CameraParamUtil;
import com.zero.magicshow.core.camera.utils.CameraUtils;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO;

public class CameraEngine {
    private static Camera camera = null;
    private static int cameraID = 0;
    private static SurfaceTexture surfaceTexture;
    private static SurfaceView surfaceView;

    public static Camera getCamera(){
        return camera;
    }

    public static boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraID);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraID = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static void releaseCamera(){
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            cameraID = 0;
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            camera.getParameters();
        return null;
    }

    public static void switchCamera(){
        releaseCamera();
        cameraID = cameraID == 0 ? 1 : 0;
        openCamera(cameraID);
        startPreview(surfaceTexture);
    }

    private static void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
//        if (parameters.getSupportedFocusModes().contains(
//                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        }
        //增加对聚焦模式的判断
        List<String> focusModesList = parameters.getSupportedFocusModes();
        if (CameraParamUtil.getInstance().isSupportedFocusMode(focusModesList, Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }else if(CameraParamUtil.getInstance().isSupportedFocusMode(focusModesList, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        Size previewSize = CameraUtils.getLargePreviewSize(camera);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
//        Size pictureSize = CameraUtils.getLargePictureSize(camera);
        parameters.setPictureSize(CameraConfig.pictureWidth, CameraConfig.pictureHeight);
//        parameters.setRotation(90);
        parameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
        parameters.setWhiteBalance(WHITE_BALANCE_AUTO);
        //设置曝光值为1，酒吧比较暗,增加曝光
        parameters.setExposureCompensation(1);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
        camera.setParameters(parameters);
        camera.autoFocus(autoFocusCallback);
    }
    private static Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.cancelAutoFocus();
                onFocusEnd();
            }
        }
    };

    public static void onFocusEnd() {
//        mFoucsView.setVisibility(INVISIBLE);
    }

    private static Size getPreviewSize(){
        return camera.getParameters().getPreviewSize();
    }

    private static Size getPictureSize(){
        return camera.getParameters().getPictureSize();
    }

    public static void startPreview(SurfaceTexture surfaceTexture){
        if(camera != null)
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void startPreview(){
        if(camera != null)
            camera.startPreview();
    }

    public static void stopPreview(){
        camera.stopPreview();
    }

    public static void setRotation(int rotation){
        Camera.Parameters params = camera.getParameters();
        params.setRotation(rotation);
        camera.setParameters(params);
    }

    public static void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static com.zero.magicshow.core.camera.utils.CameraInfo getCameraInfo(){
        com.zero.magicshow.core.camera.utils.CameraInfo info = new com.zero.magicshow.core.camera.utils.CameraInfo();
        Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraID == 1 ? true : false;
        size = getPictureSize();
        info.pictureWidth = CameraConfig.pictureWidth;
        info.pictureHeight = CameraConfig.pictureHeight;
        Log.e("HongLi","size.width:" + size.width + ";size.height:" + size.height + ";info.previewWidth:" + info.previewWidth + ";info.previewHeight:" + info.previewHeight);
        return info;
    }
}