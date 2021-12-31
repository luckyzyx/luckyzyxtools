package com.android.luckyzyx.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.luckyzyx.MainActivity;
import com.android.luckyzyx.R;
import com.android.luckyzyx.SettingsActivity;

import java.io.IOException;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toast提示
        Button btn1 = requireActivity().findViewById(R.id.toast);
        btn1.setOnClickListener(v -> Toast.makeText(requireActivity(), "Toast: 点击按钮", Toast.LENGTH_SHORT).show());
        //对话框
        Button btn2 = requireActivity().findViewById(R.id.alertdialog);
        btn2.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity())
                    .setTitle("标题")
                    .setMessage("确定--强制全局高刷\n取消--强制全局低刷\n中间--恢复默认")
                    .setCancelable(true)
                    .setPositiveButton("确定", (dialog, which) -> {
                        try {
                            Runtime.getRuntime().exec("su -c service call SurfaceFlinger 1035 i32 1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("取消",  (dialog, which) -> {
                        try {
                            Runtime.getRuntime().exec("su -c service call SurfaceFlinger 1035 i32 0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNeutralButton("中间",  (dialog, which) -> {
                        try {
                            Runtime.getRuntime().exec("su -c service call SurfaceFlinger 1035 i32 2");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            alert.create().show();
        });
        //跳转activity
        Button activity = requireActivity().findViewById(R.id.activity);
        activity.setOnClickListener(v -> {
            Intent intent=new Intent(requireActivity(), SettingsActivity.class);
            startActivity(intent);
        });
        //关闭当前activity
        Button close = requireActivity().findViewById(R.id.close);
        close.setOnClickListener(v -> requireActivity().finish());

        //获取root权限
        requireActivity().findViewById(R.id.root).setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity ) getActivity();
            assert mainActivity != null;
            mainActivity.get_root();
        });
    }


}