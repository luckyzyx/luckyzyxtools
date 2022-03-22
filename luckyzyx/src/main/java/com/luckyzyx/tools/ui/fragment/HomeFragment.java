package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.MagiskActivity;
import com.luckyzyx.tools.ui.MainActivity;
import com.luckyzyx.tools.ui.AboutActivity;
import com.luckyzyx.tools.ui.XposedActivity;
import com.luckyzyx.tools.utils.HttpUtils;
import com.luckyzyx.tools.utils.ShellUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.topAppBar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Xposed
        MaterialTextView xposed_info = requireActivity().findViewById(R.id.xposed_info);
        xposed_info.setText("版本: " + BuildConfig.VERSION_NAME + "\n版本号: " + BuildConfig.VERSION_CODE/*+"\nXposed: "+XposedCheck*/);

        //Button
        MaterialButton xposed = requireActivity().findViewById(R.id.xposed_btn);
        xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));
        MaterialButton magisk = requireActivity().findViewById(R.id.magisk_btn);
        magisk.setOnClickListener(v -> startActivity(new Intent(requireActivity(), MagiskActivity.class)));
        MaterialButton fps = requireActivity().findViewById(R.id.fps);
        fps.setOnClickListener(v -> MainActivity.setfps(requireActivity()));
        MaterialButton checkupdate = requireActivity().findViewById(R.id.checkupdate);
        checkupdate.setOnClickListener(this::CheckUpdate);

        //BottomSheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior.from(Objects.requireNonNull(bottomSheetInternal)).setPeekHeight(400);
        MaterialCardView updatelog_card = requireActivity().findViewById(R.id.updatelog_card);
        updatelog_card.setOnClickListener(v -> bottomSheetDialog.show());

        MaterialTextView updatelog_text = bottomSheetDialog.findViewById(R.id.log_text);
        Objects.requireNonNull(updatelog_text).setText("aaaa");

        TextView systeminfo = requireActivity().findViewById(R.id.systeminfo);
        systeminfo.setText(getSystemInfo());
    }

    private String getSystemInfo() {
        String[] str = {
                "厂商: " + Build.BRAND +
                        "\n型号: " + Build.MODEL +
                        "\nAndroid版本: " + Build.VERSION.RELEASE +
                        "\n版本号: " + Build.DISPLAY+
                        "\nSDK API: " + Build.VERSION.SDK_INT +
                        "\n设备参数: " + Build.DEVICE +
                        "\n闪存厂商: "+ ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry",true,true).successMsg
        };
        return str[0];
    }


    //获取更新json
    public void CheckUpdate(View view){
        String jsonurl = "https://raw.fastgit.org/luckyzyx/luckyzyxtools/main/luckyzyx/release/output-metadata.json";

        HttpUtils.sendRequestWithOkhttp(jsonurl, new Callback() {
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
                    String applicationId = jsonObject.getString("applicationId");
                    //构建类型
                    String variantName = jsonObject.getString("variantName");
                    //版本,版本号
                    String versionName = jsonObject.getJSONArray("elements").getJSONObject(0).getString("versionName");
                    String versionCode = jsonObject.getJSONArray("elements").getJSONObject(0).getString("versionCode");
                    if (Integer.parseInt(versionCode) > BuildConfig.VERSION_CODE){
                        Looper.prepare();
                        new MaterialAlertDialogBuilder(requireActivity())
                                .setTitle("检测到新版本!")
                                .setMessage("新版本: "+versionName+"_"+versionCode+"\n当前版本: "+BuildConfig.VERSION_NAME+"_"+BuildConfig.VERSION_CODE)
                                .setPositiveButton("更新", (dialog, which) -> {
                                    Uri uri = Uri.parse("https://luckyzyx.lanzouy.com/b05m8maoh");
                                    startActivity(new Intent().setAction("android.intent.action.VIEW").setData(uri));
                                })
                                .setNegativeButton("取消",null)
                                .show();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.refresh:
                MainActivity.refreshmode(requireActivity());
                break;
            case R.id.about:
                startActivity(new Intent(requireActivity(), AboutActivity.class));
                break;
        }
        return false;
    }

}