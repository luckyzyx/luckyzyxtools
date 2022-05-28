package com.luckyzyx.tools.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.MainActivity;
import com.luckyzyx.tools.ui.AboutActivity;
import com.luckyzyx.tools.ui.SettingsActivity;
import com.luckyzyx.tools.utils.HttpUtils;

public class UserFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Toolbar toolbar = view.findViewById(R.id.topAppBar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null){
            activity.setSupportActionBar(toolbar);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView user = requireActivity().findViewById(R.id.user);
        user.setOnClickListener(v -> Snackbar.make(v, "不认识字? 还点?", Snackbar.LENGTH_SHORT).show());

        MaterialCardView checkupdate_card = requireActivity().findViewById(R.id.checkupdate_card);
        checkupdate_card.setOnClickListener(v -> new HttpUtils(requireActivity()).CheckUpdate(true));

        MaterialCardView settings = requireActivity().findViewById(R.id.settings_card);
        settings.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingsActivity.class)));

        MaterialCardView about = requireActivity().findViewById(R.id.about_card);
        about.setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.add(0,1,0,"重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0,2,0,"关于").setIcon(R.drawable.ic_baseline_info_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 1:
                MainActivity.refreshmode(requireActivity());
                break;
            case 2:
                startActivity(new Intent(requireActivity(), AboutActivity.class));
                break;
        }
        return false;
    }
}