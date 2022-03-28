package com.luckyzyx.tools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.luckyzyx.tools.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private final Context context;

    //gitee update.json
    @SuppressWarnings("FieldCanBeLocal")
    private final String jsonurl = "https://gitee.com/luckyzyx/luckyzyx/raw/master/luckyzyxtools/update.json";

    private String updatejson;//updatejson url

    private String newPackageName;//包名
    private String newVersionName;//版本名
    private String newVersionCode;//版本号
    private String newFileName;//文件名
    private String newFileUrl;//文件url链接
    private File newFile;//本地File文件s路径

    private long downloadid;//下载进程ID

    public HttpUtils(Context context){
        this.context = context;
    }

    //Okhttp发送请求并回调
    public static void sendRequestWithOkhttp(String url,okhttp3.Callback callback) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(callback);
        }).start();
    }

    //获取更新json
    public void CheckUpdate(View view,boolean showToast){
        if (showToast) {
            Snackbar.make(view, "查询中...", Snackbar.LENGTH_SHORT).show();
        }
        //通过gitee获取github json
        sendRequestWithOkhttp(jsonurl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                updatejson = null;
                if (showToast) {
                    Snackbar.make(view, "检查更新失败!", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    updatejson = jsonObject.getString("updatejson");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //OkHttp3获取json回调
        sendRequestWithOkhttp(updatejson, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (showToast){
                    Snackbar.make(view,"检查更新失败!",Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    //包名
                    newPackageName = jsonObject.getString("packagename");
                    //版本,版本号,输出文件名
                    newVersionName = jsonObject.getString("versionname");
                    newVersionCode = jsonObject.getString("versioncode");
                    newFileName = jsonObject.getString("outputfilename");
                    newFileUrl = jsonObject.getString("outputfileurl");
                    newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), newFileName);

                    //显示对话框
                    if (showToast) {
                        Looper.prepare();
                        showUpdateDialog();
                        Looper.loop();
                    }else {
                        //版本号不同
                        if (Integer.parseInt(newVersionCode) != BuildConfig.VERSION_CODE){
                            Looper.prepare();
                            showUpdateDialog();
                            Looper.loop();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //显示更新对话框
    private void showUpdateDialog(){
        //对话框
        AlertDialog update_dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("检查更新!")
                .setMessage("最新版本: " + newVersionName + "_" + newVersionCode + "\n当前版本: " + BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE)
                .setCancelable(false)
                .setPositiveButton("更新", (dialog, which) -> startUpdate())
                .setNeutralButton("取消", null)
                .show();
        if (Integer.parseInt(newVersionCode) == BuildConfig.VERSION_CODE){
            update_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("无需更新");
            update_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(null);
        }
    }

    //开始下载
    private void startUpdate() {
        //下载前删除目录并输出删除状态
        System.out.print(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).delete());
        //检查写入权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //启动服务
            DownloadApk();
        }
    }

    //下载
    private void DownloadApk() {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(newFileUrl));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | android.app.DownloadManager.Request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(true);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(newFileUrl));
        request.setMimeType(mimeString);
        //在通知栏中显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置下载的标题信息
        request.setTitle(newFileName);
        //下载目录 Download + 文件名
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outFileName);
        //Android/data/packageName/file/Download/name.apk
        request.setDestinationUri(Uri.fromFile(newFile));
        // 将下载请求放入队列
        downloadid = downloadManager.enqueue(request);
        //注册广播监听下载状态
        registBroadCast();
    }

    //注册广播
    private void registBroadCast() {
        //注册下载完毕广播
        IntentFilter filter_complete = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver_complete, filter_complete);
        //注册下载过程中点击广播
        IntentFilter filter_clicked = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(receiver_clicked, filter_clicked);
    }

    // 注册广播监听系统的下载完成事件
    BroadcastReceiver receiver_complete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (reference == downloadid) {
                installApk();
            }
        }
    };

    //注册广播监听系统的下载点击事件
    BroadcastReceiver receiver_clicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
            long[] references = intent.getLongArrayExtra(extraID);
            for (long reference : references) {
                if (reference == downloadid) {
                    System.out.print("点击了广播: " + downloadid);
                }
            }
        }
    };

    //检查安装权限并跳转安装
    @SuppressLint("ObsoleteSdkInt")
    private void installApk() {
        //Android大于8.0并且开启了未知应用安装权限
        if (Build.VERSION.SDK_INT >= 26 && context.getPackageManager().canRequestPackageInstalls()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".FileProvider", newFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "请开启未知应用安装权限!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setData(Uri.parse("package:"+BuildConfig.APPLICATION_ID));
            if (Build.VERSION.SDK_INT >= 26) {
                intent.setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            }else {
                intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
            }
            context.startActivity(intent);
        }
    }
}