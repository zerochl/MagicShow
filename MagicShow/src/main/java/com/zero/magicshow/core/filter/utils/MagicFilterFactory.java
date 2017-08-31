package com.zero.magicshow.core.filter.utils;

import com.zero.magicshow.core.filter.advanced.MagicAmaroFilter;
import com.zero.magicshow.core.filter.advanced.MagicAntiqueFilter;
import com.zero.magicshow.core.filter.advanced.MagicBlackCatFilter;
import com.zero.magicshow.core.filter.advanced.MagicBrannanFilter;
import com.zero.magicshow.core.filter.advanced.MagicBrooklynFilter;
import com.zero.magicshow.core.filter.advanced.MagicCalmFilter;
import com.zero.magicshow.core.filter.advanced.MagicCoolFilter;
import com.zero.magicshow.core.filter.advanced.MagicCrayonFilter;
import com.zero.magicshow.core.filter.advanced.MagicEarlyBirdFilter;
import com.zero.magicshow.core.filter.advanced.MagicEmeraldFilter;
import com.zero.magicshow.core.filter.advanced.MagicEvergreenFilter;
import com.zero.magicshow.core.filter.advanced.MagicFairytaleFilter;
import com.zero.magicshow.core.filter.advanced.MagicFreudFilter;
import com.zero.magicshow.core.filter.advanced.MagicHealthyFilter;
import com.zero.magicshow.core.filter.advanced.MagicHefeFilter;
import com.zero.magicshow.core.filter.advanced.MagicHudsonFilter;
import com.zero.magicshow.core.filter.advanced.MagicImageAdjustFilter;
import com.zero.magicshow.core.filter.advanced.MagicInkwellFilter;
import com.zero.magicshow.core.filter.advanced.MagicKevinFilter;
import com.zero.magicshow.core.filter.advanced.MagicLatteFilter;
import com.zero.magicshow.core.filter.advanced.MagicLomoFilter;
import com.zero.magicshow.core.filter.advanced.MagicN1977Filter;
import com.zero.magicshow.core.filter.advanced.MagicNashvilleFilter;
import com.zero.magicshow.core.filter.advanced.MagicNostalgiaFilter;
import com.zero.magicshow.core.filter.advanced.MagicPixarFilter;
import com.zero.magicshow.core.filter.advanced.MagicRiseFilter;
import com.zero.magicshow.core.filter.advanced.MagicRomanceFilter;
import com.zero.magicshow.core.filter.advanced.MagicSakuraFilter;
import com.zero.magicshow.core.filter.advanced.MagicSierraFilter;
import com.zero.magicshow.core.filter.advanced.MagicSketchFilter;
import com.zero.magicshow.core.filter.advanced.MagicSkinWhitenFilter;
import com.zero.magicshow.core.filter.advanced.MagicSunriseFilter;
import com.zero.magicshow.core.filter.advanced.MagicSunsetFilter;
import com.zero.magicshow.core.filter.advanced.MagicSutroFilter;
import com.zero.magicshow.core.filter.advanced.MagicSweetsFilter;
import com.zero.magicshow.core.filter.advanced.MagicTenderFilter;
import com.zero.magicshow.core.filter.advanced.MagicToasterFilter;
import com.zero.magicshow.core.filter.advanced.MagicValenciaFilter;
import com.zero.magicshow.core.filter.advanced.MagicWaldenFilter;
import com.zero.magicshow.core.filter.advanced.MagicWarmFilter;
import com.zero.magicshow.core.filter.advanced.MagicWhiteCatFilter;
import com.zero.magicshow.core.filter.advanced.MagicXproIIFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageBrightnessFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageContrastFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageExposureFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageHueFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageSaturationFilter;
import com.zero.magicshow.core.filter.base.gpuimage.GPUImageSharpenFilter;

public class MagicFilterFactory{
	
	private static MagicFilterType filterType = MagicFilterType.NONE;
	
	public static GPUImageFilter initFilters(MagicFilterType type){
		filterType = type;
		switch (type) {
		case WHITECAT:
			return new MagicWhiteCatFilter();
		case BLACKCAT:
			return new MagicBlackCatFilter();
		case SKINWHITEN:
			return new MagicSkinWhitenFilter();
		case ROMANCE:
			return new MagicRomanceFilter();
		case SAKURA:
			return new MagicSakuraFilter();
		case AMARO:
			return new MagicAmaroFilter();
		case WALDEN:
			return new MagicWaldenFilter();
		case ANTIQUE:
			return new MagicAntiqueFilter();
		case CALM:
			return new MagicCalmFilter();
		case BRANNAN:
			return new MagicBrannanFilter();
		case BROOKLYN:
			return new MagicBrooklynFilter();
		case EARLYBIRD:
			return new MagicEarlyBirdFilter();
		case FREUD:
			return new MagicFreudFilter();
		case HEFE:
			return new MagicHefeFilter();
		case HUDSON:
			return new MagicHudsonFilter();
		case INKWELL:
			return new MagicInkwellFilter();
		case KEVIN:
			return new MagicKevinFilter();
		case LOMO:
			return new MagicLomoFilter();
		case N1977:
			return new MagicN1977Filter();
		case NASHVILLE:
			return new MagicNashvilleFilter();
		case PIXAR:
			return new MagicPixarFilter();
		case RISE:
			return new MagicRiseFilter();
		case SIERRA:
			return new MagicSierraFilter();
		case SUTRO:
			return new MagicSutroFilter();
		case TOASTER2:
			return new MagicToasterFilter();
		case VALENCIA:
			return new MagicValenciaFilter();
		case XPROII:
			return new MagicXproIIFilter();
		case EVERGREEN:
			return new MagicEvergreenFilter();
		case HEALTHY:
			return new MagicHealthyFilter();
		case COOL:
			return new MagicCoolFilter();
		case EMERALD:
			return new MagicEmeraldFilter();
		case LATTE:
			return new MagicLatteFilter();
		case WARM:
			return new MagicWarmFilter();
		case TENDER:
			return new MagicTenderFilter();
		case SWEETS:
			return new MagicSweetsFilter();
		case NOSTALGIA:
			return new MagicNostalgiaFilter();
		case FAIRYTALE:
			return new MagicFairytaleFilter();
		case SUNRISE:
			return new MagicSunriseFilter();
		case SUNSET:
			return new MagicSunsetFilter();
		case CRAYON:
			return new MagicCrayonFilter();
		case SKETCH:
			return new MagicSketchFilter();
		//image adjust
		case BRIGHTNESS:
			return new GPUImageBrightnessFilter();
		case CONTRAST:
			return new GPUImageContrastFilter();
		case EXPOSURE:
			return new GPUImageExposureFilter();
		case HUE:
			return new GPUImageHueFilter();
		case SATURATION:
			return new GPUImageSaturationFilter();
		case SHARPEN:
			return new GPUImageSharpenFilter();
		case IMAGE_ADJUST:
			return new MagicImageAdjustFilter();
		default:
			return null;
		}
	}
	
	public static MagicFilterType getCurrentFilterType(){
		return filterType;
	}
}
