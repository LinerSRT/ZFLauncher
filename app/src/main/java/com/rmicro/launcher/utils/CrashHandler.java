package com.rmicro.launcher.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.rmicro.launcher.Launcher;

/**
 * Created by tim on 16-12-16.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";
    private static CrashHandler mCrashHandler;
    private final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private Context context;

    private CrashHandler(Context context) {
        this.context = context.getApplicationContext();
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashHandler init(Context context) {
        return new CrashHandler(context);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.d(TAG, " !!!异常!!!");
        String VersionInfo = getVersionInfo();
        String MobileInfo = getMobileInfo();
        String ErrorInfo = getErrorInfo(ex);

        BufferedWriter out = null;
        try {
            String title = "########" + dateFormat.format(new Date())
                    + "########\r\n";
            String content = "VersionInfo" + VersionInfo
                    + "\r\nmobileInfo：\r\n" + MobileInfo + "errorInfo：\r\n"
                    + ErrorInfo + "\r\n";
            FileUtil fileUtils = new FileUtil();//创建工作目录//
            fileUtils.createSdcardDir("YANWEI-LOG");
            File file = fileUtils.createFileInSDCard("launcher_debug.log",
                    "/YANWEI-LOG", 0);

            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(title + content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out)
                    out.close();
            } catch (IOException e2) {
            }
        }
        Intent intent = new Intent(context,Launcher.class);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent); // 1秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");


    private String getErrorInfo(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        String error = writer.toString();
        return error;
    }

    private String getMobileInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                stringBuffer.append("name == " + name + " ## value == "
                        + value);
                stringBuffer.append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private String getVersionInfo() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "未知版本号";
        }
    }
}
