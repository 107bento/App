package com.example.aishalien.bento;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.aishalien.bento.R.id.to_register_button;

/**
 * Created by yuting on 2017/11/3.
 */

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 取消
        Button cancel = (Button)findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 回login畫面
                Intent intent = new Intent();
                intent.setClass(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // 註冊
        Button register = (Button)findViewById(to_register_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // send 註冊資料到server

                // 回login畫面
                Intent intent = new Intent();
                intent.setClass(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

}

