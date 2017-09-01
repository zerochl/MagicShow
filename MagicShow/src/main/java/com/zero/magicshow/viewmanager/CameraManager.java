package com.zero.magicshow.viewmanager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.widget.ImageView;

/**
 * Created by hongli on 2017/9/1.
 */

public class CameraManager {
    public static ObjectAnimator getShutterAnim(ImageView btnShutter){
        ObjectAnimator animator = ObjectAnimator.ofFloat(btnShutter,"rotation",0,360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        return animator;
    }
}
