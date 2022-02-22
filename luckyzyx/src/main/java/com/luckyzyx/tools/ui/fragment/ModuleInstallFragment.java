package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;
import com.luckyzyx.tools.utils.Shellfun;

public class ModuleInstallFragment extends Fragment {

    String id = "luckyzyx_tools";
    String name = "luckyzyx Tools";
    String version = BuildConfig.VERSION_NAME;
    int versioncode = BuildConfig.VERSION_CODE;
    String author = "忆清鸣、luckyzyx";
    String description = "luckyzyx Tools附加模块,跟随APP更新,配置信息请在APP查看,一切操作默认重启生效!";

    String moduleDir = "/data/adb/modules/luckyzyx_tools";
    String moduleSystemDir = "/data/adb/modules/luckyzyx_tools/system/";
    String moduleProp = "/data/adb/modules/luckyzyx_tools/module.prop";
    String modulesystemProp = "/data/adb/modules/luckyzyx_tools/system.prop";
    String moduleService = "/data/adb/modules/luckyzyx_tools/service.sh";
    String modulePostfsdata = "/data/adb/modules/luckyzyx_tools/post-fs-data.sh";
    String moduleUninstall = "/data/adb/modules/luckyzyx_tools/uninstall.sh";

    BottomSheetDialog bottomSheetDialog;
    String install_log = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initBottomSheet();
        initMagisk();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module_install, container, false);
    }

    //初始化BottomSheet
    public void initBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheetInternal != null;
        BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(400);
    }

    //初始化Magisk
    @SuppressLint("SetTextI18n")
    private void initMagisk() {
        MaterialTextView magisk_title = requireActivity().findViewById(R.id.magisk_title);
        MaterialTextView magisk_info = requireActivity().findViewById(R.id.magisk_info);
        MaterialButton install_btn = requireActivity().findViewById(R.id.install_btn);

        MaterialTextView log = requireActivity().findViewById(R.id.installlog);

        String[] magisk_version = {Shellfun.grep_prop(), "grep_prop version "+moduleProp};
        ShellUtils.CommandResult magisk_version_result = ShellUtils.execCommand(magisk_version,true,true);
        String[] magisk_versioncode = {Shellfun.grep_prop(), "grep_prop versionCode "+moduleProp};
        ShellUtils.CommandResult magisk_versioncode_result = ShellUtils.execCommand(magisk_versioncode,true,true);

        if (magisk_version_result.result==0 && magisk_versioncode_result.result==0){
            magisk_title.setText("Magisk: 已安装");
            String magiskinfo =
                    "版本: "+magisk_version_result.successMsg+
                            "\n版本号: "+magisk_versioncode_result.successMsg+
                            "\n\n注: 务必在模块选项页面勾选需要的模块,点击安装/重刷后将应用新选项"+
                            "\n\n模块安装 --> 安装模块"+
                            "\n模块选项 --> 选择需要安装的内置模块"+
                            "\n模块管理 --> 管理已安装的Magisk模块"
                    ;
            magisk_info.setText(magiskinfo);
            if (BuildConfig.VERSION_CODE>Integer.parseInt(magisk_versioncode_result.successMsg)){
                install_btn.setText("更新模块");
            }else{
                install_btn.setText("安装/重刷");
            }
        }else{
            magisk_title.setText("Magisk: 未安装");
            magisk_info.setText("版本: null\n版本号: null");
        }

        //点击安装监听
        install_btn.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(install_btn.getText())
                        .setMessage("是否确定安装/重刷模块,\n这将会删除/覆盖原模块!")
                        .setPositiveButton("确定", (dialog, which) -> install())
                        .setNegativeButton("点错了",null)
                        .show());

        //安装日志监听
        log.setOnClickListener(v -> {
            if (install_log.equals("")){
                Snackbar.make(v,"没出错看什么日志!",Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    //安装
    public void install(){
        String[] installcommands = {
                //重建module目录
                "if [[ -d "+moduleDir+" ]];then\n" +
                        "    rm -rf "+moduleDir+"\n"+
                        "    mkdir -p "+moduleSystemDir+"\n" +
                        "fi",
                //重写module.prop
                "    cat <<zyx >"+moduleProp+"\n" +
                        "id="+ id +"\n" +
                        "name="+ name +"\n" +
                        "version="+ version +"\n" +
                        "versionCode="+ versioncode +"\n" +
                        "author="+ author +"\n" +
                        "description="+ description +"\n"+
                        "zyx",
                //重写post-fs-data.sh
                "    cat <<zyx >"+modulePostfsdata+"\n" +
                        "MODDIR=\\${0%/*}\n"+
                        "zyx",
                //重写service.sh
                "    cat <<zyx >"+moduleService+"\n" +
                        "MODDIR=\\${0%/*}\n"+
                        "zyx",
                //重写uninstall.sh
                "    cat <<zyx >"+moduleUninstall+"\n" +
                        "rm -rf "+moduleDir+"\n"+
                        "zyx",
                //重写system.prop
                "echo '' >"+modulesystemProp,
                //设置权限
                "chmod -Rf 777 "+moduleDir
        };
        ShellUtils.CommandResult installcommandslog = ShellUtils.execCommand(installcommands,true,true);
        if (installcommandslog.result==0){
            Snackbar.make(requireActivity().findViewById(R.id.coordinator),"安装完成!",Snackbar.LENGTH_SHORT).show();
            initMagisk();
        }else{
            Snackbar.make(requireActivity().findViewById(R.id.coordinator),"安装错误!",Snackbar.LENGTH_SHORT).show();
            install_log=installcommandslog.allMsg;
            MaterialTextView log_text = bottomSheetDialog.findViewById(R.id.log_text);
            assert log_text != null;
            log_text.setText(install_log);
        }
    }

}