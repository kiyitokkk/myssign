package com.example.mys;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyWorker extends Worker {
    private Context context;
    private static NotificationHelper notificationHelper;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private static final int CONNECT_TIMEOUT = 40;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context.getApplicationContext();
        this.notificationHelper = new NotificationHelper(context);
    }
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String operation = inputData.getString("operation");
        SharedPreferences sharedPreferencestime = context.getSharedPreferences("button_states", MODE_PRIVATE);
        long genshinLastExecutionDate = sharedPreferencestime.getLong("genshinLastExecutionDate", 0);
        long xqtdLastExecutionDate = sharedPreferencestime.getLong("xqtdLastExecutionDate", 0);
        Calendar currentDate = Calendar.getInstance();
        int currentDay = currentDate.get(Calendar.DAY_OF_YEAR);
        if (operation != null) {
            if (operation.equals("operation1") && currentDay != genshinLastExecutionDate) {
                new Thread(() -> {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("genshin", MODE_PRIVATE);
                    SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", MODE_PRIVATE);
                    String genshinuid = sharedPreferences.getString("genshin", "uid is null");
                    String cookie = sharedPreferencesc.getString("cookie", "cookie is null");
                    String result = Genshin(genshinuid, cookie, context);
                    SharedPreferences.Editor editor = sharedPreferencestime.edit();
                    editor.putLong("genshinLastExecutionDate", currentDay);
                    editor.apply();
                    mHandler.post(() -> performOperation1("原神", result ));
                }).start();
            } else if (operation.equals("operation2") && currentDay != xqtdLastExecutionDate) {
                new Thread(() -> {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("xqtd", MODE_PRIVATE);
                    SharedPreferences sharedPreferencesc = context.getSharedPreferences("cookie", MODE_PRIVATE);
                    String xqtduid = sharedPreferences.getString("xqtd", "uid is null");
                    String cookie = sharedPreferencesc.getString("cookie", "cookie is null");
                    String result = Xqtd(xqtduid, cookie, context);
                    SharedPreferences.Editor editor = sharedPreferencestime.edit();
                    editor.putLong("xqtdLastExecutionDate", currentDay);
                    editor.apply();
                    mHandler.post(() -> performOperation2("星穹铁道", result ));
                }).start();
            }else {

            }
        } else {
            return Result.failure();
        }
        return Result.success();
    }
    private void performOperation1(String title, String content) {
        notificationHelper.showNotification(title, content);
    }
    private  void  performOperation2(String title, String content) {
        notificationHelper.showNotification1(title, content);
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