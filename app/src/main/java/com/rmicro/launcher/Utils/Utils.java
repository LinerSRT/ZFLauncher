package com.rmicro.launcher.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Freedom on 2017/12/2.
 */
public class Utils {

    private static Toast mToast;
    /** shared_pref name*/
    static final String PREF="launch.xml";	// 后缀是多余的
    static final String KEY_isFirstLoad="Is_First_Load";
    static final String KEY_Locale="Locale";

    /** DB file name  */
    static final String DB_NAME = "launcher.db";
    /** DB tables's name  */
    static final String TABLE_FAVORITES = "favorites";

    /** 每个app图标的默认宽高，正确设置celllayout，这个不会启作用*/
    static final int CellDefalutWidth = 120, CellDefalutHeight = 110;

    /**  draw a icon on background, For  like MIUI's icon <br>
     *  为app图标添加背景图片，类似小米桌面
     *  background is larger than the icon    (dfdun add;)*/
    public static Bitmap mergeTwoImg(Bitmap background,Bitmap icon){
        int w=background.getWidth(), h=background.getHeight(),
                ww=icon.getWidth(),hh=icon.getHeight();
        Bitmap newb= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(newb);
        c.drawBitmap(background, 0, 0, null);
        c.drawBitmap(icon, (w-ww)/2, (h-hh)/2, null);
        c.save();
        c.restore();
        return newb;
    }

    /**
     * resize a bitmap ; (dfdun add)
     * 调整图片的大小
     * @param src  the bitmap need to resize
     * */
    public static Bitmap scaleBitmap(Bitmap src ,float sx ,float sy){
        int width = src.getWidth(), height=src.getHeight();
        Matrix m = new Matrix();
        m.postScale(sx, sy);
        return Bitmap.createBitmap(src, 0, 0, width, height, m, true);
    }

    public static void makeToast(Context mContext, String text) {
        if (mToast != null) {
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        }
//        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static String getTime() {
        SimpleDateFormat simple = new SimpleDateFormat("HH:mm");
        return simple.format(new Date(System.currentTimeMillis()));
    }

    public static String getWeek() {
        SimpleDateFormat simple = new SimpleDateFormat("EEEE");
        return simple.format(new Date(System.currentTimeMillis()));
    }

    public static String getData() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 判断飞行模式是否打开
     *
     * @param mContext
     * @return
     */
//    public static boolean isAirPlaneOpen(Context mContext) {
//        return Settings.System.getInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false;
//    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean haveSimCard(TelephonyManager telMgr) {
        int simState = telMgr.getSimState();
        if (simState != TelephonyManager.SIM_STATE_READY) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断应用是否显示在前台
     *
     * @param mContext
     * @param pack
     * @return
     */
    public static boolean isAppRunning(Context mContext, String pack) {
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(pack)
                    || info.baseActivity.getPackageName().equals(pack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移动流量是否打开
     *
     * @param mTele
     * @return
     */
    public static boolean isFlowOpen(TelephonyManager mTele) {
        try {
            Method getDataEnabled = mTele.getClass()
                    .getDeclaredMethod("getDataEnabled");
            if (null != getDataEnabled) {
                return (Boolean) getDataEnabled.invoke(mTele);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setMobileFlowState(TelephonyManager mTel, boolean open) {
        try {
            Method setFlowEnble = mTel.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (setFlowEnble != null) {
                setFlowEnble.invoke(mTel, open);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取蓝牙连接状态 =========》不可用，待更改
     *
     * @return
     */
    public static boolean isBTConnect() {
        boolean isBTConnect = false;
//        Set<BluetoothDevice> mDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
//        for (BluetoothDevice mDevice : mDevices) {
//            String address = mDevice.getAddress();
//            int type = mDevice.getType();
//            LogUtils.e("freedom", "address:" + address + "    type:" + type);
//
//        }
        return isBTConnect;
    }


}
