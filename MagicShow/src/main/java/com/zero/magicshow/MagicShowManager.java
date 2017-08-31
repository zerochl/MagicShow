package com.zero.magicshow;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.zero.magicshow.activity.AlbumActivity;
import com.zero.magicshow.common.entity.MagicShowResultEntity;
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

    public void openEdit(Context context,String imagePath, final ImageEditCallBack imageEditCallBack){
        if(null == context || TextUtils.isEmpty(imagePath) || !FileUtil.isExist(imagePath)){
            Log.e(TAG,"in open edit data error.");
            return;
        }
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

    public void openCamera(ImageEditCallBack imageEditCallBack){

    }

}
