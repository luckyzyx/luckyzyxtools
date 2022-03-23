package com.luckyzyx.tools.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DownLoadService extends Service{

    private static final int DOWNLOADFLAG = 1;
    private DownloadManager downloadManager;
    private long downloadId;
    private DownLoadService downloadService;
    private DownLoadProgressListener downloadProgressListener;
    private ScheduledExecutorService executor;
    private static final String TAG = "DownLoadService";
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (downloadProgressListener != null && msg.what == DOWNLOADFLAG) {
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    downloadProgressListener.onProgress((int) (msg.arg1 * 1f / msg.arg2 * 100f));
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        downloadService = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String apkUrl = intent.getStringExtra("apkUrl");
        String apkName = intent.getStringExtra("apkName");
        downLoadApk(apkUrl,apkName);
        return new DownLoadBinder();
    }

    public String queryStatus() {
        if (downloadId == 0) {
            return "暂无状态";
        }
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        String statusMsg = "";
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_RUNNING:
                    statusMsg = "STATUS_RUNNING";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    statusMsg = "STATUS_SUCCESSFUL";
                    break;
                case DownloadManager.STATUS_FAILED:
                    statusMsg = "STATUS_FAILED";
                    break;
                default:
                    statusMsg = "未知状态";
                    break;
            }
        }
        return statusMsg;
    }

    public class DownLoadBinder extends Binder {
        public DownLoadService getService() {
            return downloadService;
        }
    }

    private void downLoadApk(String apkUrl,String apkName) {
        //获取DownLoadManager对象
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = getDownloadId(apkUrl, apkName);
        getCurrentProgress();
        registBroadCast();
    }

    private void registBroadCast() {
        //注册下载完毕广播
        IntentFilter filter_complete = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver_complete, filter_complete);
        //注册下载过程中点击广播
        IntentFilter filter_clicked = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(receiver_clicked, filter_clicked);
    }

    private void getCurrentProgress() {
        //轮训获取进度
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            try (Cursor cursor = downloadManager.query(query)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int downloadSoFar = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int totalSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    //线程过度
                    Message message = Message.obtain();
                    message.what = DOWNLOADFLAG;
                    message.arg1 = downloadSoFar;
                    message.arg2 = totalSize;
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: ", e);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private long getDownloadId(String url, String apkName) {
        //构建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //创建文件保存路径
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        //设置下载限定类型（此处允许wifi下载）默认无限制
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载过程中显示，下载完毕后显示（下载完毕后点击直接安装）
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//下载过程中显示，下载完毕后消失
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);//下载过程中隐藏，下载完毕后显示
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);//全程不显示，需要在Menifast文件中加入：DOWNLOAD_WITHOUT_NOTIFICATION权限
        request.setTitle(apkName);
//        request.setDescription("正在下载");
        //设置文件存放目录
        request.setDestinationUri(Uri.fromFile(file));
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,appName);
        //默认DownLoadManager下载的文件无法被系统应用扫描到，此设置为可以扫描到//已弃用
//        request.setVisibleInDownloadsUi(true);
        //加入下载队列
        return downloadManager.enqueue(request);
    }

    //点击监听
    BroadcastReceiver receiver_clicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
            long[] references = intent.getLongArrayExtra(extraID);
            for (long reference : references) {
                if (reference == downloadId) {
                    Log.i(TAG, "onReceive: clicked: downLoadId_qq" + downloadId);
                }
            }
        }
    };

    //下载完成监听
    BroadcastReceiver receiver_complete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (reference == downloadId) {
                Log.i(TAG, "onReceive: complete: downLoadId_qq" + downloadId);
                if (downloadProgressListener != null) {
                    downloadProgressListener.onProgress(100);
                }
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                if (uri != null) {
                    Log.i(TAG, "onReceive: complete: 下载完成马上安装gogogo");
//                    APKUtil.installApk(context, file);
                }
                closeDownLoad();
            }
        }
    };

    private void closeDownLoad() {
        handler.removeCallbacksAndMessages(null);
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver_clicked);
        unregisterReceiver(receiver_complete);
    }

    public void setDownLoadProgressListener(DownLoadProgressListener downLoadProgressListener) {
        this.downloadProgressListener = downLoadProgressListener;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        closeDownLoad();
        unregisterReceiver();
        //此方法终止当前服务，回调onDestroy销毁服务。否则热启动不会回调onBind方法
        stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        //设置取消下载
        int i = downloadManager.remove(downloadId);//夜神会清除apk
        Log.i(TAG, "onDestroy: " + i);
    }
}
interface DownLoadProgressListener { void onProgress(int progress);}

/*
class DownloadedApkInfo {

    public DownloadedApkInfo(String packageName, String version, int versionCode, String appName) {
        this.packageName = packageName;
        this.version = version;
        this.appName = appName;
        this.versionCode = versionCode;
    }

    public String packageName;
    public String version;
    public String appName;
    public int versionCode;

    @Override
    public String toString() {
        return "DownloadedApkInfo{" +
                "packageName='" + packageName + '\'' +
                ", version='" + version + '\'' +
                ", appName='" + appName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}*/
