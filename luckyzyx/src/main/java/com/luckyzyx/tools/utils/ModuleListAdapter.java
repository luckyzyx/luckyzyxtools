package com.luckyzyx.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luckyzyx.tools.R;

import java.util.List;
import java.util.Map;

public class ModuleListAdapter extends BaseAdapter {

    private final Context context;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,null,true);

            viewHolder.text_name = convertView.findViewById(R.id.text_name);
            viewHolder.text_version = convertView.findViewById(R.id.text_version);
            viewHolder.text_description = convertView.findViewById(R.id.text_description);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_name.setText(list.get(position).get("moduleName"));
        viewHolder.text_version.setText(list.get(position).get("moduleVersion"));
        viewHolder.text_description.setText(list.get(position).get("moduleDescription"));
        return convertView;
    }
}
