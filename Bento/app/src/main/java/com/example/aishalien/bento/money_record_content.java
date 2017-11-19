package com.example.aishalien.bento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class money_record_content extends AppCompatActivity {

    TextView tv_Time, tv_Value, tv_Remain;
    String recordID, time, value, remain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_money_record_content);

        /**
         *  接收 HashMap 物件
         */
        HashMap<String, Object> hashMap = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hashMap = (HashMap<String, Object>) bundle.getSerializable("HashMap");
            recordID = (String) hashMap.get("record_id");
            time = (String) hashMap.get("time");
            value = (String) hashMap.get("value");
            remain = (String) hashMap.get("remain");

        }

        // 找到元件
        tv_Time = (TextView) findViewById(R.id.money_record_content_time);
        tv_Value = (TextView) findViewById(R.id.money_record_content_value);
        tv_Remain = (TextView) findViewById(R.id.money_record_content_remain);

        // 設定頁面資料
        tv_Time.setText(time);
        tv_Value.setText(value);
        tv_Remain.setText(remain);
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
