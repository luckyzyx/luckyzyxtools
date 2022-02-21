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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;
import com.luckyzyx.tools.utils.Shellfun;

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
        xposed_info.setText("版本: " + BuildConfig.VERSION_NAME + "\n版本号: " + BuildConfig.VERSION_CODE);

        initMagisk();

        //Button
        MaterialButton xposed = requireActivity().findViewById(R.id.xposed_btn);
        xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));
        MaterialButton magisk = requireActivity().findViewById(R.id.magisk_btn);
        magisk.setOnClickListener(v -> startActivity(new Intent(requireActivity(), MagiskActivity.class)));
        MaterialButton fps = requireActivity().findViewById(R.id.fps);
        fps.setOnClickListener(v -> MainActivity.setfps(requireActivity()));
        MaterialButton checkupdate = requireActivity().findViewById(R.id.checkupdate);
        checkupdate.setOnClickListener(v -> Snackbar.make(v, "检查个毛的更新!", Snackbar.LENGTH_SHORT).show());

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
        updatelog_text.setText("在写了在写了!");

        TextView systeminfo = requireActivity().findViewById(R.id.systeminfo);
        systeminfo.setText(getSystemInfo());
    }

    //Magisk
    @SuppressLint({"SetTextI18n", "SdCardPath"})
    private void initMagisk() {
        MaterialTextView magisk_title = requireActivity().findViewById(R.id.magisk_title);
        MaterialTextView magisk_info = requireActivity().findViewById(R.id.magisk_info);

        String moduleName = "luckyzyx_tools";
        String moduleDir = "/data/adb/modules/luckyzyx_tools";
        String moduleProp = "/data/adb/modules/luckyzyx_tools/module.prop";

        String[] magisk_version = {Shellfun.grep_prop(), "grep_prop version "+moduleProp};
        ShellUtils.CommandResult magisk_version_result = ShellUtils.execCommand(magisk_version,true,true);
        String[] magisk_versioncode = {Shellfun.grep_prop(), "grep_prop versionCode "+moduleProp};
        ShellUtils.CommandResult magisk_versioncode_result = ShellUtils.execCommand(magisk_versioncode,true,true);

        if (magisk_version_result.result==0 && magisk_versioncode_result.result==0){
            magisk_title.setText(magisk_title.getText()+":已安装");
            magisk_info.setText("版本: "+magisk_version_result.successMsg+"\n版本号: "+magisk_versioncode_result.successMsg);
        }else{
            magisk_title.setText(magisk_title.getText()+":未安装");
            magisk_info.setText("版本: null\n版本号: null");
        }

//        String[] magisk = {Shellfun.grep_prop(), "grep_prop magisk /data/adb/modules/luckyzyx_tools/module.propp"};
//        ShellUtils.CommandResult result = ShellUtils.execCommand(magisk,true,true);
//
//        MaterialTextView log = requireActivity().findViewById(R.id.log);
//        log.setText(result.allMsg);
    }

    private String getSystemInfo() {
        String[] str = {
                "厂商: " + Build.BRAND +
                        "\n型号: " + Build.MODEL +
                        "\nAndroid版本: " + Build.VERSION.RELEASE +
                        "\nSDK API: " + Build.VERSION.SDK +
                        "\n设备参数: " + Build.DEVICE +
                        "\n版本号: " + Build.DISPLAY
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
        switch (item.getItemId()) {
            case R.id.refresh:
                MainActivity.refreshmode(requireActivity());
                break;
            case R.id.settings:
                startActivity(new Intent(requireActivity(), SettingsActivity.class));
                break;
        }
        return false;
    }

}