package com.zero.magicshow.view.edit;

import android.support.v4.app.Fragment;

import com.zero.magicshow.common.iface.DialogYesOrNoCallBack;
import com.zero.magicshow.common.utils.BaseUtil;
import com.zero.magicshow.core.MagicEngine;

public abstract class ImageEditFragment extends Fragment {
	protected onHideListener mOnHideListener;

	public void doFinishAction(){
		if(isChanged()){
			BaseUtil.openYesOrNoDialog(getActivity(), "是否应用修改？", "是否应用修改？", "是", "否", new DialogYesOrNoCallBack() {
				@Override
				public void onYesClick() {
					onDialogButtonClick();
					MagicEngine.getInstance().commitImage();
				}

				@Override
				public void onNoClick() {
					onDialogButtonClick();
					MagicEngine.getInstance().restoreImage();
				}
			});
		}else if(null != mOnHideListener){
			mOnHideListener.onAfterHide();
		}
	}

	public void doSaveConfigeAction(){
        if(isChanged()){
            MagicEngine.getInstance().commitImage();
        }
        if(mOnHideListener != null){
            mOnHideListener.onAfterHide();
        }
    }

	public void setOnHideListener(onHideListener l){
		this.mOnHideListener = l;
	}
	
	protected abstract boolean isChanged();
	
	protected void onDialogButtonClick(){
		if(mOnHideListener != null){
            mOnHideListener.onAfterHide();
        }
	}
	
	public interface onHideListener{
		void onAfterHide();
	}
}
