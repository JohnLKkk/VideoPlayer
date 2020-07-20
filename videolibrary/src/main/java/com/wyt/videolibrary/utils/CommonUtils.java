package com.wyt.videolibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.StatFs;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Set;


/**
 * Created by taihong on 2019/4/2
 */
public class CommonUtils {

    public static int getStateBarHeight(Context context){
        int stateBarHeight=getStateBar3(context);
        if (stateBarHeight==0){
            stateBarHeight=getStateBar2(context);
            if (stateBarHeight==0){
                stateBarHeight=getStateBar1(context);
            }
        }
        return stateBarHeight;
    }

    private static int getStateBar1(Context context){
        return (int) Math.ceil(20 * context.getResources().getDisplayMetrics().density);
    }


    private static  int getStateBar2(Context context) {
        Class c = null;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getStateBar3(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



}
