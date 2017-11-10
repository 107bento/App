package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class application_fillment extends AppCompatActivity {

    private Toolbar mtoolbar;
    String mMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_fillment);
        initimgbtn();

        // 接收 meal_purchase 過來的資料
        Bundle bundle = getIntent().getExtras();
        mMeal = bundle.getString("meal");

         // toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置標題
        mtoolbar.setTitle(mMeal);
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

    // 完成按鈕
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.finish_fillment);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {

//                // 建立一個Bundle
//                Bundle bundle = new Bundle();
//                bundle.putString("meal",mMeal);
//                Intent intento = new Intent();
//                intento.setClass(application_fillment.this, shopping_cart.class);
//                // 將bundle傳入
//                intento.putExtras(bundle);
//                startActivity(intento);


                // 抓取頁面資料，保存到本地端（購物車資料）

                // 提示訊息
                Toast toast = Toast.makeText(application_fillment.this,
                        "已加入購物車", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            // 按了 Action Bar 的返回鍵
            case android.R.id.home:
                onBackPressed();
                return true;

            // 按下購物車
            case R.id.goto_shop_cart:
                Intent intento = new Intent();
                intento.setClass(application_fillment.this, shopping_cart.class);
                startActivity(intento);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_normal, menu);
        return true;
    }
}