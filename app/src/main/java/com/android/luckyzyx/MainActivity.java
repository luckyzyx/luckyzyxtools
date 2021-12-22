package com.android.luckyzyx;

import static android.widget.Toast.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(v -> makeText(this, "Toast: 点击按钮", LENGTH_SHORT).show());
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("标题")
                    .setMessage("消息")
                    .setCancelable(true)
                    .setPositiveButton("确定", (dialog, which) -> {
                        Toast.makeText(this,"点击确定",LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消",  (dialog, which) -> {
                        Toast.makeText(this,"点击取消",LENGTH_SHORT).show();
                    })
                    .setNeutralButton("中间",  (dialog, which) -> {
                        Toast.makeText(this,"点击中间",LENGTH_SHORT).show();
                    });
            alert.create().show();
        });


    }
}