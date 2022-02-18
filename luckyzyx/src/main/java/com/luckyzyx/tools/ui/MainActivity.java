package com.luckyzyx.tools.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.SPUtils;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private OtherFragment otherFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckBrand();
        CheckXposed();
        CheckTheme(this);
        setContentView(R.layout.activity_main);

        //底部导航栏
        homeFragment = new HomeFragment();
        otherFragment = new OtherFragment();
        userFragment = new UserFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_item);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //设置默认选中item
        switchFragment(homeFragment);
        //设置默认选中item
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        //设置选中动画
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
    }

    //NavigationItem被选择事件
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = item -> {
                switch (item.getItemId()){
                    case R.id.nav_item_home:
                        switchFragment(homeFragment);
                        break;
                    case R.id.nav_item_other:
                        switchFragment(otherFragment);
                        break;
                    case R.id.nav_item_user:
                        switchFragment(userFragment);
                        break;
                }
                return true;
            };

    //跳转Fragment函数
    private void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.nav_container,fragment).commitNow();
    }

    //判断主题
    public static void CheckTheme(Context context){
        context.setTheme(R.style.Theme_Luckyzyx_sakura);
//        String theme = SPUtils.getString(context,"theme","material");
//        switch (theme){
//            default:
//                context.setTheme(R.style.Theme_Luckyzyx);
//                break;
//            case "purple":
//                context.setTheme(R.style.Theme_Luckyzyx_purple);
//                break;
//            case "red":
//                context.setTheme(R.style.Theme_Luckyzyx_red);
//                break;
//            case "yellow":
//                context.setTheme(R.style.Theme_Luckyzyx_yellow);
//                break;
//            case "orange":
//                context.setTheme(R.style.Theme_Luckyzyx_orange);
//                break;
//            case "green":
//                context.setTheme(R.style.Theme_Luckyzyx_green);
//                break;
//        }
    }

    //初始化Xposed XSharedPreferences
    @SuppressLint("WorldReadableFiles")
    public void CheckXposed() {
        try {
            getSharedPreferences("Settings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("XposedSettings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("OtherSettings", Context.MODE_WORLD_READABLE);
        } catch (SecurityException ignored) {
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> System.exit(0))
//                    .setNegativeButton(R.string.ignore, null)
                    .show();
        }
    }

    //检测包名
    //com.oplus.engineermode,com.oplus.engineermode
    public boolean Appexist(@NonNull Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        //获取手机系统的所有APP包名，然后进行比较
        @SuppressLint("QueryPermissionsNeeded")
        List<PackageInfo> applist = packageManager.getInstalledPackages(0);
        for (int i = 0; i < applist.size(); i++) {
            if (applist.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    //检测机型实行方案
    public void CheckBrand(){
        boolean firststart = SPUtils.getBoolean(this,"firststart",true);
        //若首次启动
        if (firststart) {
            boolean oppo = Appexist(this,"com.oppo.engineermode");
            boolean oplus = Appexist(this,"com.oplus.engineermode");
            if (oppo==oplus){
                new MaterialAlertDialogBuilder(this)
                        .setTitle("机型错误")
                        .setMessage("本模块仅适用于ColorOS11+\n判断机型出错,点击确定联系作者")
                        .setCancelable(false)
                        .setPositiveButton("确定", (dialog, which) -> {
                            ContactAuthor();
                            System.exit(0);
                        })
                        .show();
            }else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("欢迎使用")
                        .setMessage("检测到是首次使用\n判断机型为: "+(oppo?"OPPO":"OnePlus")+"\n如若有误请务必联系作者")
                        .setCancelable(false)
                        .setPositiveButton("确定", (dialog, which) -> {
                            SPUtils.putBoolean(this,"firststart",false);
                            SPUtils.putString(this,"brand",oppo?"OPPO":"OnePlus");
                        })
                        .show();
            }
        }
    }

    //联系作者_跳转URL
    void ContactAuthor(){
        Uri uri = Uri.parse("http://www.coolapk.com/u/1930284");
        startActivity(new Intent().setAction("android.intent.action.VIEW").setData(uri));
    }

    //重启自身
    static void reStart(Context context){
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //关机菜单
    public static void refreshmode(Context context){
        final String[] list = {"重启", "关机", "Recovery", "fastboot"};
        new MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setItems(list, (dialog, which) -> {
                    switch (list[which]){
                        case "重启":
                            ShellUtils.execCommand("reboot",true);
                            break;
                        case "关机":
                            ShellUtils.execCommand("reboot -p",true);
                            break;
                        case "Recovery":
                            ShellUtils.execCommand("reboot recovery",true);
                            break;
                        case "fastboot":
                            ShellUtils.execCommand("reboot bootloader",true);
                            break;
                    }
                })
                .show();
    }

    //对话框demo
    public static void alertdialog(Context context){
        new MaterialAlertDialogBuilder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("title")
                .setMessage("message")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 1",true))
                .setNegativeButton("Cancel", (dialog, which) -> ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 0",true))
                .setNeutralButton("Neutral", (dialog, which) -> ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 3",true))
                .show();
    }
}