package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.MagiskActivity;
import com.luckyzyx.tools.ui.MainActivity;
import com.luckyzyx.tools.ui.AboutActivity;
import com.luckyzyx.tools.ui.XposedActivity;
import com.luckyzyx.tools.utils.HttpUtils;
import com.luckyzyx.tools.utils.ShellUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.topAppBar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Xposed
        MaterialTextView xposed_info = requireActivity().findViewById(R.id.xposed_info);
        xposed_info.setText("版本: " + BuildConfig.VERSION_NAME + "\n版本号: " + BuildConfig.VERSION_CODE/*+"\nXposed: "+XposedCheck*/);

        //Button
        MaterialButton xposed = requireActivity().findViewById(R.id.xposed_btn);
        xposed.setOnClickListener(v -> startActivity(new Intent(requireActivity(), XposedActivity.class)));
        MaterialButton magisk = requireActivity().findViewById(R.id.magisk_btn);
        magisk.setOnClickListener(v -> startActivity(new Intent(requireActivity(), MagiskActivity.class)));
        MaterialButton fps = requireActivity().findViewById(R.id.fps);
        fps.setOnClickListener(v -> MainActivity.setfps(requireActivity()));
        MaterialButton checkupdate = requireActivity().findViewById(R.id.checkupdate);
        checkupdate.setOnClickListener(this::CheckUpdate);

        //BottomSheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheetInternal != null;
        BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(400);
        MaterialCardView updatelog_card = requireActivity().findViewById(R.id.updatelog_card);
        updatelog_card.setOnClickListener(v -> bottomSheetDialog.show());

        MaterialTextView updatelog_text = bottomSheetDialog.findViewById(R.id.log_text);
        assert updatelog_text != null;
        updatelog_text.setText("在写了在写了");

        TextView systeminfo = requireActivity().findViewById(R.id.systeminfo);
        systeminfo.setText(getSystemInfo());
    }

    private String getSystemInfo() {
        String[] str = {
                "厂商: " + Build.BRAND +
                        "\n型号: " + Build.MODEL +
                        "\nAndroid版本: " + Build.VERSION.RELEASE +
                        "\n版本号: " + Build.DISPLAY+
                        "\nSDK API: " + Build.VERSION.SDK +
                        "\n设备参数: " + Build.DEVICE +
                        "\n闪存厂商: "+ ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry",true,true).successMsg
        };
        return str[0];
    }

    //获取更新json
    public void CheckUpdate(View view){
        String url = "https://github.do/https://raw.githubusercontent.com/luckyzyx/luckyzyxtools/main/luckyzyx/release/output-metadata.json";
        HttpUtils.sendRequestWithOkhttp(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Snackbar.make(view,"检查更新失败!",Snackbar.LENGTH_SHORT).show();
                TextView aa = requireActivity().findViewById(R.id.log);
                aa.setText(e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData= Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String variantName=jsonObject.getString("variantName");
                    TextView aa = requireActivity().findViewById(R.id.log);
                    aa.setText(variantName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //解析json
    public void parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        List<String> List = gson.fromJson(jsonData, new TypeToken<List<String>>() {}.getType());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.refresh:
                MainActivity.refreshmode(requireActivity());
                break;
            case R.id.about:
                startActivity(new Intent(requireActivity(), AboutActivity.class));
                break;
        }
        return false;
    }

}