package com.zero.magicshow.view.edit.adjust;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zero.magicshow.R;
import com.zero.magicshow.core.MagicEngine;
import com.zero.magicshow.core.filter.utils.MagicFilterType;
import com.zero.magicshow.view.edit.ImageEditFragment;
import com.zero.magicshow.core.widget.TwoLineSeekBar;
import com.zero.magicshow.core.widget.TwoLineSeekBar.OnSeekChangeListener;

import static com.zero.magicshow.core.filter.utils.MagicFilterType.IMAGE_ADJUST;

public class ImageEditAdjustView extends ImageEditFragment {
	private TwoLineSeekBar mSeekBar;
	private float contrast = -50.0f;
	private float exposure = 0.0f; 
	private float saturation = 0.0f;
	private float sharpness = 0.0f;
	private float brightness = 0.0f;
	private float hue = 0.0f;
	private RadioGroup mRadioGroup;
	private MagicFilterType type = MagicFilterType.NONE;
	private ImageView mLabel;
	private TextView mVal;
	private LinearLayout mLinearLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_image_edit_adjust, container, false);  
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mRadioGroup = (RadioGroup)getView().findViewById(R.id.fragment_adjust_radiogroup);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId != -1){
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
                if(checkedId == R.id.fragment_radio_contrast){
                    type = MagicFilterType.CONTRAST;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(-100, 100, -50, 1);
                    mSeekBar.setValue(contrast);
                    mVal.setText(""+contrast);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_contrast);
                }else if(checkedId == R.id.fragment_radio_exposure){
                    type = MagicFilterType.EXPOSURE;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(-100, 100, 0, 1);
                    mSeekBar.setValue(exposure);
                    mVal.setText(""+exposure);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_exposure);
                }else if(checkedId == R.id.fragment_radio_saturation){
                    type = MagicFilterType.SATURATION;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(-100, 100, 0, 1);
                    mSeekBar.setValue(saturation);
                    mVal.setText(""+saturation);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_saturation);
                }else if(checkedId == R.id.fragment_radio_sharpness){
                    type = MagicFilterType.SHARPEN;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(-100, 100, 0, 1);
                    mSeekBar.setValue(sharpness);
                    mVal.setText(""+sharpness);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_sharpness);
                }else if(checkedId == R.id.fragment_radio_bright){
                    type = MagicFilterType.BRIGHTNESS;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(-100, 100, 0, 1);
                    mSeekBar.setValue(brightness);
                    mVal.setText(""+brightness);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_bright);
                }else if(checkedId == R.id.fragment_radio_hue){
                    type = MagicFilterType.HUE;
                    mSeekBar.reset();
                    mSeekBar.setSeekLength(0, 360, 0, 1);
                    mSeekBar.setValue(hue);
                    mVal.setText(""+hue);
                    mLabel.setBackgroundResource(R.drawable.selector_image_edit_adjust_hue);
                }
			}
		});
		mSeekBar = (TwoLineSeekBar)view.findViewById(R.id.item_seek_bar);
		mSeekBar.setOnSeekChangeListener(mOnSeekChangeListener);
		mVal = (TextView)view.findViewById(R.id.item_val);
		mLabel = (ImageView)view.findViewById(R.id.item_label);
		mLinearLayout = (LinearLayout)view.findViewById(R.id.seek_bar_item_menu);
		MagicEngine.getInstance().setFilter(IMAGE_ADJUST);
        mRadioGroup.getChildAt(0).performClick();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			contrast = -50.0f;
			exposure = 0.0f; 
			saturation = 0.0f;
			sharpness = 0.0f;
			brightness = 0.0f;
			hue = 0.0f;
			mRadioGroup.clearCheck();
			MagicEngine.getInstance().setFilter(MagicFilterType.NONE);
			mLinearLayout.setVisibility(View.INVISIBLE);
			type = MagicFilterType.NONE;
            mRadioGroup.getChildAt(0).performClick();
		}else{
			MagicEngine.getInstance().setFilter(IMAGE_ADJUST);
		}
	}
	
	protected boolean isChanged(){
		return contrast != -50.0f || exposure != 0.0f || saturation != 0.0f
				|| sharpness != 0.0f || brightness != 0.0f || hue != 0.0f;
	}
    protected float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    protected int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }
	private float convertToProgress(float value){
        if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_contrast){
            contrast = value;
            return range(Math.round((value + 100) / 2), 0.0f, 4.0f);
        }else if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_exposure){
            exposure = value;
            return range(Math.round((value + 100) / 2), -2.0f, 2.0f);
        }else if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_saturation){
            saturation = value;
            return range(Math.round((value + 100) / 2), 0.0f, 2.0f);
        }else if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_sharpness){
            sharpness = value;
            return range(Math.round((value + 100) / 2), -4.0f, 4.0f);
        }else if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_bright){
            brightness = value;
            return range(Math.round((value + 100) / 2), -0.5f, 0.5f);
        }else if(mRadioGroup.getCheckedRadioButtonId() == R.id.fragment_radio_hue){
            hue = value;
            return range(Math.round(100 * value / 360.0f), 0.0f, 360.0f);
        }
        return 0;
	}
	
	private OnSeekChangeListener mOnSeekChangeListener = new OnSeekChangeListener() {
		
		@Override
		public void onSeekStopped(float value, float step) {
            mVal.setText(""+value);
            mLabel.setPressed(value != 0.0f);
            Log.e("HongLi","percent:" + convertToProgress(value));
            MagicEngine.getInstance().adjustFilter(convertToProgress(value), type);
		}
		
		@Override
		public void onSeekChanged(float value, float step) {
		}
	};
}
