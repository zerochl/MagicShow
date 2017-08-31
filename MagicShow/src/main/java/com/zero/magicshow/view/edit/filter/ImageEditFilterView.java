package com.zero.magicshow.view.edit.filter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zero.magicshow.R;
import com.zero.magicshow.adapter.FilterAdapter;
import com.zero.magicshow.common.utils.Constants;
import com.zero.magicshow.core.MagicEngine;
import com.zero.magicshow.core.filter.utils.MagicFilterType;
import com.zero.magicshow.view.edit.ImageEditFragment;

public class ImageEditFilterView extends ImageEditFragment {
	private RecyclerView recyclerView;
	private View rootView;
    private ImageView btnClose,btnFavourite;

    private FilterAdapter filterAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_image_edit_filter, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init(){
		initView();
		initData();
		initListener();
	}

	private void initView(){
		recyclerView			= (RecyclerView)rootView.findViewById(R.id.image_edit_filter_recyclerview);
        btnClose                = (ImageView)rootView.findViewById(R.id.image_edit_filter_close);
        btnFavourite            = (ImageView)rootView.findViewById(R.id.image_edit_filter_favourite);
	}

	private void initData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
		filterAdapter = new FilterAdapter(getContext(), Constants.FILTER_TYPES);
        recyclerView.setAdapter(filterAdapter);
        filterAdapter.setOnFilterChangeListener(onFilterChangeListener);
	}

	private void initListener(){
        btnClose.setOnClickListener(onClickListener);
        btnFavourite.setOnClickListener(onClickListener);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            filterAdapter.refreshList();
        }
	}

	@Override
	protected boolean isChanged() {
		return MagicEngine.getInstance().getFilterType() != MagicFilterType.NONE;
	}

    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener(){

        @Override
        public void onFilterChanged(MagicFilterType filterType) {
//            magicEngine.setFilter(filterType);
			MagicEngine.getInstance().setFilter(filterType);
        }
    };

    private void doCloseAction(){

    }

    private void doFavouriteAction(){

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnClose){
                doCloseAction();
            }else if(v == btnFavourite){
                doFavouriteAction();
            }
        }
    };
}
