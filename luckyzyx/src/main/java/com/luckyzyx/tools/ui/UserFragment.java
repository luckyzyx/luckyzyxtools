package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.luckyzyx.tools.R;

import java.io.File;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView theme = requireActivity().findViewById(R.id.theme);
        theme.setOnClickListener(v -> {
            final String[] themelist = {"草原绿", "基佬紫"};
            new AlertDialog.Builder(requireActivity())
                    .setTitle("切换主题")
                    .setCancelable(true)
                    .setItems(themelist, (dialog, which) -> {
                        switch (themelist[which]){
                            case "草原绿":
                                new File(requireActivity().getFilesDir().getAbsoluteFile() + "/theme/").delete();
                                new File(requireActivity().getFilesDir().getAbsoluteFile() + "/theme/green/").mkdir();
                                requireActivity().recreate();
                                break;
                            case "基佬紫":
                                new File(requireActivity().getFilesDir().getAbsoluteFile() + "/theme/").delete();
                                new File(requireActivity().getFilesDir().getAbsoluteFile() + "/theme/purple/").mkdir();
                                requireActivity().recreate();
                                break;
                        }
                    })
                    .show();

        });
    }
}