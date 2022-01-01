package com.android.luckyzyx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.luckyzyx.ui.DashboardFragment;
import com.android.luckyzyx.ui.HomeFragment;
import com.android.luckyzyx.ui.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private DashboardFragment dashboardFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        dashboardFragment = new DashboardFragment();
        userFragment = new UserFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        switchFragment(homeFragment);


    }

    //NavigationItem被选择事件
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_item_home:
                    switchFragment(homeFragment);
                    break;
                case R.id.nav_item_dashboard:
                    switchFragment(dashboardFragment);
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
        fragmentTransaction.replace(R.id.nav_container,fragment).commitNow();
    }

    //创建Menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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
        menu.add(0, 0, 0, "设置").setIcon(R.drawable.ic_baseline_settings_24).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    // 菜单项被选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
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

    //判断root
    public static boolean is_root() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath)){
            return true;
        }else return new File(xBinPath).exists() && isExecutable(xBinPath);
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            Log.i("RootUtil", str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    //退出APP事件
    public void exit() {
        System.exit(0);
    }

}