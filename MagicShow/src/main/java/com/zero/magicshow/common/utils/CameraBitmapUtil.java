package com.zero.magicshow.common.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.Surface;

/**
 * Created by hongli on 2017/12/14.
 */

public class CameraBitmapUtil {

    public static Bitmap handlerCameraBitmap(Activity activity,Bitmap takeBitmap,int cameraId) {
        Matrix matrix = new Matrix();
        matrix.postRotate(getCameraDisplayOrientation(activity, cameraId));
        matrix.postScale(1, cameraId == 1 ? -1 : 1);
        Bitmap cropRotateScaled = Bitmap.createBitmap(takeBitmap, 0, 0, takeBitmap.getWidth(), takeBitmap.getHeight(), matrix, true);
        takeBitmap.recycle();
        return cropRotateScaled;
    }

    public static int getCameraDisplayOrientation(Activity activity,int cameraId) {
        int result = 90;
        try {
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(cameraId, info);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
