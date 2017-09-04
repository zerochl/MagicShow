package com.zero.magicshow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;

import com.zero.magicshow.activity.AlbumActivity;
import com.zero.magicshow.activity.CameraActivity;
import com.zero.magicshow.common.entity.MagicShowResultEntity;
import com.zero.magicshow.common.iface.CameraShootCallBack;
import com.zero.magicshow.common.iface.ImageEditCallBack;
import com.zero.magicshow.common.utils.Constants;
import com.zero.magicshow.common.utils.RxBus;
import com.zero.magicshow.common.config.PathConfig;
import com.zero.zerolib.util.FileUtil;

import rx.functions.Action1;

/**
 * Created by hongli on 2017/8/31.
 */

public class MagicShowManager {
    private static final String TAG = "MagicShowManager";
    private static final MagicShowManager magicShowManager = new MagicShowManager();
    private MagicShowManager(){}
    public static MagicShowManager getInstance(){
        return magicShowManager;
    }

    public void setCachePath(String cachePath){
        PathConfig.setTempCache(cachePath);
    }

    /**
     * 执行照片编辑
     * @param context
     * @param imagePath
     * @param imageEditCallBack
     */
    public void openEdit(Context context,String imagePath, final ImageEditCallBack imageEditCallBack){
        if(null == context || TextUtils.isEmpty(imagePath) || !FileUtil.isExist(imagePath)){
            Log.e(TAG,"in open edit data error.");
            return;
        }
        RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_IMAGE_EDIT);
        RxBus.getInstance().registerMain(Constants.RX_JAVA_TYPE_IMAGE_EDIT, new Action1<MagicShowResultEntity>() {
            @Override
            public void call(MagicShowResultEntity magicShowResultEntity) {
                imageEditCallBack.onCompentFinished(magicShowResultEntity);
                RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_IMAGE_EDIT);
            }
        });
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(Constants.TRANSMIT_IMAGE_URL,imagePath);
        context.startActivity(intent);
    }

    /**
     * 执行拍照
     * @param context
     * @param cameraShootCallBack
     */
    public void openCamera(Activity context,final CameraShootCallBack cameraShootCallBack){
        if(null == context){
            Log.e(TAG,"in open edit data error.");
            return;
        }
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.CAMERA },1);
            return;
        }
        RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_CAMERA_SHOOT);
        RxBus.getInstance().registerMain(Constants.RX_JAVA_TYPE_CAMERA_SHOOT, new Action1<MagicShowResultEntity>() {
            @Override
            public void call(MagicShowResultEntity magicShowResultEntity) {
                cameraShootCallBack.onCompentFinished(magicShowResultEntity);
                RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_CAMERA_SHOOT);
            }
        });
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开拍照，然后直接对拍照图片进行编辑
     * @param context
     * @param imageEditCallBack
     */
    public void openCameraAndEdit(final Activity context, final ImageEditCallBack imageEditCallBack){
        openCamera(context, new CameraShootCallBack() {
            @Override
            public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
                openEdit(context,magicShowResultEntity.getFilePath(),imageEditCallBack);
            }
        });
    }

}
