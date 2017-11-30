package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class purchase_record_order extends AppCompatActivity {

    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView recordListView;
    String[] listFromResource ;
    String[] listDate ;
    String[] listFromMeal;
    String[] listFromNum;
    String[] codeStatus =  {"訂單確認中,未銷帳","已成單", "已領取", "流單"};
    String[] listStatus;
    JsonArray resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_purchase_record_order);

        recordListView = (ListView) findViewById(R.id.purchase_record_order);
        mList =  new ArrayList<>();

        /**
         *  接收 Json Array
         */
        Bundle bundle = getIntent().getExtras();
        String jsonArray = bundle.getString("jsonArray");
        try {
            JSONArray array = new JSONArray(jsonArray);
            System.out.println(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        接收hashMap
//        HashMap<String, Object> hashMap = null;
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            hashMap = (HashMap<String, Object>) bundle.getSerializable("HashMap");
//            String date = (String) hashMap.get("date");
//            String store = (String) hashMap.get("store_name");
//            Integer mealPic = (Integer) hashMap.get("meal_pic");
//            String meal = (String) hashMap.get("meal_name");
//            String counts = (String) hashMap.get("meal_num");
//            String status = (String) hashMap.get("status");
//
//        }
        setlist();

        // 添加點擊事件
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获得选中项的HashMap对象
                HashMap<String,Object> map = (HashMap<String,Object>) recordListView.getItemAtPosition(arg2);
                Object orderDate = map.get("date");

                /**
                 *  傳遞 HashMap 物件:
                 *  由於Bundle是可以帶Serializable的，而HashMap實作了Serializable
                 */
                Intent intent = new Intent(purchase_record_order.this, purchase_record_order_detail.class);
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

    public void setlist(){

        // json Array 資料

        /**
         * 目前還沒有把志願序的資料放進map
         */
        Map<String, Object> item ;
        for (int i = 0; i < listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("date", listDate[i]);
            item.put("store_name",listFromResource[i]);
            item.put("meal_pic", R.drawable.store0_1);
            item.put("meal_name",listFromMeal[i]);
            item.put("meal_num", listFromNum[i]);
            item.put("status", listStatus[i]);
            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_purchase_record_detail,
                new String[] {  "date",
                                "meal_name",
                                "status"
                },
                new int[] { R.id.purchase_record_date,
                            R.id.purchase_record_meal_name,
                            R.id.purchase_record_status,
                });

        recordListView.setAdapter(adapter);
    }
}
