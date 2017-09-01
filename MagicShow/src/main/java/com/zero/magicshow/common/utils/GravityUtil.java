package com.zero.magicshow.common.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zero.magicshow.common.iface.GravityCallBack;

/**
 * Created by hongli on 2017/9/1.
 */

public class GravityUtil {
    private static final String TAG = GravityUtil.class.getSimpleName();
    public static final int DIRECTION_LAND_LEFT = 1;
    public static final int DIRECTION_PORTRAIT_POSITIVE = 2;
    public static final int DIRECTION_LAND_RIGHT = 3;
    public static final int DIRECTION_PORTRAIT_NEGATIVE = 4;
    public static final int DIRECTION_LAND = 5;//手动选择横屏
    public static final int DIRECTION_PORTRAIT = 6;//手动选择竖屏

    private volatile static GravityUtil gravityUtil = new GravityUtil();
    private GravityCallBack gravityCallBack;

    private Activity mActivity;

    // 是否是竖屏
    private boolean isPortrait = true;
    //是否是左面倾斜横屏还是右面倾斜
    private boolean isLeftCrossScreen = true;

    private SensorManager sm;
    private GravityUtil.OrientationSensorListener listener;
    private Sensor sensor;

    private SensorManager sm1;
    private Sensor sensor1;
    private GravityUtil.OrientationSensorListener1 listener1;

    private final int MAX_RETRY_COUNT = 10;
    private int retryLandLeftCount = 0;//并非已判断是横或者竖就立即发送消息，持续MAX_RETRY_COUNT次之后才会发送
    private int retryLandRightCount = 0;
    private int retryPorPositiveCount = 0;
    private int retryPorNegtiveCount = 0;

    public static GravityUtil getInstance(){
        return gravityUtil;
    }

    private GravityUtil(){

    }
    /**
     * 返回ScreenSwitchUtils单例
     **/
    public void init(Context context, GravityCallBack gravityCallBack) {
        Log.d(TAG, "init orientation listener.");
        if(null == sm){
            // 注册重力感应器,监听屏幕旋转
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            listener = new GravityUtil.OrientationSensorListener(mHandler);

            // 根据 旋转之后/点击全屏之后 两者方向一致,激活sm.
            sm1 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensor1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            listener1 = new GravityUtil.OrientationSensorListener1();
        }
        this.gravityCallBack = gravityCallBack;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {
                        if (isPortrait && retryLandRightCount >= MAX_RETRY_COUNT) {
                            Log.e("test", "切换成右横屏");
                            isLeftCrossScreen = false;
                            gravityCallBack.onGravityChange(DIRECTION_LAND_RIGHT);
//                            mActivity.setRequestedOrientation(0);
                            isPortrait = false;
                            retryLandRightCount = 0;
                        }else{
                            retryLandRightCount++;
                            retryLandLeftCount = 0;
                            retryPorNegtiveCount = 0;
                            retryPorPositiveCount = 0;
                        }
                    } else if (orientation > 135 && orientation < 225) {
                        if (!isPortrait && retryPorNegtiveCount >= MAX_RETRY_COUNT) {
                            Log.e("test", "切换成反竖屏");
//                            mActivity.setRequestedOrientation(1);
                            gravityCallBack.onGravityChange(DIRECTION_PORTRAIT_NEGATIVE);
                            isPortrait = true;
                            retryPorNegtiveCount = 0;
                        }else{
                            retryPorNegtiveCount++;
                            retryLandRightCount = 0;
                            retryLandLeftCount = 0;
                            retryPorPositiveCount = 0;
                        }
                    } else if (orientation > 225 && orientation < 315) {
                        if (isPortrait && retryLandLeftCount >= MAX_RETRY_COUNT) {
                            Log.e("test", "切换成左横屏");
                            isLeftCrossScreen = true;
                            gravityCallBack.onGravityChange(DIRECTION_LAND_LEFT);
//                            mActivity.setRequestedOrientation(0);
                            isPortrait = false;
                            retryLandLeftCount = 0;
                        }else{
                            retryLandLeftCount++;
                            retryPorNegtiveCount = 0;
                            retryLandRightCount = 0;
                            retryPorPositiveCount = 0;
                        }
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        if (!isPortrait && retryPorPositiveCount >+MAX_RETRY_COUNT) {
                            Log.e("test", "切换成竖屏");
//                            mActivity.setRequestedOrientation(1);
                            gravityCallBack.onGravityChange(DIRECTION_PORTRAIT_POSITIVE);
                            isPortrait = true;
                            retryPorPositiveCount = 0;
                        }else{
                            retryPorPositiveCount++;
                            retryPorNegtiveCount = 0;
                            retryLandRightCount = 0;
                            retryLandLeftCount = 0;
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };


    /**
     * 开始监听
     */
    public void start(Activity activity) {
        Log.d(TAG, "start orientation listener.");
        mActivity = activity;
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 停止监听
     */
    public void stop() {
        Log.d(TAG, "stop orientation listener.");
        sm.unregisterListener(listener);
        sm1.unregisterListener(listener1);
    }

    /**
     * 手动横竖屏切换方向
     */
    public void toggleScreen() {
//        sm.unregisterListener(listener);
//        sm1.registerListener(listener1, sensor1,SensorManager.SENSOR_DELAY_UI);
//        if (isPortrait) {
//            isPortrait = false;
//            // 切换成横屏
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            isPortrait = true;
//            // 切换成竖屏
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    public boolean isPortrait() {
        return this.isPortrait;
    }

    public boolean isLeftCrossScreen() {
        return this.isLeftCrossScreen;
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }

    public class OrientationSensorListener1 implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        public OrientationSensorListener1() {
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (orientation > 225 && orientation < 315) {// 检测到当前实际是横屏
                if (!isPortrait) {
                    sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                    sm1.unregisterListener(listener1);
                }
            } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {// 检测到当前实际是竖屏
                if (isPortrait) {
                    sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                    sm1.unregisterListener(listener1);
                }
            }
        }
    }
}
