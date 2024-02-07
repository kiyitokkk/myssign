package com.example.mys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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

public class Setting extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        mys = findViewById(R.id.genshin);
        xqtd = findViewById(R.id.xqtd);
        mysSwitch = findViewById(R.id.mysSwitch);
        xqtdSwitch = findViewById(R.id.xqtdSwitch);
        addcookie = findViewById(R.id.addcookie);
        addgenshin = findViewById(R.id.addgenshin);
        addxqtd = findViewById(R.id.addxqtd);
        sharedPreferences = getSharedPreferences("button_states", MODE_PRIVATE);
        context = this;
        mysharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        isGenshinExecuted = mysharedPreferences.getBoolean("isGenshinExecuted", false);
        isXqtdExecuted = mysharedPreferences.getBoolean("isXqtdExecuted", false);
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
        addcookie.setOnClickListener(v -> MyDialog.showInputDialog(Setting.this));
        addgenshin.setOnClickListener(v -> MyDialog.showInputDialoggenshin(Setting.this));
        addxqtd.setOnClickListener(v -> MyDialog.showInputDialogxqtd(Setting.this));


    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean mysSwitchState = sharedPreferences.getBoolean("mysSwitchState", false);
        boolean xqtdSwitchState = sharedPreferences.getBoolean("xqtdSwitchState", false);
        mysSwitch.setChecked(mysSwitchState);
        xqtdSwitch.setChecked(xqtdSwitchState);
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
        if (RandomStringGenerator.getUUID(Setting.this).equals("null")){
            RandomStringGenerator.saveUUID(RandomStringGenerator.generateUUID(), Setting.this);
        }
        if (RandomStringGenerator.readRandomString(Setting.this).equals("null")){
            RandomStringGenerator.saveRandomString(Setting.this, RandomStringGenerator.generateRandomString());
        }
    }

}
