package com.zero.magicshow.view.edit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.zero.magicshow.R;
import com.zero.magicshow.activity.AlbumActivity;
import com.zero.magicshow.view.edit.adjust.ImageEditAdjustView;
import com.zero.magicshow.view.edit.beauty.ImageEditBeautyView;
import com.zero.magicshow.view.edit.filter.ImageEditFilterView;
import com.zero.magicshow.view.edit.iface.ImageEditNavListener;
import com.zero.magicshow.view.edit.navigation.ImageEditNavigationView;

import java.util.HashMap;

/**
 * Created by hongli on 2017/8/22.
 */

public class ImageEditManager {
    public static void initAdjustView(Context context, HashMap<String,Fragment> fragmentHashMap, ViewGroup blockNavigation,
                                      ImageEditFragment.onHideListener mOnHideListener, ImageEditNavListener imageEditNavListener){
        ImageEditAdjustView adjustView = new ImageEditAdjustView();
        adjustView.setOnHideListener(mOnHideListener);
        ImageEditNavigationView adjustNavView = ImageEditNavigationView.builder(context)
                .setIconRes(R.drawable.selector_image_edit)
                .setName(context.getResources().getString(R.string.edit_edit))
                .setType(AlbumActivity.IMAGE_EDIT_TYPE_ADJUST)
                .setListener(imageEditNavListener);
        fragmentHashMap.put(AlbumActivity.IMAGE_EDIT_TYPE_ADJUST,adjustView);
        blockNavigation.addView(adjustNavView);
    }


    public static void initBeautyView(Context context, HashMap<String,Fragment> fragmentHashMap, ViewGroup blockNavigation,
                                      ImageEditFragment.onHideListener mOnHideListener, ImageEditNavListener imageEditNavListener){

        ImageEditBeautyView beautyView = new ImageEditBeautyView();
        beautyView.setOnHideListener(mOnHideListener);
        ImageEditNavigationView beautyNavView = ImageEditNavigationView.builder(context)
                .setIconRes(R.drawable.selector_image_beauty)
                .setName(context.getResources().getString(R.string.edit_beauty))
                .setType(AlbumActivity.IMAGE_EDIT_TYPE_BEAUTY)
                .setListener(imageEditNavListener);
        fragmentHashMap.put(AlbumActivity.IMAGE_EDIT_TYPE_BEAUTY,beautyView);
        blockNavigation.addView(beautyNavView);
    }

    public static void initFilterView(Context context, HashMap<String,Fragment> fragmentHashMap, ViewGroup blockNavigation,
                                      ImageEditFragment.onHideListener mOnHideListener, ImageEditNavListener imageEditNavListener){

        ImageEditFilterView filterView = new ImageEditFilterView();
        filterView.setOnHideListener(mOnHideListener);
        ImageEditNavigationView filterNavView = ImageEditNavigationView.builder(context)
                .setIconRes(R.drawable.selector_image_filter)
                .setName(context.getResources().getString(R.string.edit_filter))
                .setType(AlbumActivity.IMAGE_EDIT_TYPE_FILTER)
                .setListener(imageEditNavListener);
        fragmentHashMap.put(AlbumActivity.IMAGE_EDIT_TYPE_FILTER,filterView);
        blockNavigation.addView(filterNavView);
    }

}
