package com.zero.magicshowsim;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;

import com.zero.magicshow.MagicShowManager;
import com.zero.magicshow.activity.AlbumActivity;
import com.zero.magicshow.activity.CameraActivity;
import com.zero.magicshow.common.config.PathConfig;
import com.zero.magicshow.common.entity.MagicShowResultEntity;
import com.zero.magicshow.common.iface.ImageEditCallBack;
import com.zero.zerolib.util.BaseUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.zero.magicshow.R.layout.activity_main);
        PathConfig.setTempCache("/sdcard/DCIM");
        findViewById(com.zero.magicshow.R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA },
                            v.getId());
                } else {
//                    startActivity(v.getId());
                    MagicShowManager.getInstance().openCameraAndEdit(MainActivity.this, new ImageEditCallBack() {
                        @Override
                        public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
                            Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getAngle() + ";" + magicShowResultEntity.getFilePath());
                            BaseUtil.showToast(MainActivity.this,magicShowResultEntity.getFilePath());
                        }
                    });
                }
            }
        });
        findViewById(com.zero.magicshow.R.id.button_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AlbumActivity.class));
                Log.e("HongLi","图片角度:" + com.zero.magicshow.common.utils.BaseUtil.readPictureDegree("/sdcard/DCIM/dark.jpg"));
                MagicShowManager.getInstance().openEdit(MainActivity.this,"/sdcard/DCIM/dark.jpg", new ImageEditCallBack() {
                    @Override
                    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
                        Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getFilePath());
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        if (grantResults.length != 1 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(requestCode);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startActivity(int id) {
        switch (id) {
            case com.zero.magicshow.R.id.button_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            default:
                startActivity(new Intent(this, AlbumActivity.class));
                break;
        }
    }
}
