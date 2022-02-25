package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.SPUtils;
import com.luckyzyx.tools.utils.ShellUtils;
import com.luckyzyx.tools.utils.Shellfun;


public class ModuleInstallFragment extends Fragment {

    private static final String PREFERENCE_NAME = "MagiskSettings";

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
        initMagiskInstall();
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
    private void initMagiskInstall() {
        MaterialTextView magisk_title = requireActivity().findViewById(R.id.magisk_title);
        MaterialTextView magisk_info = requireActivity().findViewById(R.id.magisk_info);
        MaterialButton install_btn = requireActivity().findViewById(R.id.install_btn);
        MaterialButton modify_brand_btn = requireActivity().findViewById(R.id.modify_brand_btn);
        MaterialButton app_avatar_btn = requireActivity().findViewById(R.id.app_avatar_btn);

        MaterialTextView log = requireActivity().findViewById(R.id.installlog);

        String[] magisk_version = {Shellfun.grep_prop(), "grep_prop version "+moduleProp};
        ShellUtils.CommandResult magisk_version_result = ShellUtils.execCommand(magisk_version,true,true);
        String[] magisk_versioncode = {Shellfun.grep_prop(), "grep_prop versionCode "+moduleProp};
        ShellUtils.CommandResult magisk_versioncode_result = ShellUtils.execCommand(magisk_versioncode,true,true);

        if (magisk_version_result.result==0 && magisk_versioncode_result.result==0){
            magisk_title.setText("附加: 已安装");
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
            magisk_title.setText("附加: 未安装");
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
            }else {
                bottomSheetDialog.show();
            }
        });

        //修改机型
        modify_brand_btn.setOnClickListener(v -> {
            AlertDialog modify_brand_dialog = new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("修改机型")
                    .setMessage("机型信息请勿填写特殊符号!可能会导致系统读取出错!\n若以下各项都未填写,点击确定后将移除修改机型功能!\n以免冲突,确保其他机型模块已关闭或卸载!")
                    .setView(R.layout.modify_brand)
                    .show();

            TextInputEditText brand_text = modify_brand_dialog.findViewById(R.id.brand_text);
            TextInputEditText model_text = modify_brand_dialog.findViewById(R.id.model_text);
            TextInputEditText name_text = modify_brand_dialog.findViewById(R.id.name_text);
            TextInputEditText device_text = modify_brand_dialog.findViewById(R.id.device_text);
            TextInputEditText manufacturer_text = modify_brand_dialog.findViewById(R.id.manufacturer_text);
            MaterialButton write_btn = modify_brand_dialog.findViewById(R.id.write_btn);

            assert write_btn != null;
            write_btn.setOnClickListener(v1 -> {
                Editable brand = brand_text != null ? brand_text.getText() : null;
                Editable name = name_text != null ? name_text.getText() : null;
                Editable model = model_text != null ? model_text.getText() : null;
                Editable manufacturer = manufacturer_text != null ? manufacturer_text.getText() : null;
                Editable device = device_text != null ? device_text.getText() : null;
                boolean uninstall = brand.equals("") && name.equals("") && model.equals("") && manufacturer.equals("") && device.equals("");
                String[] commands = {
                        "if [[ -f "+modulesystemProp+" ]]; then\n"+
                        "    if [[ ! "+uninstall+" ]]; then\n"+
                        "        cat <<zyx >>"+modulesystemProp+"\n" +
                        "ro.product.brand="+brand+"\n"+
                        "ro.product.name="+name+"\n"+
                        "ro.product.model="+model+"\n"+
                        "ro.product.manufacturer="+manufacturer+"\n"+
                        "ro.product.device="+device+"\n"+
                        "zyx\n" +
                        "    else\n"+
                        "        sed -i -e '/ro.product.brand/d' "+modulesystemProp+"\n"+
                        "        sed -i -e '/ro.product.name/d' "+modulesystemProp+"\n"+
                        "        sed -i -e '/ro.product.model/d' "+modulesystemProp+"\n"+
                        "        sed -i -e '/ro.product.manufacturer/d' "+modulesystemProp+"\n"+
                        "        sed -i -e '/ro.product.device/d' "+modulesystemProp+"\n"+
                        "    fi\n"+
                        "fi"
                };
                ShellUtils.CommandResult modify_brand_Msg = ShellUtils.execCommand(commands,true,true);
                if (modify_brand_Msg.result==0){
                    modify_brand_dialog.dismiss();
                    Snackbar.make(requireActivity().findViewById(R.id.coordinator),"机型修改成功!",Snackbar.LENGTH_SHORT).show();
                }else{
                    modify_brand_dialog.dismiss();
                    Snackbar.make(requireActivity().findViewById(R.id.coordinator),"机型修改出错!",Snackbar.LENGTH_SHORT).show();
                    install_log=modify_brand_Msg.allMsg;
                    MaterialTextView log_text = bottomSheetDialog.findViewById(R.id.log_text);
                    assert log_text != null;
                    log_text.setText(install_log);
                }
            });

        });

    }

    //安装
    public void install(){
        boolean fingerprint_repair = SPUtils.getBoolean(requireActivity(),PREFERENCE_NAME,"fingerprint_repair",false);
        boolean statusbar_developer_warn = SPUtils.getBoolean(requireActivity(),PREFERENCE_NAME,"statusbar_developer_warn",false);

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
                "chmod -Rf 777 "+moduleDir,

                //指纹修复
                "if [[ "+fingerprint_repair+" == true ]]; then\n"+
                "    cat <<zyx >>"+modulesystemProp+"\n" +
                "ro.boot.flash.locked=0\n" +
                "ro.boot.vbmeta.device_state=unlocked\n" +
                "ro.boot.verifiedbootstate=orange\n" +
                "zyx\n" +
                "    cat <<zyx >>"+moduleService+"\n" +
                "while [ \"\\$(getprop sys.boot_completed)\" != \"1\" ]; do\n" +
                "  sleep 1\n" +
                "done\n" +
                "resetprop ro.boot.flash.locked 1\n" +
                "resetprop ro.boot.vbmeta.device_state locked\n" +
                "resetprop ro.boot.verifiedbootstate green\n" +
                "zyx\n"+
                "fi",
                //开发者警告
                "if [[ "+statusbar_developer_warn+" == true ]]; then\n"+
                "    mkdir -p "+moduleSystemDir+"etc/permissions/\n"+
                "    cat <<zyx >>"+moduleSystemDir+"etc/permissions/developer_features.xml\n"+
                "<permissions>\n" +
                "    <feature name=\"oppo.systemui.highlight.nodeveloper\" />\n" +
                "    <feature name=\"oppo.settings.verification.dialog.disallow\"/>\n" +
                "    <feature name=\"oppo.settings.account.dialog.disallow\" />\n" +
                "</permissions>\n" +
                "zyx\n" +
                "chmod -Rf 644 "+moduleSystemDir+"etc/permissions/developer_features.xml\n"+
                "fi"
        };

        ShellUtils.CommandResult installcommandslog = ShellUtils.execCommand(installcommands,true,true);
        if (installcommandslog.result==0){
            Snackbar.make(requireActivity().findViewById(R.id.coordinator),"安装完成!",Snackbar.LENGTH_SHORT).show();
            initMagiskInstall();
        }else{
            Snackbar.make(requireActivity().findViewById(R.id.coordinator),"安装错误!",Snackbar.LENGTH_SHORT).show();
            install_log=installcommandslog.allMsg;
            MaterialTextView log_text = bottomSheetDialog.findViewById(R.id.log_text);
            assert log_text != null;
            log_text.setText(install_log);
        }
    }

}