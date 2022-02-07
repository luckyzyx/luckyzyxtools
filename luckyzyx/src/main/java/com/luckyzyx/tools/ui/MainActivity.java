package com.luckyzyx.tools.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private OtherFragment otherFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //处理有地图的Fragment第一次切换时 会黑屏闪一下的问题。
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        //底部导航栏
        homeFragment = new HomeFragment();
        otherFragment = new OtherFragment();
        userFragment = new UserFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        switchFragment(homeFragment);

        CheckXposed();
        CheckBrand();
    }
    //NavigationItem被选择事件
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
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
        }
    };
    //跳转Fragment函数
    private void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //移除切换fragment时闪烁的动画效果
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.nav_container,fragment).commitNow();
    }

    //创建Menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //引用menu文件
        //getMenuInflater().inflate(R.menu.menu,menu);
        /*
         * add()方法的四个参数，依次是：
         * 1、组别，如果不分组的话就写Menu.NONE,
         * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         * 4、文本，菜单的显示文本
         */
        // setIcon()方法为菜单设置图标，这里使用的是系统自带的图标
        // 以android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的

        //  menu.addSubMenu("一级菜单").add(0,0,0,"二级菜单");
        menu.add(0, 0, 0, "重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 0, "设置").setIcon(R.drawable.ic_baseline_settings_24).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    // 菜单项被选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            refreshmode();
        }
        if (item.getItemId() == 1) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return false;
    }

    // 菜单被显示之前的事件
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //选项菜单显示之前onPrepareOptionsMenu方法会被调用
        //如果返回false，此方法就把用户点击menu的动作取消，onCreateOptionsMenu方法将不会被调用
        return super.onPrepareOptionsMenu(menu);
    }

//    com.oplus.engineermode
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
        File first = new File(getFilesDir().getAbsoluteFile() + "/nofirst/");
        File oppof = new File(getFilesDir().getAbsoluteFile() + "/oppo/");
        File oplusf = new File(getFilesDir().getAbsoluteFile() + "/oplus/");
        if (!first.exists()) {
            boolean oppo = Appexist(this,"com.oppo.engineermode");
            boolean oplus = Appexist(this,"com.oplus.engineermode");
            if (oppo==oplus){
                new AlertDialog.Builder(this)
                        .setMessage("检测机型出错,请联系作者修复")
                        .setCancelable(false)
                        .show();
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("首次启动")
                        .setMessage("检测机型为:"+(oppo?"OPPO":"OnePlus")+"\n如若有误请联系作者")
                        .setCancelable(false)
                        .setPositiveButton("确定", (dialog, which) -> {
                            //noinspection ResultOfMethodCallIgnored
                            first.mkdir();
                            if (oppo) {
                                //noinspection ResultOfMethodCallIgnored
                                oppof.mkdir();
                            } else {
                                //noinspection ResultOfMethodCallIgnored
                                oplusf.mkdir();
                            }
                        })
                        .setNeutralButton("联系作者", (dialog, which) -> {
                            Uri uri = Uri.parse("http://www.coolapk.com/u/1930284");
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .show();
            }
        }
    }

    //初始化Xposed XSharedPreferences
    @SuppressLint("WorldReadableFiles")
    public void CheckXposed() {
        try {
            getSharedPreferences("XposedSettings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("OtherSettings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("Settings", Context.MODE_WORLD_READABLE);
        } catch (SecurityException exception) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> System.exit(0))
                    .setNegativeButton(R.string.ignore, null)
                    .show();
        }
    }

    //关机菜单
    public void refreshmode(){
        final String[] list = {"重启", "关机", "Recovery", "fastboot"};
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setItems(list, (dialog, which) -> {
                    switch (list[which]){
                        case "重启":
                            ShellUtils.execCommand("reboot",true,false);
                            break;
                        case "关机":
                            ShellUtils.execCommand("reboot -p",true,false);
                            break;
                        case "Recovery":
                            ShellUtils.execCommand("reboot recovery",true,false);
                            break;
                        case "fastboot":
                            ShellUtils.execCommand("reboot bootloader",true,false);
                            break;
                    }
                })
                .show();
    }

    @Deprecated
    //对话框demo
    public void alertdialog(){
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("title")
                .setMessage("message")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> {

                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setNeutralButton("Neutral", (dialog, which) -> {

                })
                .show();
    }
}