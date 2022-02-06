package com.luckyzyx.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textview = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        button.setOnClickListener(v -> textview.setText(istext()));
        button2.setOnClickListener(v -> istext2("传参"));
    }

    public String istext(){
        return "hook return";
    }
    public void istext2(String a){
        TextView textview = findViewById(R.id.textView);
        textview.setText(a);
    }
}