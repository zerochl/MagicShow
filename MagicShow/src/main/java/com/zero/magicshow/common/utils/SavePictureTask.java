package com.zero.magicshow.common.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.zero.magicshow.common.entity.MagicShowResultEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePictureTask extends AsyncTask<Bitmap, Integer, MagicShowResultEntity>{
	
	private OnPictureSaveListener onPictureSaveListener;
	private File file;

	public SavePictureTask(File file, OnPictureSaveListener listener){
		this.onPictureSaveListener = listener;
		this.file = file;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final MagicShowResultEntity result) {
		if(result != null){
            if (onPictureSaveListener != null){
                onPictureSaveListener.onSaved(result);
            }
//            BaseUtil.scanFile(result);
        }
	}

	@Override
	protected MagicShowResultEntity doInBackground(Bitmap... params) {
		if(file == null)
			return null;
        MagicShowResultEntity resultEntity = new MagicShowResultEntity();
        resultEntity.setAngle(params[0].getHeight() > params[0].getWidth() ? 90 : 0);
        resultEntity.setFilePath(saveBitmap(params[0]));
		return resultEntity;
	}
	
	private String saveBitmap(Bitmap bitmap) {
		if (file.exists()) {
			file.delete();
		}
		try {
//            Log.e("HongLi","bitmap degree:" + bitmap.getConfig());
            FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
            bitmap.recycle();
			return file.toString();
		} catch (FileNotFoundException e) {
		   e.printStackTrace();
		} catch (IOException e) {
		   e.printStackTrace();
		}
		return null;
	}
	
	public interface OnPictureSaveListener{
		void onSaved(MagicShowResultEntity resultEntity);
	}
}
