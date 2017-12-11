package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class purchase_record extends AppCompatActivity {

    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView recordListView;
    String[] timeSplit = new String [2];
    String[] listDate ;
    String[] listTime ;
    String[] listFromOrder;
    JsonArray resource;
    static String cookie;

    public interface Api{
        @GET("user/orders/all")
        Call<JsonArray> getinfo(@Header("Cookie") String userCookie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_record);
        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();
        recordListView = (ListView) findViewById(R.id.purchase_record_listview);
        mList =  new ArrayList<>();
        try {
            getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 添加點擊事件
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获得选中项的HashMap对象
                HashMap<String,Object> map = (HashMap<String,Object>) recordListView.getItemAtPosition(arg2);
                // 取得被點擊的 order項目底下的 detail
                Intent intent = new Intent(purchase_record.this, purchase_record_order.class);
                Bundle bundle = new Bundle();
                bundle.putString("jsonArray", resource.toString());
                bundle.putInt("position",arg2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    public void getJson() throws IOException {
        /*創建一個retrofit*/

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
                if(response.code()==200){
                    resource = response.body().getAsJsonArray();
                    listDate = new String[resource.size()];
                    listTime= new String[resource.size()];
                    listFromOrder = new String[resource.size()];
                    for(int i=0;i<resource.size();i++){
                        JsonObject tmp = new JsonObject();
                        tmp = resource.get(i).getAsJsonObject();
                        //設定日期
                        setTime(tmp.get("order_time").getAsString());
                        listDate[i] = timeSplit[0];
                        listTime[i] = timeSplit[1];
                        listFromOrder[i]= tmp.get("order_id").getAsString();
                    }
                    setlist();
                    // 購買紀錄為空
                    if (resource.size() == 0) {
                        // 提示訊息
                        new AlertDialog.Builder(purchase_record.this)
                                // 標題
                                .setTitle("提示")
                                // 訊息
                                .setMessage("你還沒有購買過商品喔！")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }else{
                    Toast.makeText(purchase_record.this, "系統錯誤", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(purchase_record.this, "系統錯誤", Toast.LENGTH_SHORT).show();
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
    /*要傳入下一頁的資料*/
    public void setlist(){
        Map<String, Object> item ;
        for (int i = 0; i < listFromOrder.length; i++) {
            item = new HashMap<String, Object>();
            item.put("date", listDate[i]);
            item.put("time", listTime[i]);
            item.put("order_id","單號  "+listFromOrder[i]);
            //放入要擺入Listview的容器中
            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_purchase_record_list,
                new String[] {  "date",
                                "time",
                                "order_id"
                },
                new int[] { R.id.purchase_record_list_date,
                            R.id.purchase_record_list_time,
                            R.id.purchase_record_list_ID
                });

        recordListView.setAdapter(adapter);
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