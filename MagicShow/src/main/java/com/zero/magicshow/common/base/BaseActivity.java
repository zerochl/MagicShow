package com.zero.magicshow.common.base;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

/**
 * Created by hongli on 2017/8/22.
 */

public abstract class BaseActivity extends FragmentActivity{
    protected void doFinishAction(){
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            doFinishAction();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
