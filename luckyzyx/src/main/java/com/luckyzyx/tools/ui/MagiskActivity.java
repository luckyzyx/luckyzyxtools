package com.luckyzyx.tools.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.fragment.ModuleInstallFragment;
import com.luckyzyx.tools.ui.fragment.ModuleListFragment;
import com.luckyzyx.tools.ui.fragment.ModuleSwitchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MagiskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.activity_magisk);
        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initFragments();
    }

    //Tabs + ViewPage2
    private void initFragments() {
        String[] titles = {"模块安装", "模块选项","模块管理"};
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new ModuleInstallFragment());
        fragmentList.add(new ModuleSwitchFragment());
        fragmentList.add(new ModuleListFragment());
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
        return true;
    }

    //标题栏返回事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.openOptionsMenu();
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }

}