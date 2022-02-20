package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Xposed
        MaterialTextView xposed_title = requireActivity().findViewById(R.id.xposed_title);
        xposed_title.setText( "忆清鸣、luckyzyx");
        MaterialTextView xposed_info = requireActivity().findViewById(R.id.xposed_info);
        xposed_info.setText( "版本: " + BuildConfig.VERSION_NAME+"\n版本号: " + BuildConfig.VERSION_CODE);
        //Magisk
        MaterialTextView magisk_title = requireActivity().findViewById(R.id.magisk_title);
        magisk_title.setText( "忆清鸣、luckyzyx");
        MaterialTextView magisk_info = requireActivity().findViewById(R.id.magisk_info);
        magisk_info.setText( "版本: " + BuildConfig.VERSION_NAME+"\n版本号: " + BuildConfig.VERSION_CODE);
        //Button
        MaterialButton xposed = requireActivity().findViewById(R.id.xposed_btn);
        xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));
        MaterialButton magisk = requireActivity().findViewById(R.id.magisk_btn);
        magisk.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));

        //BottomSheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.updatelog_sheet);
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheetInternal != null;
        BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(400);
        MaterialCardView updatelog_card = requireActivity().findViewById(R.id.updatelog_card);
        updatelog_card.setOnClickListener(v -> bottomSheetDialog.show());

        MaterialTextView updatelog_text = bottomSheetDialog.findViewById(R.id.updatelog_text);
        assert updatelog_text != null;
//        updatelog_text.setText();

        TextView systeminfo = requireActivity().findViewById(R.id.systeminfo);
        systeminfo.setText(getSystemInfo());
    }

    private String getSystemInfo() {
        String[] str = {
                "厂商: "+ Build.BRAND+
                "\n型号: "+Build.MODEL +
                "\nAndroid版本: "+Build.VERSION.RELEASE+
                "\nSDK API: "+Build.VERSION.SDK+
                "\n设备参数: "+Build.DEVICE+
                "\n版本号: "+Build.DISPLAY
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