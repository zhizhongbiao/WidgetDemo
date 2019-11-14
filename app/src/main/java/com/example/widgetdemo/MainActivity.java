package com.example.widgetdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.widgetdemo.widget.KnobBtn;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.kb)
    KnobBtn kb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
