package com.luckyzyx.tools.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.textview.MaterialTextView;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ModuleListAdapter;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleListFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        test();
        initListview();
    }

    //test
    public void test(){
        String[] command = {
                //获取已安装模块列表
                "ls /data/adb/modules/ | while read module ; do\n"+
                "    echo -e $module\n"+
                "done > "+requireActivity().getFilesDir().getPath()+"/modulelist.txt"
        };
        ShellUtils.CommandResult installcommandslog = ShellUtils.execCommand(command,true,true);
        MaterialTextView log = requireActivity().findViewById(R.id.log);
        log.setText(installcommandslog.allMsg);

    }

    //字符串插入字符串组
    private static String[] insert(String[] arr, String str) {
        int size = arr.length;
        String[] tmp = new String[size + 1];
        System.arraycopy(arr, 0, tmp, 0, size);
        tmp[size] = str;
        return tmp;
    }

    //初始化ListView
    public void initListview() {
        String[] command = {
                //获取已安装模块列表
                "ls /data/adb/modules/"
        };
//        ShellUtils.CommandResult connmandlog = ShellUtils.execCommand(command,true,true);
//        List<String> testlist = Collections.singletonList(connmandlog.successMsg);
//        String [] moduleName = (String[]) testlist.toArray();


        String [] moduleName = {"11","22","33","44","55","66","77"};
        String [] moduleVersion = {"11","22","33","44","55","66","77"};
        String [] moduleDescription = {"1","2","3","4","5","6","7"};

        ListView listView = requireActivity().findViewById(R.id.list_view);
        List<Map<String, String>> list = new ArrayList<>();
        for (int i=0;i<moduleName.length;i++)
        {
            Map<String, String> map = new HashMap<>();
            map.put("moduleName",moduleName[i]);
            map.put("moduleVersion",moduleVersion[i]);
            map.put("moduleDescription",moduleDescription[i]);
            list.add(map);
        }
        //设置自定义适配器
        ModuleListAdapter moduleListAdapter = new ModuleListAdapter(requireActivity(),list);
        listView.setAdapter(moduleListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module_list, container, false);
    }
}