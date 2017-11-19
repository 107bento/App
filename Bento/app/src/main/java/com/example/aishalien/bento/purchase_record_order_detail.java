package com.example.aishalien.bento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class purchase_record_order_detail extends AppCompatActivity {

    Integer mealPic;
    String date, store, meal, counts, status;
    TextView storeText, mealText, countsText, statusText;
    ImageView mealImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_purchase_record_order_detail);

        /*
        *  接收 HashMap 物件
        */
        HashMap<String, Object> hashMap = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hashMap = (HashMap<String, Object>) bundle.getSerializable("HashMap");
            date = (String) hashMap.get("date");
            store = (String) hashMap.get("store_name");
            mealPic = (Integer) hashMap.get("meal_pic");
            meal = (String) hashMap.get("meal_name");
            counts = (String) hashMap.get("meal_num");
            status = (String) hashMap.get("status");

        }

        mealImg = (ImageView) findViewById(R.id.purchase_record_detail_img);
        storeText = (TextView) findViewById(R.id.purchase_record_detail_store);
        mealText = (TextView) findViewById(R.id.purchase_record_detail_meal);
        countsText = (TextView) findViewById(R.id.purchase_record_detail_count);
        statusText = (TextView) findViewById(R.id.purchase_record_detail_status);


        // 設定頁面資料
        mealImg.setImageResource(mealPic);
        storeText.setText(store);
        mealText.setText(meal);
        countsText.setText("x" + counts);
        statusText.setText(status);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch(id)
        {
            case android.R.id.home: // 按了 Action Bar 的返回鍵
                onBackPressed();
                return true;    // 注意! 一定要回傳 true
        }
        return super.onOptionsItemSelected(item);
    }
}
