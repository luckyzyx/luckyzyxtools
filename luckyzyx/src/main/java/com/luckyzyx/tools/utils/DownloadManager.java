//package com.luckyzyx.tools.utils;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Environment;
//import android.webkit.MimeTypeMap;
//
//public class DownloadManager {
//
//    private BroadcastReceiver downloadCompleteReceiver;
//
//    public static void DownloadApk(Context context,String apkUrl, String outFileName) {
//        DownloadManager downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(apkUrl));
//        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
//        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_MOBILE | android.app.DownloadManager.Request.NETWORK_WIFI);
//        //设置是否允许漫游
//        request.setAllowedOverRoaming(true);
//        //设置文件类型
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkUrl));
//        request.setMimeType(mimeString);
//        //在通知栏中显示
//        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        //设置下载的标题信息
//        request.setTitle(outFileName);
//        //下载目录 Download + 文件名
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outFileName);
//        // 将下载请求放入队列
//        long downloadid = downloadManager.enqueue(request);
//        //执行下载任务时注册广播监听下载成功状态
//        registerBroadcast();
//    }
//
//    //注册ContentObserver
//    private void registerContentObserver() {
//        if (downloadObserver != null) {
//            requireActivity().getContentResolver().registerContentObserver(Uri.parse(url), true, downloadObserver);
//        }
//    }
//
//    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            /**下载完成后安装APK**/
//            installApk();
//        }
//    };
//}
