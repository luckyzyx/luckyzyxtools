package com.luckyzyx.tools.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;

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

    private final String jsonurl = "https://raw.fastgit.org/luckyzyx/luckyzyxtools/main/luckyzyx/release/output-metadata.json";
    private String newFileUrl;


    private String newPackageName;
    private String newVariantName;

    private String newVersionName;
    private String newVersionCode;
    private String newFileName;
    private File newFile;

    private AlertDialog update_dialog;

    private DownloadManager downloadManager;
    private BroadcastReceiver broadcastReceiver;
    private long downloadid;

    public HttpUtils(Context context){
        this.context = context;
    }

    //Okhttp发送请求并回调
    public static void sendRequestWithOkhttp(String url,okhttp3.Callback callback) {
        new Thread(() -> {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            client.newCall(request).enqueue(callback);
        }).start();
    }

    //获取更新json
    public void CheckUpdate(View view){

        sendRequestWithOkhttp(jsonurl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Snackbar.make(view,"检查更新失败!",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);

                    //包名
                    newPackageName = jsonObject.getString("applicationId");
                    //构建类型
                    newVariantName = jsonObject.getString("variantName");
                    //版本,版本号,输出文件名
                    newVersionName = jsonObject.getJSONArray("elements").getJSONObject(0).getString("versionName");
                    newVersionCode = jsonObject.getJSONArray("elements").getJSONObject(0).getString("versionCode");
                    newFileName = jsonObject.getJSONArray("elements").getJSONObject(0).getString("outputFile");

                    //版本号不同
                    if (Integer.parseInt(newVersionCode) != BuildConfig.VERSION_CODE){
                        Looper.prepare();
                        showDialog();
                        Looper.loop();
                    }else{
                        Snackbar.make(view,"已是最新版本!",Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //显示更新对话框
    private void showDialog(){
        //对话框
        update_dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("检测到新版本!")
                .setMessage("新版本: "+newVersionName+"_"+newVersionCode+"\n当前版本: "+BuildConfig.VERSION_NAME+"_"+BuildConfig.VERSION_CODE)
                .setView(R.layout.update_dialog)
                .show();
        //按钮事件
        MaterialButton cancelUpdate_btn = update_dialog.findViewById(R.id.cancelUpdate_btn);
        Objects.requireNonNull(cancelUpdate_btn).setOnClickListener(v -> update_dialog.dismiss());
        MaterialButton startUpdate_btn = update_dialog.findViewById(R.id.startUpdate_btn);
        Objects.requireNonNull(startUpdate_btn).setOnClickListener(v -> {
//            newFileUrl = "https://raw.fastgit.org/luckyzyx/luckyzyxtools/main/luckyzyx/release/"+newFileName;
            newFileUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
            newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), newFileName);

            System.out.print("点击更新按钮");
            checkPermission();
            }
        );
    }

    //开始下载
    private void checkPermission() {
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
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

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
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outFileName);
        //Android/data/packageName/file/Download/name.apk
        request.setDestinationUri(Uri.fromFile(newFile));
        // 将下载请求放入队列
        downloadid = downloadManager.enqueue(request);
        //执行下载任务时注册广播监听下载成功状态
        listener(downloadid);
    }

    //监听下载完成
    private void listener(final long downloadID) {

        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == downloadID) {
                    Toast.makeText(context, "下载完成!", Toast.LENGTH_LONG).show();
                }
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

}
