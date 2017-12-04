package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    String[] timeSplit = new String [2];
    String[] listFromResource ;
    String[] listTime ;
    String[] listDate ;
    String[] listFromMeal;
    String[] listFromNum;
    //目標JsonArr 並要傳到下一頁(當天的餐點)
    JsonArray Jarr;
    String[] codeStatus =  {"訂單確認中,未銷帳","已成單", "已領取", "流單"};
    String[] listStatus;
    JsonObject resource;

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
        int Order = bundle.getInt("position");
        //轉換String to JsonArray
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        //找到目標訂單
        JsonArray rArr = parser.parse(jsonArray).getAsJsonArray();
        resource = rArr.get(Order).getAsJsonObject();
        //設置listview 的內容
        setData();
        setlist();

        // 添加點擊事件
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //跳頁設定
                Intent intent = new Intent(purchase_record_order.this, purchase_record_order_detail.class);
                //傳給下一頁的資料設定
                Bundle bundle = new Bundle();
                JsonObject Jobj = Jarr.get(arg2).getAsJsonObject();
                bundle.putString("jsonObj", Jobj.toString());
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
            item.put("date", listDate[i]);
            item.put("store_name",listFromResource[i]);
            //item.put("meal_pic", R.drawable.store0_1);
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
    /*把接回來的DATA放入array中 方便後面使用*/
    public void setData(){
        Jarr = resource.getAsJsonObject().get("details").getAsJsonArray();
        listDate = new String[Jarr.size()];
        listTime= new String[Jarr.size()];
        listFromResource = new String[Jarr.size()];
        listFromMeal = new String[Jarr.size()];
        listFromNum= new String[Jarr.size()];
        listStatus= new String[Jarr.size()];
        for(int i=0;i<Jarr.size();i++){
            JsonObject tmp;
            tmp = Jarr.get(i).getAsJsonObject();
            //設定日期
            setTime(resource.get("order_time").getAsString());
            listDate[i] = timeSplit[0];
            listTime[i] = timeSplit[1];
            listFromMeal[i] = tmp.get("meal").getAsJsonObject().get("meal_name").getAsString();
            listFromResource[i]=  tmp.get("meal").getAsJsonObject().get("shop_name").getAsString();
            listFromNum[i]= tmp.get("amount").getAsString();
            //配合陣列位置getAsInt()-1
            int tmpcode = tmp.get("state").getAsInt()-1;
            listStatus[i] = codeStatus[tmpcode];
        }
    }
    /*將時間切割成天 跟  時*/
    public void setTime(String time){
        System.out.println(time);
        //欲切割的字串
        String splitString = time;
        //使用" "(空白)進行切割
        String[] cmds = splitString.split(" ");
        int i=0;
        for(String cmd:cmds){
            timeSplit[i] = cmd;
            i++;
        }
    }
}
