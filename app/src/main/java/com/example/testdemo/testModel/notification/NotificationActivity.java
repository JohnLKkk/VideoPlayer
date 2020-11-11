package com.example.testdemo.testModel.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.testdemo.R;
import com.yoy.v_Base.ui.BaseDefaultActivity;

public class NotificationActivity extends BaseDefaultActivity {
    private NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setActionBar("通知测试",true);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_notification;
    }

    @Override
    public boolean isFullScreenWindow() {
        return true;
    }

    public void onOpenOntification(View view) {
        if (!isEnabled()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onCloseOntification(View view) {
        if (isEnabled()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已关闭", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onSendNotification(View view) {
        Notification notification;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            // 创建通知(标题、内容、图标)
            notification = new NotificationCompat.Builder(this, "default")
                    .setContentTitle("通知标题")
                    .setContentText("通知内容")
                    .setSmallIcon(R.drawable.ic_notification)
                    .build();
            // 发送通知
        } else {
            notification = new NotificationCompat.Builder(this, "default")
                    .setContentTitle("收到一条订阅消息")
                    .setContentText("" + System.currentTimeMillis())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification))
                    .setAutoCancel(true)
                    .build();
        }
        manager.notify(2, notification);
    }

    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
