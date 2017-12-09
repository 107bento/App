package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class money_record extends AppCompatActivity {

    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView recordListView;
    JsonArray resource;
    String[] listFromRecordId ;
    String[] listDetail;
    String[] listDate;
    String[] listTime;
    String[] listValue;
    String[] listRemain;
    String[] timeSplit = new String [2];

    static String cookie;

    public interface Api{
        @GET("user/records")
        Call<JsonArray> getinfo(@Header("Cookie") String userCookie);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_record);

        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();
        recordListView = (ListView) findViewById(R.id.money_record_listview);
        mList =  new ArrayList<>();
        getJson();


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
        for (int i = 0; i < listFromRecordId.length; i++) {
            item = new HashMap<String, Object>();
            item.put("record_id", listFromRecordId[i]);
            item.put("date",listDate[i]);
            item.put("time",listTime[i]);
            item.put("remain",listRemain[i]);
            if(listDetail[i].equals("store")){
                item.put("value", "+"+listValue[i]);
            }else{
                item.put("value", "-"+listValue[i]);
            }
            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_money_record,
                new String[] {  "date",
                                "value"
                },
                new int[] { R.id.money_record_time,
                            R.id.money_record_value
                });

        recordListView.setAdapter(adapter);
    }
    /*創建一個retrofit*/
    public void getJson(){

        /*OKHTTP 監視網路*/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        /*OKHTTP 監視網路*/

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://163.22.17.227/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        Api api = retrofit.create(Api.class);
        Call<JsonArray> Model = api.getinfo(cookie);

        Model.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                resource = response.body().getAsJsonArray();
                listFromRecordId= new String[resource.size()];
                listDate = new String[resource.size()];
                listTime= new String[resource.size()];
                listDetail = new String[resource.size()];
                listValue = new String[resource.size()];
                listRemain = new String[resource.size()];
                for(int i=0;i<resource.size();i++){
                    JsonObject tmp = new JsonObject();
                    tmp = resource.get(i).getAsJsonObject();
                    listFromRecordId[i] = tmp.get("record_id").getAsString();
                    //設定日期
                    setTime(tmp.get("time").getAsString());
                    listDate[i] = timeSplit[0];
                    listTime[i] = timeSplit[1];
                    listDetail[i] = tmp.get("record_detail").getAsString();
                    listValue[i] = tmp.get("value").getAsString();
                    listRemain[i] = tmp.get("remain").getAsString();
                }
                setlist();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }
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
