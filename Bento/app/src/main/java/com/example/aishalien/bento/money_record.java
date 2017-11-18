package com.example.aishalien.bento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class money_record extends AppCompatActivity {

    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView recordListView;
    String[] listFromResource = {"1","2","3","4"};
    String[] listID = {"1","2","3","4"};
    String[] listTime = {"2017/11/16","2017/11/17","2017/11/18","2017/11/19"};
    String[] listValue = {"+100","-50","+200","-60"};
    String[] listRemain = {"100","50","250","190"};
    static String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_record);

        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();
        recordListView = (ListView) findViewById(R.id.money_record_listview);
        mList =  new ArrayList<>();

//        try {
//            getJson();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        setlist();

        // 添加點擊事件
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获得选中项的HashMap对象
                HashMap<String,Object> map = (HashMap<String,Object>) recordListView.getItemAtPosition(arg2);

                /**
                 *  傳遞 HashMap 物件:
                 *  由於Bundle是可以帶Serializable的，而HashMap實作了Serializable
                 */
                Intent intent = new Intent(money_record.this, money_record_content.class);
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

        Map<String, Object> item ;
        for (int i = 0; i < listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("record_id", listID[i]);
            item.put("time",listTime[i]);
            item.put("value", listValue[i]);
            item.put("remain",listRemain[i]);
            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_money_record,
                new String[] {  "time",
                                "value"
                },
                new int[] { R.id.money_record_time,
                            R.id.money_record_value
                });

        recordListView.setAdapter(adapter);
    }
}
