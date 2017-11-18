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
    String[] listFromResource ;
    String[] listDate ;
    String[] listTime ;
    String[] listFromMeal;
    String[] listFromNum;
    String[] codeStatus =  {"訂單確認中","已成單", "已領取", "流單"};
    String[] listStatus;
    JsonArray resource;
    JsonArray mJsonArray;//要傳到下一頁的array資料
    static String cookie;

    public interface Api{
        @GET("user/orders")
        Call<JsonArray> getinfo(@Header("Cookie") String userCookie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


                /**
                 *  傳遞 json Array
                 *  把 jsonArray.toString()
                 */
                Intent intent = new Intent(purchase_record.this, purchase_record_order.class);
                Bundle bundle = new Bundle();
                bundle.putString("jsonArray", mJsonArray.toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    public void getJson() throws IOException {

        /*創建一個retrofit*/
        /*OKHTTP*/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
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
                listDate = new String[resource.size()];
                listFromResource = new String[resource.size()];
                listFromMeal = new String[resource.size()];
                listFromNum= new String[resource.size()];
                listStatus= new String[resource.size()];
                for(int i=0;i<resource.size();i++){
                    JsonObject tmp = new JsonObject();
                    tmp = resource.get(i).getAsJsonObject();
                    //設定日期
                    try {
                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                        int year = f.parse(tmp.get("order_time").getAsString()).getYear()+1900;
                        int month = f.parse(tmp.get("order_time").getAsString()).getMonth()+1;
                        int date = f.parse(tmp.get("order_time").getAsString()).getDate();
                        listDate[i] = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listFromMeal[i] = tmp.getAsJsonArray("details").get(0).getAsJsonObject().get("meal").getAsJsonObject().get("meal_name").getAsString();
                    listFromResource[i]= tmp.getAsJsonArray("details").get(0).getAsJsonObject().get("meal").getAsJsonObject().get("shop_name").getAsString();
                    listFromNum[i]= tmp.getAsJsonArray("details").get(0).getAsJsonObject().get("amount").getAsString();
                    int tmpcode = tmp.getAsJsonArray("details").get(0).getAsJsonObject().get("state").getAsInt()-1;
                    listStatus[i] = codeStatus[tmpcode];
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
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

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
        /**
         * 目前還沒有把志願序的資料放進map
         */
        Map<String, Object> item ;
        for (int i = 0; i < listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("date", listDate[i]);
            item.put("time", listTime[i]);

//            it123em.put("store_name",listFromResource[i]);
//            item.put("meal_pic", R.drawable.store2_1);
//            item.put("meal_name",listFromMeal[i]);
//            item.put("meal_num", listFromNum[i]);
//            item.put("status", listStatus[i]);

            mList.add(item);
        }

        // 執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_purchase_record_list,
                new String[] {  "date",
                                "time"
                },
                new int[] { R.id.purchase_record_list_date,
                            R.id.purchase_record_list_time,
                });

        recordListView.setAdapter(adapter);
    }
}