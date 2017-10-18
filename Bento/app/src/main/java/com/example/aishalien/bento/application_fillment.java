package com.example.aishalien.bento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class application_fillment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_fillment);
        initimgbtn();
    }
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.finish_fillment);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent();
                intento.setClass(application_fillment.this,shopping_cart.class);
                startActivity(intento);
            }
        });
    }
}