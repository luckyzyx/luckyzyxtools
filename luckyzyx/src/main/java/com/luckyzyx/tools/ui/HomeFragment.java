package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;

import java.io.File;

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
        if (activity != null){
            activity.setSupportActionBar(toolbar);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        CardView cardview = requireActivity().findViewById(R.id.cardview);
        cardview.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));

        TextView condition_app = requireActivity().findViewById(R.id.condition_app);
        condition_app.setText(getAppInfo());
        TextView condition_module = requireActivity().findViewById(R.id.condition_module);
        condition_module.setText(getModuleInfo());

        Button btn_magisk = requireActivity().findViewById(R.id.btn_magisk);
        btn_magisk.setOnClickListener(v -> MainActivity.alertdialog(requireActivity()));

        TextView systeminfo = requireActivity().findViewById(R.id.systeminfo);
        systeminfo.setText(getSystemInfo());
    }

    private String getSystemInfo() {
        String[] str = {
                "Product Model: \n" +
                "型号"+android.os.Build.MODEL + "\n" +
                "SDK API"+android.os.Build.VERSION.SDK + "\n" +
                "Android版本: "+android.os.Build.VERSION.RELEASE
        };
        return str[0];
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
        switch(item.getItemId()){
            case R.id.refresh:
                MainActivity.refreshmode(requireActivity());
                break;
            case R.id.settings:
            startActivity(new Intent(requireActivity(), SettingsActivity.class));
                break;
        }
        return true;
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