package com.luckyzyx.tools.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ModuleListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleListFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListview();
    }

    public void initListview(){
        String [] moduleName = {"1111","2222","3333","4444","5555","6666","7777"};
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

        ModuleListAdapter moduleListAdapter = new ModuleListAdapter(requireActivity(),list);
        listView.setAdapter(moduleListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module_list, container, false);
    }
}