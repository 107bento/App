package com.example.aishalien.bento;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class shopping_cart extends ListActivity {
    List<Map<String,Object>> mList;
    //Map<String, Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        mList =  new ArrayList<>();
        String[] listFromResource = getResources().getStringArray(R.array.list_shopping_cart);
        String[] listFromMeal = getResources().getStringArray(R.array.list_shopping_cart_meal);
        String[] listFromNum = getResources().getStringArray(R.array.list_shopping_cart_num);

        Map<String, Object> item ;
        for(int i=0; i<listFromResource.length;i++) {
            item = new HashMap<String, Object>();
            item.put("store_name",listFromResource[i]);
            item.put("store_pic", R.drawable.meal_1);
            item.put("meal_name",listFromMeal[i]);
            item.put("meal_num",listFromNum[i]);
            mList.add(item);
        }
        //執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_pictext,
                new String[]{"store_name", "store_pic","meal_name","meal_num"},
                new int[]{R.id.store_name,R.id.store_pic,R.id.meal_name,R.id.meal_num});

        setListAdapter(adapter);
    }
}
