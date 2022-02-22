package com.luckyzyx.tools.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luckyzyx.tools.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleListAdapter extends BaseAdapter {

    private Context context;
    List<Map<String, String>> list;

    public ModuleListAdapter(Context context,List<Map<String,String>> list) {
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        private TextView text_name;
        private TextView text_version;
        private TextView text_description;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,null,true);

            viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
            viewHolder.text_version = (TextView) convertView.findViewById(R.id.text_version);
            viewHolder.text_description = (TextView) convertView.findViewById(R.id.text_description);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String moduleName = list.get(position).get("moduleName");
        String moduleVersion = list.get(position).get("moduleVersion");
        String moduleDescription = list.get(position).get("moduleDescription");
        viewHolder.text_name.setText(moduleName);
        viewHolder.text_version.setText(moduleVersion);
        viewHolder.text_description.setText(moduleDescription);
        return convertView;
    }
}
