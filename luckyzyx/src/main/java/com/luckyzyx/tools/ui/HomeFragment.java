package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;

import java.io.File;
import java.util.List;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardview = requireActivity().findViewById(R.id.cardview);
        cardview.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));

        TextView condition_app = requireActivity().findViewById(R.id.condition_app);
        condition_app.setText(getAppInfo());
        TextView condition_module = requireActivity().findViewById(R.id.condition_module);
        condition_module.setText(getModuleInfo());

        TextView btn_magisk = requireActivity().findViewById(R.id.btn_magisk);
        btn_magisk.setOnClickListener(v -> MainActivity.alertdialog(requireActivity()));

        TextView appinfo = requireActivity().findViewById(R.id.appinfo);
        appinfo.setText("aaa");

    }



    public String getAppInfo() {
        String appName = BuildConfig.APPLICATION_ID;
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        String[] info ={
                "App: "+appName+"\n"+
                "内置版本: "+versionName+"\n"+
                "内置版本号: " + versionCode
        };
        return info[0];
    }

    public String getModuleInfo() {
        String moduleName = "luckyzyx_tools";
        String moduleDir = "/data/adb/modules/"+moduleName;
        String versionName = null;
        int versionCode = 0;
        if(!new File(moduleDir).exists()){
            moduleName = versionName = "未安装";
        }
        String[] info ={
                "Magisk: "+moduleName+"\n"+
                "版本: "+versionName+"\n"+
                "版本号: "+versionCode
        };
        return info[0];
    }
}