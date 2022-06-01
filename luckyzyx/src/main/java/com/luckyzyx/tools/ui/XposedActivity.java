package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.XposedAndroid;
import com.luckyzyx.tools.ui.fragment.XposedUserApp;
import com.luckyzyx.tools.ui.fragment.XposedSystemOther;
import com.luckyzyx.tools.ui.fragment.XposedSystemUI;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.ArrayList;
import java.util.List;
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

    //Tabs + ViewPage2
    private void initFragments() {
        String[] titles = {"系统框架", "系统界面","系统其他","三方APP"};
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new XposedAndroid());
        fragmentList.add(new XposedSystemUI());
        fragmentList.add(new XposedSystemOther());
        fragmentList.add(new XposedUserApp());
        TabLayout tabs = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_page2);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        new TabLayoutMediator(tabs, viewPager2, true, true, (tab, position) ->
                tab.setText(titles[position])
        ).attach();
    }

    //创建Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"重启系统界面");
        menu.add(0,2,1,"重启时钟");
        menu.add(0,3,1,"重启设置");
        menu.add(0,4,1,"重启系统桌面");
        menu.add(0,5,1,"重启主题商店");
        menu.add(0,6,1,"停止应用包安装器");
        menu.add(0,9,1,"停止好多动漫");
        return true;
    }

    //标题栏返回事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.openOptionsMenu();
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case 1:
                ShellUtils.execCommand("killall com.android.systemui",true);
                break;
            case 6:
                ShellUtils.execCommand("killall com.android.packageinstaller",true);
                break;
            case 5:
                ShellUtils.execCommand("killall com.heytap.themestore",true);
                break;
            case 4:
                ShellUtils.execCommand("killall com.android.launcher",true);
                break;
            case 3:
                ShellUtils.execCommand("killall com.android.settings",true);
                break;
            case 2:
                ShellUtils.execCommand("killall com.coloros.alarmclock",true);
                break;
            case 9:
                ShellUtils.execCommand("killall com.east2d.everyimage",true);
                break;
            default:
                Toast.makeText(this, "错误->"+item.getItemId()+":"+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}