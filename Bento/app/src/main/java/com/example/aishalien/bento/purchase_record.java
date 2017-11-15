package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class purchase_record extends AppCompatActivity {

    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView recordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_record);

        recordListView = (ListView) findViewById(R.id.purchase_record_list);
        mList =  new ArrayList<>();

        String[] listDate = {"2017/11/11", "2017/11/9", "2017/10/26", "2017/9/29"};
        String[] listFromResource = getResources().getStringArray(R.array.list_shopping_cart);
        String[] listFromMeal = getResources().getStringArray(R.array.list_shopping_cart_meal);
        String[] listFromNum = getResources().getStringArray(R.array.list_shopping_cart_num);
        String[] listStatus =  {"待領取", "訂單確認中", "已領取", "流單"};

        /**
         * 目前還沒有把志願序的資料放進map
         */
        Map<String, Object> item ;
        for (int i = 0; i < listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("date", listDate[i]);
            item.put("store_name",listFromResource[i]);
            item.put("meal_pic", R.drawable.store2_1);
            item.put("meal_name",listFromMeal[i]);
            item.put("meal_num", listFromNum[i]);
            item.put("status", listStatus[i]);
            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_purchase_record,
                new String[] {  "date",
                                "meal_name",
                                "status"
                },
                new int[] { R.id.purchase_record_date,
                            R.id.purchase_record_meal_name,
                            R.id.purchase_record_status,
                });

        recordListView.setAdapter(adapter);

        // 添加點擊事件
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获得选中项的HashMap对象
                HashMap<String,Object> map = (HashMap<String,Object>) recordListView.getItemAtPosition(arg2);
                Object orderDate = map.get("date");

                /*
                *  傳遞 HashMap 物件:
                *  由於Bundle是可以帶Serializable的，而HashMap實作了Serializable
                */
                Intent intent = new Intent(purchase_record.this, purchase_record_detail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HashMap", map);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
