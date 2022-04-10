package com.luckyzyx.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.adapter.UpdateLogAdapter;
import com.luckyzyx.tools.utils.bean.UpdateLogBean;

import java.util.ArrayList;
import java.util.List;

public class UpdateLog {

    private final Context context;

    public UpdateLog(Context context){
        this.context = context;
    }

    private List<UpdateLogBean> UpdateLogList() {
        List<UpdateLogBean> logslist = new ArrayList<>();
        logslist.add(new UpdateLogBean("更新日志", "命名规则: {版本名}.{版本号}"));

        logslist.add(new UpdateLogBean("2.0.2293", "更新Yuki API\n修复lpparam.appinfo报错"));
        logslist.add(new UpdateLogBean("2.0.2287", "添加Hook时的包名日志打印\n采用R8混淆"));
        logslist.add(new UpdateLogBean("2.0.2253", "更新Yuki API\n优化混淆\n添加时钟显秒\n优化其他页面卡顿"));
        logslist.add(new UpdateLogBean("2.0.2224", "检查更新移至我的页面\n添加Hook 应用包安装程序(适配ing)"));
        logslist.add(new UpdateLogBean("2.0.2206", "移除启动时检查更新的延迟\nYuki API 更新\n修改版本号判断"));
        logslist.add(new UpdateLogBean("2.0.2186", "修复检查更新失败后的崩溃\n添加设置页面\n设置添加启动时检查选项\n优化代码结构与命名"));
        logslist.add(new UpdateLogBean("2.0.2186", "优化更新日志"));
        logslist.add(new UpdateLogBean("2.0.2181", "添加更新日志功能\n修复Xposed页面bug"));
        logslist.add(new UpdateLogBean("2.0.2136", "优化检测更新逻辑\n更换更新链接"));
        logslist.add(new UpdateLogBean("2.0.2135", "更新Hook API\n优化检测更新对话框逻辑"));
        logslist.add(new UpdateLogBean("2.0.2065", "添加检查更新功能"));
        logslist.add(new UpdateLogBean("2.0.1881", "Xposed更换为YukiHookAPI kotlin"));
        logslist.add(new UpdateLogBean("2.0.1823", "修改关于页面\n添加机型修改模板"));
        logslist.add(new UpdateLogBean("2.0.1708", "添加修改机型模板功能"));
        logslist.add(new UpdateLogBean("2.0.1664", "添加修改机型移除功能\n优化无线adb调试判断"));
        logslist.add(new UpdateLogBean("2.0.1627", "实装安装内置模块功能"));
        logslist.add(new UpdateLogBean("2.0.1505", "添加Magisk页面\n精简部分layout"));
        logslist.add(new UpdateLogBean("2.0.1424", "优化Preference卡顿"));
        logslist.add(new UpdateLogBean("2.0.1419", "优化代码结构\n添加SnackBar\n优化Magisk检测"));
        logslist.add(new UpdateLogBean("2.0.1306", "添加无线adb调试\n移除机型判断\n代码逻辑优化"));
        logslist.add(new UpdateLogBean("2.0.1239", "修复ToolBar bug\n重写ToolBar的Menu监听"));
        logslist.add(new UpdateLogBean("2.0.1010", "添加系统框架作用域以解决无限弹XSP修复问题"));
        logslist.add(new UpdateLogBean("2.0.996", "初步适配Material"));
        logslist.add(new UpdateLogBean("2.0.759", "添加V1 V2签名"));
        logslist.add(new UpdateLogBean("2.0.367", "添加Other页面和进程管理"));
        logslist.add(new UpdateLogBean("2.0.233", "移除Preference icon"));
        logslist.add(new UpdateLogBean("2.0.214", "添加Hook好多动漫"));
        logslist.add(new UpdateLogBean("2.0.208", "修复XSP读取问题"));
        logslist.add(new UpdateLogBean("2.0.52", "添加XSP"));
        logslist.add(new UpdateLogBean("2.0.17", "添加Xposed页面"));
        logslist.add(new UpdateLogBean("2.0.1", "修复更新后模块不更新bug"));
        logslist.add(new UpdateLogBean("2.0", "首次构建APP"));
        return logslist;
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
//        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
//        BottomSheetBehavior.from(Objects.requireNonNull(bottomSheetInternal)).setPeekHeight(600);

        bottomSheetDialog.show();
    }
}
