package com.example.aishalien.bento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class modification_shopping_car extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_shopping_car);
        initimgbtn();
    }
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.confirm_change);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent();
                intento.setClass(modification_shopping_car.this,shopping_cart.class);
                startActivity(intento);
            }
        });
    }
}
