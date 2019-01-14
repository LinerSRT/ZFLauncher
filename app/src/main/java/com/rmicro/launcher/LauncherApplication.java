package com.rmicro.launcher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.rmicro.launcher.utils.CrashHandler;
import com.rmicro.launcher.utils.LogUtil;
import com.rmicro.launcher.utils.customFontUtils.FontManager;

import java.util.Iterator;
import java.util.List;

/*
* Create by Tim  2016
* */

/**
 * *                                                   #
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

public class LauncherApplication extends Application {

	private final String TAG = "=RMApplication=";
	 
	private static LauncherApplication mApplication = null;

	public static boolean isAPPrunning = false;

	public static LauncherApplication getInstance() {
		if (null == mApplication)
			mApplication = new LauncherApplication();
		return mApplication;
	}

	/**
	 * 确保只初始化一次
	 */
	public static boolean isInitOnce(Application application) {

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(application, pid);
		// 如果APP启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
		if (processAppName == null || !processAppName.equalsIgnoreCase(application.getPackageName())) {
			// 则此application::onCreate 是被service 调用的，直接返回
			return false;
		}
		return true;
	}

	private static String getAppName(Application application, int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = application.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	@Override
	public void onCreate() {
	 	super.onCreate();

		LogUtil.d(TAG,"onCreate start. ");

		//设置logger//
		//LogUtil.logger.setClientID(LogUtil.TAG);
		if (isInitOnce(this)) {//确保初始化一次//
			//LogUtil.isBootOK = true;//
			//加载字体//
			FontManager.createInstance(this);
			FontManager.getInstance().initFontType();

			//startRMDevControls();
			//异常崩溃处理//
			CrashHandler.init(this);

			LogUtil.d(TAG,"onCreate finished ");

			//RTCinit();
		}
	}

	//启动管控服务//
	//private void startRMDevControls(){
	//	Intent intent = new Intent();
	//	intent = new Intent("android.intent.action.RMDEV_CONTROLS");
	//	sendBroadcast(intent);
	//}

	@Override
	public void onLowMemory() {
		LogUtil.e(TAG,"onLowMemory,some object or class would be recycled!");
		System.gc();//回收内存
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		LogUtil.e(TAG,"onTerminate,app exit,release instance object!");
		mApplication = null;
		super.onTerminate();
	}
}
