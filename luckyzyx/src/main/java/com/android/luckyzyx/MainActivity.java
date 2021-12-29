package com.android.luckyzyx;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.luckyzyx.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.android.luckyzyx.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // 将每个菜单标识作为一组标识传递，因为每个
        // menu should be considered as top level destinations.
        // 菜单应被视为顶级目的地。
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Toast提示
        Button btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(v -> Toast.makeText(this, "Toast: 点击按钮", Toast.LENGTH_SHORT).show());
        //对话框
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("标题")
                    .setMessage("消息")
                    .setCancelable(true)
                    .setPositiveButton("确定", (dialog, which) -> Toast.makeText(this,"点击确定",Toast.LENGTH_SHORT).show())
                    .setNegativeButton("取消",  (dialog, which) -> Toast.makeText(this,"点击取消",Toast.LENGTH_SHORT).show())
                    .setNeutralButton("中间",  (dialog, which) -> Toast.makeText(this,"点击中间",Toast.LENGTH_SHORT).show());
            alert.create().show();
        });
        //跳转activity
        Button activity = findViewById(R.id.activity);
        activity.setOnClickListener(v -> {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        });
        //关闭当前activity
        Button close = findViewById(R.id.close);
        close.setOnClickListener(v -> this.finish());

        //获取root权限
        findViewById(R.id.root).setOnClickListener(v -> get_root());
    }
    // 获取ROOT权限
    public void get_root(){
        if (is_root()){
            Toast.makeText(this, "已经具有ROOT权限!", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                Toast.makeText(this, "正在获取ROOT权限!", Toast.LENGTH_SHORT).show();
                Runtime.getRuntime().exec("su");
            }
            catch (Exception e){
                Toast.makeText(this, "获取ROOT权限时出错!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 判断是否具有ROOT权限
    public static boolean is_root() {
        boolean res = false;
        try {
            res = new File("/system/bin/su").exists();
        } catch (Exception ignored) {

        }
        return res;
    }

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
        menu.add(1, 0, 0, "关于");
        menu.add(999, 999, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    }
    // 菜单项被选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case 999:
                exit();
                break;
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

    //退出APP事件
    public void exit() {
        System.exit(0);
    //overridePendingTransition(0, 0);
    }

}