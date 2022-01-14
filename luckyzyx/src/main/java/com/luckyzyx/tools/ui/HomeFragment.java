package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_xposed = requireActivity().findViewById(R.id.btn_xposed);
        btn_xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));

        TextView condition = requireActivity().findViewById(R.id.condition);
        condition.setText(getModuleInfo());
    }

    public String getModuleInfo(){
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        String[] info ={
                "内置版本:"+versionName+"\n"+
                "内置版本号:" + versionCode+"\n"+ ""
        };
        return info[0];
    }
}