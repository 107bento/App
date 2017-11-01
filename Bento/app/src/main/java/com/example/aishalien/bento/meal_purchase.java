package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class meal_purchase extends AppCompatActivity {
    TextView value,num;
    int counter = 0;
    private Toolbar mtoolbar;
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

//        toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
        mtoolbar.setTitle(R.string.list_menu_name);
        // 設置標題顏色
        mtoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);
        // 設置返回按鍵作用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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