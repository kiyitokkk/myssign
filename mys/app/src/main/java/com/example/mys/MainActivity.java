package com.example.mys;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity {
    private Button mys;
    private Button xqtd;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Switch mysSwitch;
    private Switch xqtdSwitch;
    private WorkManager workManager;
    private SharedPreferences sharedPreferences;
    private NotificationHelper notificationHelper ;
    private  SharedPreferences sharedPreferences1;
    private Button addcookie;
    private Button addgenshin;
    private Button addxqtd;
    private static Context context;
    SharedPreferences mysharedPreferences ;
    SharedPreferences.Editor myeditor ;

    // 读取标志位的值
    boolean isGenshinExecuted ;
    boolean isXqtdExecuted ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mys = findViewById(R.id.genshin);
        xqtd = findViewById(R.id.xqtd);
        mysSwitch = findViewById(R.id.mysSwitch);
        xqtdSwitch = findViewById(R.id.xqtdSwitch);
        addcookie = findViewById(R.id.addcookie);
        addgenshin = findViewById(R.id.addgenshin);
        addxqtd = findViewById(R.id.addxqtd);
        context = this;
        mysharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        isGenshinExecuted = mysharedPreferences.getBoolean("isGenshinExecuted", false);
        isXqtdExecuted = mysharedPreferences.getBoolean("isXqtdExecuted", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Channel Name";
            String channelDescription = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id2", channelName, importance);
            channel.setDescription(channelDescription);
            // 将通道添加到通知管理器
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            String channelId1 = "channel_id";
            String channelName1 = "Channel Name";
            int importance1 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel1 = new NotificationChannel(channelId1, channelName1, importance1);
            notificationChannel1.setDescription("Channel Description");
// 注册通知通道
            NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager1.createNotificationChannel(notificationChannel1);
        }
        sharedPreferences = getSharedPreferences("button_states", MODE_PRIVATE);
        mysSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 保存开关状态到SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("mysSwitchState", isChecked);
            editor.apply();
        });
        xqtdSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 保存开关状态到SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("xqtdSwitchState", isChecked);
            editor.apply();
        });
        mys.setOnClickListener(v -> new Thread(() -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("genshin", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            String genshinuid = sharedPreferences.getString("genshin", "uid is null");
            String cookie = sharedPreferencesc.getString("cookie", "null");
            String result = MyWorker.Genshin(genshinuid, cookie, context);
            mHandler.post(() -> {
                notificationHelper = new NotificationHelper(MainActivity.this);
                notificationHelper.showNotification("原神", result );

            });
        }).start());
        xqtd.setOnClickListener(v -> new Thread(() -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("xqtd", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            String xqtduid = sharedPreferences.getString("xqtd", "uid is null");
            String cookie = sharedPreferencesc.getString("cookie", "null");
            String result = MyWorker.Xqtd(xqtduid, cookie, context);
            notificationHelper = new NotificationHelper(MainActivity.this);
            mHandler.post(() -> notificationHelper.showNotification1("星穹铁道", result  ));
        }).start());
        addcookie.setOnClickListener(v -> MyDialog.showInputDialog(MainActivity.this));
        addgenshin.setOnClickListener(v -> MyDialog.showInputDialoggenshin(MainActivity.this));
        addxqtd.setOnClickListener(v -> MyDialog.showInputDialogxqtd(MainActivity.this));
    }
    public void genshin() {
        boolean mysSwitchState = sharedPreferences.getBoolean("mysSwitchState", false);
        if (mysSwitchState) {
                Constraints constraints = new Constraints.Builder()
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .build();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long triggerTime = calendar.getTimeInMillis();
                long delay = triggerTime - System.currentTimeMillis();
                // 构建输入数据
                Data inputData = new Data.Builder()
                        .putString("operation", "operation1")
                        .build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance(this).enqueueUniqueWork("genshin_work", ExistingWorkPolicy.REPLACE, workRequest);

        } else {
            // 取消工作请求
            WorkManager.getInstance(this).cancelUniqueWork("genshin_work");
        }
    }
    public void xqtd() {
        boolean xqtdSwitchState = sharedPreferences.getBoolean("xqtdSwitchState", false);
        if (xqtdSwitchState) {
                Constraints constraints = new Constraints.Builder()
                        .setRequiresDeviceIdle(false)
                        .setRequiresCharging(false)
                        .build();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long triggerTime = calendar.getTimeInMillis();
                long delay = triggerTime - System.currentTimeMillis();

                // 构建输入数据
                Data inputData = new Data.Builder()
                        .putString("operation", "operation2")
                        .build();
                // 创建工作请求
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance(this).enqueueUniqueWork("xqtd_work", ExistingWorkPolicy.REPLACE, workRequest);
        } else {
            // 取消工作请求
            WorkManager.getInstance(this).cancelUniqueWork("xqtd_work");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean mysSwitchState = sharedPreferences.getBoolean("mysSwitchState", false);
        boolean xqtdSwitchState = sharedPreferences.getBoolean("xqtdSwitchState", false);
        mysSwitch.setChecked(mysSwitchState);
        xqtdSwitch.setChecked(xqtdSwitchState);
        WorkManager.getInstance(this).cancelUniqueWork("genshin_work");
        WorkManager.getInstance(this).cancelUniqueWork("xqtd_work");
        myeditor =  mysharedPreferences.edit();
        SharedPreferences genshinSharedPreferences = context.getSharedPreferences("genshin", Context.MODE_PRIVATE);
        String genshinText = genshinSharedPreferences.getString("genshin", "uid为空");
        TextView genshinTextView = ((Activity) context).findViewById(R.id.genshinTextView);
        genshinTextView.setText("原神uid: " + genshinText);
        SharedPreferences xqtdSharedPreferences = context.getSharedPreferences("xqtd", Context.MODE_PRIVATE);
        String xqtdText = xqtdSharedPreferences.getString("xqtd", "uid为空");
        TextView xqtdTextView = ((Activity) context).findViewById(R.id.xqtdTextView);
        xqtdTextView.setText("铁道uid: " + xqtdText);
        SharedPreferences cookiesPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String cookiestext = cookiesPreferences.getString("cookie", "null");
        TextView cookiesText = ((Activity) context).findViewById(R.id.cookies);
        if (!cookiestext.equals("null")){
            cookiesText.setText("cookie: 已添加");
        }
        if (RandomStringGenerator.getUUID(MainActivity.this).equals("null")){
            RandomStringGenerator.saveUUID(RandomStringGenerator.generateUUID(), MainActivity.this);
        }
        if (RandomStringGenerator.readRandomString(MainActivity.this).equals("null")){
            RandomStringGenerator.saveRandomString(MainActivity.this, RandomStringGenerator.generateRandomString());
        }
        if (mysSwitchState){
            genshin();
        }
        if (xqtdSwitchState){
            xqtd();
        }
    }
}
