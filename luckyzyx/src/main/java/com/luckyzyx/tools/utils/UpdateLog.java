package com.luckyzyx.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.adapter.UpdateLogAdapter;
import com.luckyzyx.tools.utils.bean.LogBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateLog {

    private final Context context;

    public UpdateLog(Context context){
        this.context = context;
    }

    private List<LogBean> UpdateLogList() {
        List<LogBean> logsList = new ArrayList<>();
        logsList.add(new LogBean("更新日志", "命名规则: {版本名}.{版本号}  [r]: release  [d]: debug"));

        logsList.add(new LogBean("2.0.r2186", "优化更新日志"));
        logsList.add(new LogBean("2.0.r2181", "添加更新日志功能\n修复Xposed页面bug"));
        logsList.add(new LogBean("2.0.r2136", "优化检测更新逻辑\n更换更新链接"));
        logsList.add(new LogBean("2.0.r2135", "更新Hook API\n优化检测更新对话框逻辑"));
        logsList.add(new LogBean("2.0.r2065", "添加检查更新功能"));
        logsList.add(new LogBean("2.0.r1881", "Xposed更换为YukiHookAPI kotlin"));
        logsList.add(new LogBean("2.0.r1823", "修改关于页面\n添加机型修改模板"));
        logsList.add(new LogBean("2.0.r1708", "添加修改机型模板功能"));
        logsList.add(new LogBean("2.0.r1664", "添加修改机型移除功能\n优化无线adb调试判断"));
        logsList.add(new LogBean("2.0.r1627", "实装安装内置模块功能"));
        logsList.add(new LogBean("2.0.r1505", "添加Magisk页面\n精简部分layout"));
        logsList.add(new LogBean("2.0.r1424", "优化Preference卡顿"));
        logsList.add(new LogBean("2.0.r1419", "优化代码结构\n添加SnackBar\n优化Magisk检测"));
        logsList.add(new LogBean("2.0.r1306", "添加无线adb调试\n移除机型判断\n代码逻辑优化"));
        logsList.add(new LogBean("2.0.r1239", "修复ToolBar bug\n重写ToolBar的Menu监听"));
        logsList.add(new LogBean("2.0.r1010", "添加系统框架作用域以解决无限弹XSP修复问题"));
        logsList.add(new LogBean("2.0.r996", "初步适配Material"));
        logsList.add(new LogBean("2.0.r759", "添加V1 V2签名"));
        logsList.add(new LogBean("2.0.r367", "添加Other页面和进程管理"));
        logsList.add(new LogBean("2.0.r233", "移除Preference icon"));
        logsList.add(new LogBean("2.0.r214", "添加Hook好多动漫"));
        logsList.add(new LogBean("2.0.r208", "修复XSP读取问题"));
        logsList.add(new LogBean("2.0.r52", "添加XSP"));
        logsList.add(new LogBean("2.0.r17", "添加Xposed页面"));
        logsList.add(new LogBean("2.0.r1", "修复更新后模块不更新bug"));
        logsList.add(new LogBean("2.0", "首次构建APP"));
        return logsList;
    }

    //显示更新日志BottomSheet
    @SuppressLint("InflateParams")
    public void ShowUpdateLog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//        bottomSheetDialog.setContentView(R.layout.update_log_dialog);

        View view = LayoutInflater.from(context).inflate(R.layout.update_log_dialog_layout, null);
        RecyclerView logs = view.findViewById(R.id.update_log_list);
        logs.setLayoutManager(new LinearLayoutManager(context));
        UpdateLogAdapter logAdapter = new UpdateLogAdapter(UpdateLogList());
        logs.setAdapter(logAdapter);
        bottomSheetDialog.setContentView(view);

        //设置显示高度,必须先设置setContentView
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior.from(Objects.requireNonNull(bottomSheetInternal)).setPeekHeight(600);

        bottomSheetDialog.show();
    }
}
