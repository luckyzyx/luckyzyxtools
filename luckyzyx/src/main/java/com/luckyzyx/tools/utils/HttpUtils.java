package com.luckyzyx.tools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.ui.SettingsActivity;

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

    private String jsonurl;
    private final String jsonfile = "output-metadata.json";
    private final String GithubUrl = "https://raw.githubusercontent.com/luckyzyx/luckyzyxtools/main/luckyzyx/release/";
    private final String FastGitUrl = "https://ghproxy.futils.com/https://github.com/luckyzyx/luckyzyxtools/blob/main/luckyzyx/release/";
    private final String iQDNSUrl = "https://raw.iqiq.io/luckyzyx/luckyzyxtools/main/luckyzyx/release/";
    private String checkupdate_settings;

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

    //显示更新对话框
    public void ShowUpdateDialog(boolean isShow){
        checkupdate_settings = SPUtils.getString(context,"Settings","checkupdate_settings","Github");
        jsonurl = GithubUrl+jsonfile;
        if (!isShow){
            CheckUpdate(false,jsonurl);
            return;
        }
        new MaterialAlertDialogBuilder(context)
                .setTitle("检查更新")
                .setMessage("当前更新源: "+checkupdate_settings+"\n官方源永久有效,检查失败请科学上网\n其他更新源无需科学上网,但不保证永久有效")
                .setCancelable(true)
                .setNeutralButton("切换更新源", (dialog, which) -> context.startActivity(new Intent(context, SettingsActivity.class)))
                .setPositiveButton("确定", (dialog, which) -> {
                    switch (checkupdate_settings){
                        case "Github":
                            jsonurl = GithubUrl+jsonfile;newFileUrl = GithubUrl;
                            break;
                        case "FastGit":
                            jsonurl = FastGitUrl+jsonfile;newFileUrl = FastGitUrl;
                            break;
                        case "iQDNS":
                            jsonurl = iQDNSUrl+jsonfile;newFileUrl = iQDNSUrl;
                            break;
                    }
                    CheckUpdate(true, jsonurl);
                })
                .show();
    }

    //获取更新json
    public void CheckUpdate(boolean showToast,String url) {
        //OkHttp3获取json回调
        sendRequestWithOkhttp(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                Toast.makeText(context, "检查更新失败!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    //包名
                    newPackageName = jsonObject.getString("applicationId");
                    //版本,版本号,输出文件名
                    newVersionName = jsonObject.getString("versionName");
                    newVersionCode = jsonObject.getString("versionCode");
                    newFileName = jsonObject.getString("outputFile");
                    newFileUrl += newFileName;
                    newFile = new File(context.getExternalCacheDir(), newFileName);

                    //判断包名
                    if (newPackageName.equals(BuildConfig.APPLICATION_ID)){
                        //判断版本号
                        if (Integer.parseInt(newVersionCode) > BuildConfig.VERSION_CODE){
                            Looper.prepare();
                            ShowDownloadDialog();
                            Looper.loop();
                        }else if(showToast){
                            Looper.prepare();
                            Toast.makeText(context, "已是最新版本!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }else {
                        Looper.prepare();
                        Toast.makeText(context, "云端文件错误!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //显示下载对话框
    public void ShowDownloadDialog() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("检查更新!")
                .setMessage("最新版本: " + newVersionName + "_" + newVersionCode + "\n当前版本: " + BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE)
                .setCancelable(false)
                .setPositiveButton("更新", (dialog, which) -> startUpdate())
                .setNeutralButton("取消", null)
                .show();
    }
    //开始下载
    private void startUpdate() {
        //下载前删除目录并输出删除状态
//        if (!context.getExternalCacheDir().delete()){
//            Toast.makeText(context, "下载缓存删除失败!", Toast.LENGTH_SHORT).show();
//        }
        //检查写入权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //启动服务
            DownloadApk();
            Toast.makeText(context, "下载中...", Toast.LENGTH_SHORT).show();
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
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, newFileName);
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