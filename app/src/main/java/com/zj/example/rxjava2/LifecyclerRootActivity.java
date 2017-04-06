package com.zj.example.rxjava2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Title: LifecyclerRootActivity
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/30  14:01
 *
 * @author 郑炯
 * @version 1.0
 */
public class LifecyclerRootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_example);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecyclerRootActivity.this, LifecycleExample1Activity.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecyclerRootActivity.this, LifecycleExample2Activity.class));
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecyclerRootActivity.this, LifecycleExample3Activity.class));
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecyclerRootActivity.this, LifecycleExample4Activity.class));
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecyclerRootActivity.this, LifecycleExample5Activity.class));
            }
        });

    }
}
