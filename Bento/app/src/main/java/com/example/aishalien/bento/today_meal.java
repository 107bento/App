package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuting on 2017/11/8.
 */

public class today_meal extends AppCompatActivity {
    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView todayMealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_meal);

        todayMealList = (ListView)findViewById(R.id.today_meal_list);
        mList = new ArrayList<>();

        String[] listFromResource = getResources().getStringArray(R.array.list_shopping_cart);
        String[] listFromMeal = getResources().getStringArray(R.array.list_shopping_cart_meal);
        String[] listFromNum = getResources().getStringArray(R.array.list_shopping_cart_num);
        String[] listStatus =  {"待領取", "訂單確認中", "已領取", "流單"};

        Map<String, Object> item;
        for (int i = 0; i< listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("meal_pic", R.drawable.store0_1);
            item.put("store_name", listFromResource[i]);
            item.put("meal_name", listFromMeal[i]);
            item.put("meal_num", "x" + listFromNum[i]);
            item.put("status", listStatus[i]);
            mList.add(item);
        }

        //執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_today_meal,
                new String[] {  "meal_pic",
                                "store_name",
                                "meal_name",
                                "meal_num",
                                "status" },
                new int[] { R.id.today_list_img,
                            R.id.today_list_store_name,
                            R.id.today_list_meal_name,
                            R.id.today_list_count,
                            R.id.toady_list_status });

        todayMealList.setAdapter(adapter);

        // 添加點擊事件
        todayMealList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获得选中项的HashMap对象
                HashMap<String,Object> map = (HashMap<String,Object>) todayMealList.getItemAtPosition(arg2);

                /**
                 * 測試有沒有拿到資料
                 */
                Object meal_name = map.get("meal_name");
//                Toast.makeText(getApplicationContext(), "你選擇的是" + meal_name, Toast.LENGTH_SHORT).show();

                /*
                *  傳遞 HashMap 物件:
                *  由於Bundle是可以帶Serializable的，而HashMap實作了Serializable
                */
                Intent intent=new Intent(today_meal.this, today_meal_detail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HashMap", map);
                intent.putExtras(bundle);
                startActivity(intent);

                /*
                *  使用一般 Map 物件的傳遞方法：
                *
                final SerializableMap myMap = new SerializableMap();
                myMap.setMap(map); //将map数据添加到封装的myMap<span></span>中
                Bundle bundle=new Bundle();
                bundle.putSerializable("map", myMap);
                intent.putExtras(bundle);
                */
            }
        });
    }

    /**
     * 序列化map供Bundle传递map使用
     * Created  on 13-12-9.
     */
    public class SerializableMap implements Serializable {

        private Map<String,Object> map;

        public Map<String, Object> getMap() {
            return map;
        }
        public void setMap(Map<String, Object> map) {
            this.map = map;
        }
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
