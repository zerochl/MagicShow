package com.zero.magicshow.view.edit.navigation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.magicshow.R;
import com.zero.magicshow.view.edit.iface.ImageEditNavListener;
import com.zero.zerolib.util.BaseUtil;

/**
 * Created by hongli on 2017/8/22.
 */

public class ImageEditNavigationView extends LinearLayout{
    private ImageView ivIcon;
    private TextView tvName;

    private ImageEditNavListener imageEditNavListener;
    private String type;//导航类型
    private ImageEditNavigationView(Context context) {
        super(context);
        init();
    }

    public ImageEditNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initView();
        initData();
        initListener();
    }

    private void initView(){
        ivIcon = new ImageView(getContext());
        this.addView(ivIcon);
        tvName = new TextView(getContext());
        this.addView(tvName);
    }

    private void initData(){
        LayoutParams iconParam = new LayoutParams(BaseUtil.dipToPix(getContext(),25), BaseUtil.dipToPix(getContext(),25));
        iconParam.gravity = Gravity.CENTER;
        ivIcon.setLayoutParams(iconParam);
        ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);

        LayoutParams nameParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, BaseUtil.dipToPix(getContext(),25));
        nameParam.gravity = Gravity.CENTER;
        tvName.setLayoutParams(nameParam);
        tvName.setText(getResources().getString(R.string.edit_edit));
        tvName.setTextSize(14);
        tvName.setTextColor(getResources().getColor(R.color.selector_image_edit));
        tvName.setGravity(Gravity.CENTER);

        LayoutParams rootParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootParam.weight = 1;
        this.setLayoutParams(rootParam);
        this.setPadding(0,BaseUtil.dipToPix(getContext(),10),0,BaseUtil.dipToPix(getContext(),10));
        this.setOrientation(LinearLayout.VERTICAL);
    }

    private void initListener(){
        this.setOnClickListener(onClickListener);
    }

    public static ImageEditNavigationView builder(Context context){
        return new ImageEditNavigationView(context);
    }

    public ImageEditNavigationView setType(String type){
        this.type = type;
        return this;
    }

    public ImageEditNavigationView setName(String name){
        tvName.setText(name);
        return this;
    }

    public ImageEditNavigationView setIconRes(int resId){
//        ivIcon.setImageResource(resId);
        ivIcon.setBackgroundResource(resId);
        return this;
    }

    public ImageEditNavigationView setListener(ImageEditNavListener imageEditNavListener){
        this.imageEditNavListener = imageEditNavListener;
        return this;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null != imageEditNavListener){
                imageEditNavListener.onClick(v,type);
            }
        }
    };

}
