package com.luckyzyx.tools.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.HomeFragment;
import com.luckyzyx.tools.ui.fragment.OtherFragment;
import com.luckyzyx.tools.ui.fragment.UserFragment;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private OtherFragment otherFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckPermission();
        CheckXposed();
        CheckTheme(this);
        setContentView(R.layout.activity_main);

        //底部导航栏
        homeFragment = new HomeFragment();
        otherFragment = new OtherFragment();
        userFragment = new UserFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_item);
        //设置监听方法
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //设置默认选中item
        switchFragment(homeFragment);
        //设置默认选中item
        bottomNavigationView.setSelectedItemId(R.id.nav_item_home);
        //设置选中显示label
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);

        //获取Root权限
        ShellUtils.checkRootPermission();
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
            getSharedPreferences("MagiskSettings", Context.MODE_WORLD_READABLE);
        } catch (SecurityException ignored) {
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> System.exit(0))
//                    .setNegativeButton(R.string.ignore, null)
                    .show();
        }
    }

    //存储权限
    private void CheckPermission(){
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //检测包名
    public static boolean APPexist(@NonNull Context context, String packageName) {
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

    //联系作者_跳转URL
    @SuppressWarnings("unused")
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
        final String[] list = {"重启", "关机", "recovery", "fastboot"};
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
                        case "recovery":
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
    public static void setfps(Context context){
        new MaterialAlertDialogBuilder(context)
                .setTitle("FPS")
                .setMessage("强制全局刷新率\n例: 60Hz/90Hz\n最高即90Hz 最低即60Hz\n详情请根据系统选项")
                .setCancelable(true)
                .setPositiveButton("最高", (dialog, which) -> {
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 1",true);
                    Toast.makeText(context, "已将FPS设置为最高!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("最低", (dialog, which) -> {
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 0",true);
                            Toast.makeText(context, "已将FPS设置为最低!", Toast.LENGTH_SHORT).show();
                        }
                )
                .setNeutralButton("恢复", (dialog, which) -> {
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 -1",true);
                    Toast.makeText(context, "已将FPS设置为默认!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}