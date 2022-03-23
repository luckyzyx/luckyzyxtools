package com.luckyzyx.tools.utils;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private final Context context;

    private String jsonurl = "https://raw.fastgit.org/luckyzyx/luckyzyxtools/main/luckyzyx/release/output-metadata.json";
    private String newFileUrl;


    private String newPackageName;
    private String newVariantName;
    ;
    private String newVersionName;
    private String newVersionCode;
    private String newFileName;

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
    public void showDialog(){
        AlertDialog update_dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("检测到新版本!")
                .setMessage("新版本: "+newVersionName+"_"+newVersionCode+"\n当前版本: "+BuildConfig.VERSION_NAME+"_"+BuildConfig.VERSION_CODE)
                .setView(R.layout.update_dialog)
                .show();
        MaterialButton cancelUpdate_btn = update_dialog.findViewById(R.id.cancelUpdate_btn);
        Objects.requireNonNull(cancelUpdate_btn).setOnClickListener(v -> update_dialog.dismiss());
        MaterialButton startUpdate_btn = update_dialog.findViewById(R.id.startUpdate_btn);
        Objects.requireNonNull(startUpdate_btn).setOnClickListener(v -> {
            newFileUrl = "https://raw.fastgit.org/luckyzyx/luckyzyxtools/main/luckyzyx/release/"+newFileName;
            Toast.makeText(context, "点击更新", Toast.LENGTH_SHORT).show();
            }
        );
    }


}
