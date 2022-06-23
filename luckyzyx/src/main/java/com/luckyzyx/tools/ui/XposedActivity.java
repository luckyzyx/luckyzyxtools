package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.XposedAndroid;
import com.luckyzyx.tools.ui.fragment.XposedSystemOther;
import com.luckyzyx.tools.ui.fragment.XposedSystemUI;
import com.luckyzyx.tools.ui.fragment.XposedUserApp;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.Objects;

public class XposedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.activity_xposed);
        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initFragments();
    }

    //Tabs + Fragment
    private void initFragments() {
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("系统框架"),0,true);
        tabs.addTab(tabs.newTab().setText("系统界面"),1,false);
        tabs.addTab(tabs.newTab().setText("系统其他"),2,false);
        tabs.addTab(tabs.newTab().setText("三方APP"),3,false);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //添加选中Tab的逻辑
                switch (tab.getPosition()){
                    case 0:
                        switchFragment(new XposedAndroid());
                        break;
                    case 1:
                        switchFragment(new XposedSystemUI());
                        break;
                    case 2:
                        switchFragment(new XposedSystemOther());
                        break;
                    case 3:
                        switchFragment(new XposedUserApp());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //添加未选中Tab的逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
            }
        });

        //设置默认选中item
        switchFragment(new XposedAndroid());
    }

    //跳转Fragment函数
    private void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.tabs_container,fragment).commitNow();
    }

    //创建Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"停止系统界面");
        menu.add(0,2,1,"停止时钟");
//        menu.add(0,3,1,"停止设置");
        menu.add(0,4,1,"停止系统桌面");
        menu.add(0,5,1,"停止主题商店");
        menu.add(0,6,1,"停止应用包安装器");
        menu.add(0,9,1,"停止好多动漫");
        return super.onCreateOptionsMenu(menu);
    }

    //菜单栏
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.openOptionsMenu();
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case 1:
                ShellUtils.execCommand("kill -9 `pgrep systemui`",true);
                break;
            case 6:
                ShellUtils.execCommand("am force-stop com.android.packageinstaller",true);
                break;
            case 5:
                ShellUtils.execCommand("am force-stop com.heytap.themestore",true);
                break;
            case 4:
                ShellUtils.execCommand("am force-stop com.android.launcher",true);
                break;
            case 3:
                ShellUtils.execCommand("am force-stop com.android.settings",true);
                break;
            case 2:
                ShellUtils.execCommand("am force-stop com.coloros.alarmclock",true);
                break;
            case 9:
                ShellUtils.execCommand("am force-stop com.east2d.everyimage",true);
                break;
            default:
                Toast.makeText(this, "错误->"+item.getItemId()+":"+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}