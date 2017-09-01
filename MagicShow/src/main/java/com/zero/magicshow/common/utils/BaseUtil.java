package com.zero.magicshow.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.magicshow.R;
import com.zero.magicshow.common.config.PathConfig;
import com.zero.magicshow.common.iface.DialogYesOrNoCallBack;
import com.zero.zerolib.util.AnimationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hongli on 2017/8/30.
 */

public class BaseUtil extends com.zero.zerolib.util.BaseUtil {
    //    public static void scanFile(String filePath){
//        MediaScannerConnection.scanFile(MagicParams.context,
//                new String[] {filePath}, null,
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    @Override
//                    public void onScanCompleted(final String path, final Uri uri) {
//
//                    }
//                });
//    }
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static boolean isPortrait(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels < dm.heightPixels;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static void fadeOutView(final View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        AnimationUtils.doFadeOut(view, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static void fadeInView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        AnimationUtils.doFadeIn(view);
    }

    public static void openYesOrNoDialog(final Activity activity, String title, String content, String yesText, String noText, final DialogYesOrNoCallBack yesOrNoCallBack) {
        final Dialog shopTipDialog = new Dialog(activity, R.style.loading_dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_yes_or_no, null);
        final ImageView btnClose = (ImageView) view.findViewById(R.id.gift_balance_lack_close);
        final TextView btnYes = (TextView) view.findViewById(R.id.dialog_yes_or_no_btnyes);
        final TextView tvTitle = (TextView) view.findViewById(R.id.dialog_yes_or_no_title);
        final TextView tvCon = (TextView) view.findViewById(R.id.dialog_yes_or_no_con);
        final TextView btnNo = (TextView) view.findViewById(R.id.dialog_yes_or_no_btnno);
        tvTitle.setText(title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        }
        tvCon.setText(content);
        if (TextUtils.isEmpty(content)) {
            tvCon.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(yesText)) {
            btnYes.setText(yesText);
        }
        if (!TextUtils.isEmpty(noText)) {
            btnNo.setText(noText);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnClose) {
                    shopTipDialog.dismiss();
                } else if (v == btnYes) {
                    yesOrNoCallBack.onYesClick();
                    shopTipDialog.dismiss();
                } else if (v == btnNo) {
                    yesOrNoCallBack.onNoClick();
                    shopTipDialog.dismiss();
                }
            }
        };
        shopTipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                yesOrNoCallBack.onNoClick();
            }
        });
        btnClose.setOnClickListener(onClickListener);
        btnYes.setOnClickListener(onClickListener);
        btnNo.setOnClickListener(onClickListener);
//		shopTipDialog.setCanceledOnTouchOutside(true);
        shopTipDialog.setContentView(view, new ViewGroup.LayoutParams(dipToPix(activity, 267), dipToPix(activity, 100)));
        //try catch防止activity已经销毁
        try {
            shopTipDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getRandomTempImageFile() {
        return new File(getRandomTempImagePath());
    }

    public static String getRandomTempImagePath() {
        return PathConfig.getTempPath() + "/" + getRandomStr() + ".jpg";
    }

    public static String getRandomStr() {
        int randInt = (int) (Math.random() * 100000);
        if (randInt < 10000) {
            randInt = randInt + 10000;
        }
        return DateUtils.getTimeStamp() + randInt;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param distancePath
     */
    public static void saveBitmap(Bitmap bitmap, String distancePath) {
        if (TextUtils.isEmpty(distancePath) || null == bitmap || bitmap.isRecycled()) {
            Log.e("BaseUtil", "in saveBitmap bitmap is null or recycles or distancePath is empty.");
            return;
        }
        File file = new File(distancePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            //压缩20%，否则保存的文件会变大
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * according to the width to get bitmap,if image's width more than width,
     * than compress the image's width and height
     *
     * @param path
     * @param width  the width which need to be
     * @param height
     */
    public static Bitmap getBitmapBySimpleSize(
            String path,
            float width,
            float height) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        setBitmapNormalOptions(options, width, height);
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
//		MyLogUtil.e("HongLi", "path:" + path);
        bitmap = BitmapFactory.decodeFile(path, options);
//		MyLogUtil.e("HongLi", "image width:" + bitmap.getWidth() + ";height:" + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 设置bitmap的缩放比
     *
     * @param options
     * @param width
     * @param height
     */
    private static void setBitmapNormalOptions(
            BitmapFactory.Options options, float width, float height) {
        if (null == options) {
            return;
        }
        if (options.outWidth > width) {
            float scale = (options.outWidth / width);
            //如果按照宽度缩放比例导致高度小于原定高度，则按照高度的缩放比例来缩放
            if (options.outHeight / scale < height) {
                scale = (options.outHeight / height);
            }
            int realScale = scale - (int) scale >= 0.2 ? (int) scale + 1 : (int) scale;
            realScale = realScale <= 0 ? 1 : realScale;
            options.inSampleSize = realScale;
            options.outWidth = options.outWidth / options.inSampleSize;
            options.outHeight = options.outHeight / options.inSampleSize;
        } else {
            options.inSampleSize = 1;
        }
        if (options.outHeight > 3500) {
            int realScale = options.outHeight / 3500;
            options.inSampleSize = options.inSampleSize < realScale ? realScale : options.inSampleSize;
        }
        if (options.outWidth > 3500) {
            int realScale = options.outWidth / 3500;
            options.inSampleSize = options.inSampleSize < realScale ? realScale : options.inSampleSize;
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        //RGB会造成透明PNG显示黑色背景
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }
}
