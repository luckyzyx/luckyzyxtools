package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.AboutActivity;
import com.luckyzyx.tools.ui.MagiskActivity;
import com.luckyzyx.tools.ui.MainActivity;
import com.luckyzyx.tools.ui.XposedActivity;
import com.luckyzyx.tools.utils.ShellUtils;
import com.luckyzyx.tools.utils.UpdateLog;

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

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Xposed
        MaterialTextView xposed_info = requireActivity().findViewById(R.id.xposed_info);
        xposed_info.setText("版本: " + BuildConfig.VERSION_NAME + "\n版本号: " + BuildConfig.VERSION_CODE);

        //Button
        MaterialButton xposed = requireActivity().findViewById(R.id.xposed_btn);
        xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));
        MaterialButton magisk = requireActivity().findViewById(R.id.magisk_btn);
        magisk.setOnClickListener(v -> startActivity(new Intent(requireActivity(), MagiskActivity.class)));
        MaterialButton fps = requireActivity().findViewById(R.id.fps);
        fps.setOnClickListener(v -> MainActivity.setfps(requireActivity()));

        MaterialCardView updatelog_card = requireActivity().findViewById(R.id.updatelog_card);
        updatelog_card.setOnClickListener(v -> new UpdateLog(requireActivity()).ShowUpdateLog());

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.add(0,1,0,"重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0,2,0,"关于").setIcon(R.drawable.ic_baseline_info_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 1:
                MainActivity.refreshmode(requireActivity());
                break;
            case 2:
                startActivity(new Intent(requireActivity(), AboutActivity.class));
                break;
        }
        return false;
    }

}