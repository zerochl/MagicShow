package com.zero.magicshow.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zero.magicshow.R;
import com.zero.magicshow.adapter.FilterAdapter;
import com.zero.magicshow.common.base.BaseActivity;
import com.zero.magicshow.common.entity.MagicShowResultEntity;
import com.zero.magicshow.common.utils.BaseUtil;
import com.zero.magicshow.common.utils.Constants;
import com.zero.magicshow.common.utils.GravityUtil;
import com.zero.magicshow.common.utils.MagicParams;
import com.zero.magicshow.common.utils.RxBus;
import com.zero.magicshow.common.utils.SavePictureTask;
import com.zero.magicshow.core.MagicEngine;
import com.zero.magicshow.core.camera.CameraEngine;
import com.zero.magicshow.core.filter.utils.MagicFilterType;
import com.zero.magicshow.core.widget.MagicCameraView;
import com.zero.magicshow.viewmanager.CameraManager;
import com.zero.zerolib.util.AnimationUtils;

/**
 * Created by why8222 on 2016/3/17.
 */
public class CameraActivity extends BaseActivity{
    private LinearLayout filterLayout;
    private RecyclerView filterListView;
    private MagicCameraView magicCameraView;

    private FilterAdapter filterAdapter;
    private MagicEngine magicEngine;
    private boolean isRecording = false;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;

    private ImageView btnShutter,btnMode,btnFilter,btnFilterClose,btnCameraSwitch;//,btnBeauty;

    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
    }

    private void init(){
        initView();
        initData();
        initListener();
    }

    private void initView(){
        filterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        filterListView          = (RecyclerView) findViewById(R.id.filter_listView);

        btnShutter              = (ImageView)findViewById(R.id.camera_shutter);
        btnMode                 = (ImageView)findViewById(R.id.camera_mode);
        btnFilter               = (ImageView)findViewById(R.id.camera_filter);
        btnFilterClose          = (ImageView)findViewById(R.id.camera_closefilter);
        btnCameraSwitch         = (ImageView)findViewById(R.id.camera_switch);
//        btnBeauty               = (ImageView)findViewById(R.id.camera_beauty);
        magicCameraView         = (MagicCameraView)findViewById(R.id.camera_camera_view);
    }

    private void initData(){
        magicEngine = new MagicEngine.Builder().build(magicCameraView);
        initFilterView();
        animator = CameraManager.getShutterAnim(btnShutter);
    }

    private void initListener(){
        btnFilter.setOnClickListener(btn_listener);
        btnFilterClose.setOnClickListener(btn_listener);
        btnShutter.setOnClickListener(btn_listener);
        btnCameraSwitch.setOnClickListener(btn_listener);
        btnMode.setOnClickListener(btn_listener);
//        btnBeauty.setOnClickListener(btn_listener);
    }

    private void initFilterView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filterListView.setLayoutManager(linearLayoutManager);

        filterAdapter = new FilterAdapter(this, Constants.FILTER_TYPES);
        filterListView.setAdapter(filterAdapter);
        filterAdapter.setOnFilterChangeListener(onFilterChangeListener);
    }

    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener(){
        @Override
        public void onFilterChanged(MagicFilterType filterType) {
            magicEngine.setFilter(filterType);
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
//        if (grantResults.length != 1 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if(mode == MODE_PIC){
//                takePhoto();
//            }else{
//                takeVideo();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void doClickShutterAction(View view){
        if (PermissionChecker.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },view.getId());
        } else {
            if(mode == MODE_PIC){
                takePhoto();
            }else{
                takeVideo();
            }
        }
    }

    private void doClickBeautyAction(){
        new AlertDialog.Builder(CameraActivity.this)
                .setSingleChoiceItems(new String[] { "关闭", "1", "2", "3", "4", "5"}, MagicParams.beautyLevel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                magicEngine.setBeautyLevel(which);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    private View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnMode){
                switchMode();
            }else if(v == btnShutter){
                doClickShutterAction(v);
            }else if(v == btnFilter){
                showFilters();
            }else if(v == btnCameraSwitch){
                magicEngine.switchCamera();
            }
//            else if(v == btnBeauty){
//                doClickBeautyAction();
//            }
            else if(v == btnFilterClose){
                hideFilters();
            }
        }
    };

    private void switchMode(){
        if(mode == MODE_PIC){
            mode = MODE_VIDEO;
            btnMode.setImageResource(R.drawable.icon_camera);
        }else{
            mode = MODE_PIC;
            btnMode.setImageResource(R.drawable.icon_video);
        }
    }

    private void takePhoto(){
        final long startTime = System.nanoTime() / 1000000;
        magicEngine.savePicture(BaseUtil.getRandomTempImageFile(), new SavePictureTask.OnPictureSaveListener() {
            @Override
            public void onSaved(String result) {
                Log.e("HongLi","保存成功:" + (System.nanoTime() / 1000000 - startTime));
                MagicShowResultEntity magicShowResultEntity = new MagicShowResultEntity();
                magicShowResultEntity.setFilePath(result);
                magicShowResultEntity.setAngle(BaseUtil.readPictureDegree(result));
                Log.e("HongLi","angle:" + BaseUtil.readPictureDegree(result));
                RxBus.getInstance().post(magicShowResultEntity,Constants.RX_JAVA_TYPE_CAMERA_SHOOT);
                doFinishAction();
            }
        });
    }

    private void takeVideo(){
        if(isRecording) {
            animator.end();
            magicEngine.stopRecord();
        }else {
            animator.start();
            magicEngine.startRecord();
        }
        isRecording = !isRecording;
    }

    @Override
    protected void doFinishAction() {
        if(filterLayout.getVisibility() == View.VISIBLE){
            hideFilters();
            return;
        }
        super.doFinishAction();
    }

    private void showFilters(){
        btnShutter.setClickable(false);
        filterLayout.setVisibility(View.VISIBLE);
        AnimationUtils.doSlidingInFromBottom(filterLayout, filterLayout.getHeight(),false);
    }

    private void hideFilters(){
        AnimationUtils.doSlidingOutFromBottom(filterLayout, filterLayout.getHeight(), false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                filterLayout.setVisibility(View.INVISIBLE);
                btnShutter.setClickable(true);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraEngine.releaseCamera();
        GravityUtil.getInstance().stop();
        RxBus.getInstance().unregisterMain(Constants.RX_JAVA_TYPE_CAMERA_SHOOT);
    }
}
