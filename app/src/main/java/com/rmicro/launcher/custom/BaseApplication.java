package com.rmicro.launcher.custom;

import android.app.Application;
import android.text.TextUtils;

import com.rmicro.launcher.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Freedom on 2018/1/6.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //更换默认字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/typeface.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
