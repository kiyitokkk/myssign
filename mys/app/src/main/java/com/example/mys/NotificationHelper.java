package com.example.mys;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
public class NotificationHelper {
    private Context context;
    public NotificationHelper(Context context) {
        this.context = context;
    }
    @SuppressLint("MissingPermission")
    public  void showNotification(String title,String content) {
        // 创建通知栏
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.genshins))
                .setSmallIcon(R.mipmap.circle);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(content);
        builder.setStyle(bigTextStyle);
        // 显示通知栏
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
    @SuppressLint("MissingPermission")
    public void showNotification1(String title, String content) {
        // 创建通知栏
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id2")
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.xqtdic))
                .setSmallIcon(R.mipmap.xqtd);
        // 设置长文本样式
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(content);
        builder.setStyle(bigTextStyle);
        // 显示通知栏
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());
    }
}