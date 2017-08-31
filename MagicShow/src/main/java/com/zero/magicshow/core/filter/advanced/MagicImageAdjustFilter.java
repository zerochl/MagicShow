package com.zero.magicshow.core.filter.advanced;

import com.zero.magicshow.core.filter.base.MagicBaseGroupFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageBrightnessFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageContrastFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageExposureFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageHueFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageSaturationFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageSharpenFilter;
import com.zero.magicshow.core.filter.utils.MagicFilterType;

import java.util.ArrayList;
import java.util.List;

public class MagicImageAdjustFilter extends MagicBaseGroupFilter{
	
	public MagicImageAdjustFilter() {
		super(initFilters());
	}
	
	private static List<GPUImageFilter> initFilters(){
		List<GPUImageFilter> filters = new ArrayList<GPUImageFilter>();
		filters.add(new GPUImageContrastFilter());
		filters.add(new GPUImageBrightnessFilter());
		filters.add(new GPUImageExposureFilter());
		filters.add(new GPUImageHueFilter());
		filters.add(new GPUImageSaturationFilter());
		filters.add(new GPUImageSharpenFilter());
		return filters;		
	}

	public void setImageFilter(float range,MagicFilterType type){
		switch (type){
			case CONTRAST:
				setContrast(range);
				break;
			case BRIGHTNESS:
				setBrightness(range);
				break;
			case EXPOSURE:
				setExposure(range);
				break;
			case HUE:
				setHue(range);
				break;
			case SATURATION:
				setSaturation(range);
				break;
			case SHARPEN:
				setSharpness(range);
				break;
		}
	}
	public void setSharpness(final float range){
		((GPUImageSharpenFilter) filters.get(5)).setSharpness(range);
	}
	
	public void setHue(final float range){
		((GPUImageHueFilter) filters.get(3)).setHue(range);
	}
	
	public void setBrightness(final float range){
		((GPUImageBrightnessFilter) filters.get(1)).setBrightness(range);
	}
	
	public void setContrast(final float range){
		((GPUImageContrastFilter) filters.get(0)).setContrast(range);
	}
	
	public void setSaturation(final float range){
		((GPUImageSaturationFilter) filters.get(4)).setSaturation(range);
	}
	
	public void setExposure(final float range){
		((GPUImageExposureFilter) filters.get(2)).setExposure(range);
	}
}
