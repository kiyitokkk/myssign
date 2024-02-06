package com.example.mys;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
public class MyDialog {
    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置对话框的标题和消息
        builder.setTitle(title);
        builder.setMessage(message);
        // 设置对话框的按钮，这里只添加了一个"确定"按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 在这里处理确定按钮的点击事件
            dialog.dismiss(); // 关闭对话框
        });
        // 创建并显示对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public static void showInputDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入cookie");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputEditText = dialogView.findViewById(R.id.inputEditText);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String inputText = inputEditText.getText().toString();
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", inputText);
            editor.apply();
            String savedText = sharedPreferences.getString("cookie", "null");
            TextView cookietext = ((Activity) context).findViewById(R.id.cookies);
            if (!savedText.equals("null")){
                cookietext.setText("cookie: 已添加");
            }
            RandomStringGenerator.saveUUID(RandomStringGenerator.generateUUID(), context);
            RandomStringGenerator.saveRandomString(context, RandomStringGenerator.generateRandomString());
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void  showInputDialoggenshin(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入原神uid");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputEditText = dialogView.findViewById(R.id.inputEditText);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String inputText = inputEditText.getText().toString();
            SharedPreferences sharedPreferences = context.getSharedPreferences("genshin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("genshin", inputText);
            editor.apply();
            String savedText = sharedPreferences.getString("genshin", "uid is null");
            TextView genshinTextView = ((Activity) context).findViewById(R.id.genshinTextView);
            genshinTextView.setText("原神uid: " + savedText);
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void showInputDialogxqtd(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入铁道uid");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputEditText = dialogView.findViewById(R.id.inputEditText);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String inputText = inputEditText.getText().toString();
            SharedPreferences sharedPreferences = context.getSharedPreferences("xqtd", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("xqtd", inputText);
            editor.apply();
            String savedText = sharedPreferences.getString("xqtd", "uid is null");
//                String savedText = sharedPreferences.getString("genshin", "未添加");
            TextView xqtdTextView = ((Activity) context).findViewById(R.id.xqtdTextView);
            xqtdTextView.setText("铁道uid: " + savedText);
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
