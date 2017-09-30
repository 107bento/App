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
    List<Map<String, String>> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        mList =  new ArrayList<>();
        String[] listFromResource = getResources().getStringArray(R.array.list_shopping_cart);

        for(int i=0; i<listFromResource.length;i++) {
            Map<String, Objects> item = new HashMap<String, Objects>();
            item.put("imgView", android.R.drawable.ic_menu_my_calendar);
            item.put("textView",listFromResource[i]);
            mList.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,mList,R.layout.list_item_pictext,new String[]{"imgView",
                "txtView","txtView","txtView"},new int[]{R.id.imgView,R.id.stroe_name,R.id.meal_name,R.id.num_beto});
        setListAdapter(adapter);
    }
    public void init_arraylist(){

    }
}
