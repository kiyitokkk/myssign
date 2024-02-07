package com.example.mys;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SharedPreferences sharedPreferences;
    private NotificationHelper notificationHelper ;
    private  SharedPreferences sharedPreferences1;
    private static Context context;
    private static final int CONNECT_TIMEOUT = 40;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genshin();
        Log.e( "onCreate: ", "ssss");
//        if (Objects.equals(getIntent().getAction(), Intent.ACTION_MAIN)
//                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)) {
//
//            if (Objects.equals(getIntent().getAction(), "android.intent.action.VIEW")) {
//                genshin();
//                xqtd();
//            }
//            finish();
//            return;
//        }
    }
    public void genshin() {
        sharedPreferences = getSharedPreferences("button_states", MODE_PRIVATE);

        boolean mysSwitchState = sharedPreferences.getBoolean("mysSwitchState", false);

        if (mysSwitchState) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("genshin", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            String genshinuid = sharedPreferences.getString("genshin", "uid is null");
            String cookie = sharedPreferencesc.getString("cookie", "null");
            String result = Genshin(genshinuid, cookie, context);
            notificationHelper = new NotificationHelper(MainActivity.this);
            notificationHelper.showNotification("原神", result );
        }
    }
    public void xqtd() {
        sharedPreferences = getSharedPreferences("button_states", MODE_PRIVATE);
        boolean xqtdSwitchState = sharedPreferences.getBoolean("xqtdSwitchState", false);

        if (xqtdSwitchState) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("xqtd", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            String xqtduid = sharedPreferences.getString("xqtd", "uid is null");
            String cookie = sharedPreferencesc.getString("cookie", "null");
            String result = Xqtd(xqtduid, cookie, context);
            notificationHelper = new NotificationHelper(MainActivity.this);
            mHandler.post(() -> notificationHelper.showNotification1("星穹铁道", result  ));
        }
    }

    protected static String Genshin(String uid, String cookie, Context context) {
        String url = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign";
        String[] arr = {"c78eik", "5inm7p", "edm91m", "9k18gf", "0hh40n", "ojk2g2", "ag55ec", "80d6la"};
        long t = System.currentTimeMillis() / 1000;
        String salt = "QCRgj6bHHQvS0Rz03loexYSXpuiO3DZ6";
        Random random = new Random();
        String r = arr[random.nextInt(8)];
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        String device_model = Build.MODEL;
        String device = Build.MANUFACTURER + ' ' + Build.MODEL;
        String ds = getDS(salt, t, r);
        String requestBodyJson = String.format("{\"act_id\":\"e202009291139501\",\"region\":\"cn_gf01\",\"uid\":\"%s\"}", uid);
        Log.e("chen",requestBodyJson);
        RequestBody body = RequestBody.create(mediaType, requestBodyJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Host", "api-takumi.mihoyo.com")
                .addHeader("DS", ds)
                .header("Connection", "keep-alive")
                .header("x-rpc-platform", "android")
                .header("Origin", "https://webstatic.mihoyo.com")
                .header("x-rpc-device_model", device_model)
                .header("Sec-Fetch-Dest", "empty")
                .header("x-rpc-device_name", device)
                .header("x-rpc-device_fp", RandomStringGenerator.readRandomString(context))
                .header("x-rpc-client_type", "5")
                .header("x-rpc-app_version", "2.52.1")
                .header("User-Agent", "Mozilla/5.0 (Linux; " + "Android " +  Build.VERSION.RELEASE  + "; " +  Build.MODEL + " Build/" + Build.ID + ";wv" + ") AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/92.0.4515.131 Mobile Safari/537.36 miHoYoBBS/2.52.1")
                .header("x-rpc-device_id", RandomStringGenerator.getUUID(context))
                .header("Accept", "application/json, text/plain, */*")
                .header("x-rpc-channel", "huawei")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("x-rpc-sys_version", "9")
                .header("X-Requested-With", "com.mihoyo.hyperion")
                .header("Sec-Fetch-Site", "same-site")
                .header("Sec-Fetch-Mode", "cors")
                .header("Referer", "https://webstatic.mihoyo.com/bbs/event/signin/hkrpg/e202304121516551.html?bbs_auth_required=true&act_id=e202304121516551&bbs_auth_required=true&bbs_presentation_style=fullscreen&utm_source=bbs&utm_medium=mys&utm_campaign=icon")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("Cookie", cookie)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();

            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "发生错误";
    }
    public static String getDS(String salt, long t, String r) {
        String params = String.format("salt=%s&t=%d&r=%s", salt, t, r);
        System.out.println(params);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(params.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            String encryptMd5 = sb.toString();
            return t + "," + r + "," + encryptMd5;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected static String Xqtd(String uid, String cookie, Context context) {
        String url = "https://api-takumi.mihoyo.com/event/luna/sign";
        String[] arr = {"c78eik", "5inm7p", "edm91m", "9k18gf", "0hh40n", "ojk2g2", "ag55ec", "80d6la"};
        long t = System.currentTimeMillis() / 1000;
        String salt = "QCRgj6bHHQvS0Rz03loexYSXpuiO3DZ6";
        Random random = new Random();
        String r = arr[random.nextInt(8)];
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        String ds = getDS(salt, t, r);
        RequestBody body = RequestBody.create(mediaType, String.format("{\"act_id\":\"e202304121516551\",\"region\":\"prod_gf_cn\",\"uid\":\"%s\",\"lang\":\"zh-cn\"}", uid));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Host", "api-takumi.mihoyo.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("DS", ds)
                .addHeader("Origin", "https://webstatic.mihoyo.com")
                .addHeader("x-rpc-app_version", "2.52.1")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; " + "Android " +  Build.VERSION.RELEASE  + "; " +  Build.MODEL + " Build/" + Build.ID + ";wv" + ") AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/92.0.4515.131 Mobile Safari/537.36 miHoYoBBS/2.52.1")
                .addHeader("x-rpc-device_id", RandomStringGenerator.getUUID(context))
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("x-rpc-client_type", "5")
                .addHeader("X-Requested-With", "com.mihoyo.hyperion")
                .addHeader("Sec-Fetch-Site", "same-site")
                .addHeader("Referer", "https://webstatic.mihoyo.com/bbs/event/signin/hkrpg/e202304121516551.html?bbs_auth_required=true&act_id=e202304121516551&bbs_auth_required=true&bbs_presentation_style=fullscreen&utm_source=bbs&utm_medium=mys&utm_campaign=icon")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("Cookie", cookie)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "发生错误";
    }


}
