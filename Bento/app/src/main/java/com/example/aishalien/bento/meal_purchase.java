package com.example.aishalien.bento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class meal_purchase extends AppCompatActivity {
    TextView value,num;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_purchase);
        value= (TextView) findViewById(R.id.meal_purchase_value);
        num= (TextView) findViewById(R.id.meal_purchase_num);
        ImageView img= (ImageView) findViewById(R.id.meal_purchase_img);
        Bundle bundle = getIntent().getExtras();
        value.setText("單價    NT."+bundle.getString("value")+" 元");
        img.setImageResource(bundle.getInt("pic"));
        initimgbtn();
    }
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.btn_put_in_cart);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent();
                intento.setClass(meal_purchase.this,application_fillment.class);
                startActivity(intento);
            }
        });
    }
}