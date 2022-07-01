package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.joom.paranoid.Obfuscate;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.XposedAndroid;
import com.luckyzyx.tools.ui.fragment.XposedSystemOther;
import com.luckyzyx.tools.ui.fragment.XposedSystemUI;
import com.luckyzyx.tools.ui.fragment.XposedUserApp;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.Objects;

@Obfuscate
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
        menu.add(0,0,0,"重启全部作用域").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单栏
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case 0:
                String[] commands = {
                        "kill -9 `pgrep systemui`",
                        "am force-stop com.android.packageinstaller",
                        "am force-stop com.heytap.themestore",
                        "am force-stop com.oplus.safecenter",
                        "am force-stop com.oplus.games",
                        "am force-stop com.android.launcher",
                        //"am force-stop com.android.settings",
                        "am force-stop com.coloros.alarmclock",
                        "am force-stop com.east2d.everyimage",
                };
                new MaterialAlertDialogBuilder(this)
                        .setMessage("确定要重启除系统框架外的全部作用域?")
                        .setPositiveButton("确定", (dialog, which) -> ShellUtils.execCommand(commands,true))
                        .setNeutralButton("取消",null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}