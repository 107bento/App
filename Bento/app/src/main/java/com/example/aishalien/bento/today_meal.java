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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
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

/**
 * Created by yuting on 2017/11/8.
 */

public class today_meal extends AppCompatActivity {
    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView todayMealList;
    JsonArray resource;
    String[] listFromResource;
    String[] listFromMeal ;
    String[] listFromNum;
    String[] listStatus;
    String[] codeStatus =  {"訂單確認中，未銷帳", "已成單，待領取", "已領取", "流單"};

    int[] listPic;
    static String cookie;

    public interface Api{
        @GET("user/orders/today")
        Call<JsonArray> getinfo(@Header("Cookie") String userCookie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_meal);
        todayMealList = (ListView)findViewById(R.id.today_meal_list);
        mList = new ArrayList<>();
        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();
        getJson();
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
                System.out.println(resource);
                listFromResource= new String[resource.size()];
                listFromMeal = new String[resource.size()];
                listFromNum= new String[resource.size()];
                listStatus= new String[resource.size()];
                listPic = new int [resource.size()];
                for(int i=0;i<resource.size();i++){
                    System.out.println(resource.get(i).getAsJsonObject());
                    JsonArray Jarr = resource.get(i).getAsJsonObject().get("details").getAsJsonArray();
                    for(int j=0;j<Jarr.size();j++){
                        JsonObject tmp = new JsonObject();
                        tmp = Jarr.get(j).getAsJsonObject();
                        listPic[i] = setpic(tmp);
                        listFromResource[i] = tmp.get("meal").getAsJsonObject().get("shop_name").getAsString();
                        listFromMeal [i] = tmp.get("meal").getAsJsonObject().get("meal_name").getAsString();
                        listFromNum[i] = tmp.get("amount").getAsString();
                        //配合陣列位置getAsInt()-1
                        int tmpcode = tmp.get("state").getAsInt()-1;
                        listStatus[i] = codeStatus[tmpcode];
                    }
                }
                setlist();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("error");
            }
        });
    }
    public void setlist(){
        Map<String, Object> item;
        for (int i = 0; i< listFromResource.length; i++) {
            item = new HashMap<String, Object>();
            item.put("meal_pic", listPic[i]);
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
    }
    /*處理圖片*/
    private int setpic(JsonObject tmp){
        String shop_id = tmp.get("meal").getAsJsonObject().get("shop_id").getAsString();
        String meal_id = tmp.get("meal").getAsJsonObject().get("meal_id").getAsString();
        String picName = "store"+String.valueOf(Integer.valueOf(shop_id)-1)+"_"+meal_id;
        int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
        return picId;
    }
}
