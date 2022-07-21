package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.joom.paranoid.Obfuscate;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.HomeFragment;
import com.luckyzyx.tools.ui.fragment.OtherFragment;
import com.luckyzyx.tools.ui.fragment.UserFragment;
import com.luckyzyx.tools.utils.HttpUtils;
import com.luckyzyx.tools.utils.SPUtils;
import com.luckyzyx.tools.utils.ShellUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

@Obfuscate
public class MainActivity extends AppCompatActivity {

    //底部导航栏
    private final HomeFragment homeFragment = new HomeFragment();
    private final OtherFragment otherFragment = new OtherFragment();
    private final UserFragment userFragment = new UserFragment();

    private boolean XposedStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckTheme(this);
        setContentView(R.layout.activity_main);
        InitPermission();
        CheckXposed();
        //初始化APPCommit
        InitHookAPP();
        //startCheckUpdate();
        //初始化BottomNavigationView底部导航栏
        initBottomNavigationView();
        //获取Root权限
        ShellUtils.checkRootPermission();

    }

    //初始化BottomNavigationView底部导航栏
    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigationView(){
        NavigationBarView navigationBarView = findViewById(R.id.nav_item);
        //设置监听方法_NavigationItem被选择事件
        navigationBarView.setOnItemSelectedListener(item -> {
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
        });
        //设置默认选中item
        switchFragment(homeFragment);
        //设置默认选中item
        navigationBarView.setSelectedItemId(R.id.nav_item_home);
        //设置选中显示label
        navigationBarView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
    }


    //跳转Fragment函数
    private void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.nav_container,fragment).commitNow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.nav_container);
            if (current instanceof OtherFragment) finishAndRemoveTask();
        }
        return super.onKeyDown(keyCode, event);
    }

    //判断是否暗色模式
    public static boolean isDarkTheme(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        int flag = uiModeManager.getNightMode();
        return flag == UiModeManager.MODE_NIGHT_YES;
    }

    //判断主题
    public static void CheckTheme(Context context){
        if (!isDarkTheme(context)){
            context.setTheme(R.style.Theme_Luckyzyx_sakura);
        }else {
            context.setTheme(R.style.Theme_Luckyzyx_sakura_night);
        }
    }

    //初始化Xposed XSharedPreferences
    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    public void CheckXposed() {
        try {
            getSharedPreferences("Settings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("XposedSettings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("OtherSettings", Context.MODE_WORLD_READABLE);
            getSharedPreferences("MagiskSettings", Context.MODE_WORLD_READABLE);
        } catch (SecurityException ignored) {
            XposedStatus = false;
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> System.exit(0))
                    //.setNegativeButton(R.string.ignore, null)
                    .show();
        }
    }

    //初始化Hook APP
    private void InitHookAPP(){
        if(XposedStatus){
            String[] keylist = {"PackageInstallCommit","OplusGamesCommit"};
            String[] packlist = {"com.android.packageinstaller","com.oplus.games"};
            for(int i = 0; i < keylist.length; i++) {
                if (getAppCommit(packlist[i]).equals("error")){
                    Toast.makeText(this, keylist[i]+":"+getAppCommit(packlist[i]), Toast.LENGTH_SHORT).show();
                }
                SPUtils.putString(this,"XposedSettings",keylist[i],getAppCommit(packlist[i]));
            }
        }
   }

    //获取APPCommit API
    private String getAppCommit(String packageName) {
        try {
            PackageManager packageManager = this.getPackageManager();
            return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData.getString("versionCommit");
        } catch (PackageManager.NameNotFoundException exception) {
//            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();
            return "error";
        }
    }

    //存储权限
    private void InitPermission(){
        String[] permissionList = new String[]{Permission.REQUEST_INSTALL_PACKAGES,Permission.MANAGE_EXTERNAL_STORAGE};
        if (XXPermissions.isGranted(MainActivity.this, permissionList)){
            return;
        }
        XXPermissions.with(this)
            .permission(permissionList)
            .request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (!all) {
                        Toast.makeText(MainActivity.this, permissions+"权限获取失败!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    if (never) {
                        Toast.makeText(MainActivity.this, permissions+"拒绝授权,请手动授予权限!", Toast.LENGTH_SHORT).show();
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                    } else {
                        Toast.makeText(MainActivity.this, permissions+"获取权限失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    //启动时检查更新
    @SuppressWarnings("unused")
    private void startCheckUpdate(){
        if (SPUtils.getBoolean(this,"Settings","startCheckUpdate",false)){
            new HttpUtils(this).ShowUpdateDialog(false);
        }
    }

    //检测包名是否存在
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

    //重启自身
    @SuppressWarnings("unused")
    public static void reStart(Context context){
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //复制文件
    public static void copyFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                FileInputStream inputStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream outputStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 10];
                int byteread;
                while ( (byteread = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteread);
                }
                inputStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    //复制整个文件夹
    @SuppressWarnings("unused")
    public void copyFolder(String oldPath, String newPath) {
        try {
            //noinspection ResultOfMethodCallIgnored
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;
            for (int i = 0; i < Objects.requireNonNull(file).length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()));
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }

    //关机菜单
    public static void refreshmode(Context context){
        final String[] list = {"重启系统界面","重启系统","关闭系统","Recovery","BootLoader"};
        new MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setItems(list, (dialog, which) -> {
                    switch (list[which]){
                        case "重启系统界面":
                            ShellUtils.execCommand("kill -9 `pgrep systemui`", true);
                            break;
                        case "重启系统":
                            ShellUtils.execCommand("reboot",true);
                            break;
                        case "关闭系统":
                            ShellUtils.execCommand("reboot -p",true);
                            break;
                        case "Recovery":
                            ShellUtils.execCommand("reboot recovery",true);
                            break;
                        case "BootLoader":
                            ShellUtils.execCommand("reboot bootloader",true);
                            break;
                    }
                })
                .show();
    }

    //全局刷新率
    //90Hz -> settings put secure oplus_customize_screen_refresh_rate 1
    public static void setfps(Context context){
        new MaterialAlertDialogBuilder(context)
                .setTitle("FPS")
                .setMessage("手动强制全局刷新率 重启恢复默认值\n支持在状态栏创建磁贴Tile\n与dfps模块冲突 会导致不生效等问题\n例: 60Hz/90Hz 60Hz/120Hz\n最低即60Hz 最高即90Hz/120Hz\n具体以系统为准")
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