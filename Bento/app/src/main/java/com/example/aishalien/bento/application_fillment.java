package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class application_fillment extends AppCompatActivity {

    private Toolbar mtoolbar;
    String mMeal;
    JsonArray resource;
    JsonObject tmp;
    final String[] lunch = {"雞腿飯", "魯肉飯", "排骨飯", "水餃", "陽春麵"};
    String[] store;//設定店家
    String[] meal;//設定店家 meal
    //下拉選項元件宣告(店家)
    Spinner spinnerS1;
    Spinner spinnerS2;
    Spinner spinnerS3;
    Spinner spinnerM1;
    Spinner spinnerM2;
    Spinner spinnerM3;
    public interface Api{
        @GET("shops/all")
        Call<JsonArray> getinfo();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_fillment);
        initspinner();
        initimgbtn();
        // 接收 meal_purchase 過來的資料
        Bundle bundle = getIntent().getExtras();
        mMeal = bundle.getString("meal");
         // toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置標題
        mtoolbar.setTitle(mMeal);
        // 設置標題顏色
        mtoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);
        // 設置返回按鍵作用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 完成按鈕
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.finish_fillment);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 建立一個Bundle
//                Bundle bundle = new Bundle();
//                bundle.putString("meal",mMeal);
//                Intent intento = new Intent();
//                intento.setClass(application_fillment.this, shopping_cart.class);
//                // 將bundle傳入
//                intento.putExtras(bundle);
//                startActivity(intento);

                // 抓取頁面資料，保存到本地端（購物車資料）
                // 提示訊息
                Toast toast = Toast.makeText(application_fillment.this,
                        "已加入購物車", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    public void initspinner(){
        try {
            getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setshopOption(){
        spinnerS1 = (Spinner)findViewById(R.id.store_spinner01);
        ArrayAdapter<String> shopList = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS1.setAdapter(shopList);
        //監聽事件
        spinnerS1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(application_fillment.this, "你選的是" + store[position], Toast.LENGTH_SHORT).show();
                setmealOption(R.id.store_spinner01,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerS2 = (Spinner)findViewById(R.id.store_spinner02);
        ArrayAdapter<String> shopList2 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS2.setAdapter(shopList2);
        //監聽事件
        spinnerS2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(application_fillment.this, "你選的是" + store[position], Toast.LENGTH_SHORT).show();
                setmealOption(R.id.store_spinner02,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerS3 = (Spinner)findViewById(R.id.store_spinner03);
        ArrayAdapter<String> shopList3 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS3.setAdapter(shopList3);
        //監聽事件
        spinnerS3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(application_fillment.this, "你選的是" + store[position], Toast.LENGTH_SHORT).show();
                setmealOption(R.id.store_spinner03,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //哪一個志願(rid)的哪家店菜單storeID,是否為初始(0=>不是,1=>是)
    public void setmealOption(int rid,int storeID){
        if(rid==R.id.store_spinner01||rid == -1){
            //第一家店的餐點志願序
            spinnerM1 = (Spinner)findViewById(R.id.meal_spinner01);
            JsonArray tmpmeal1 = resource.get(storeID).getAsJsonObject().get("meals").getAsJsonArray();
            meal= new String[tmpmeal1.size()];
            for(int i=0;i<tmpmeal1.size();i++){
                meal[i] =tmpmeal1.get(i).getAsJsonObject().get("meal_name").getAsString();
            }
            ArrayAdapter<String> mealList1 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM1.setAdapter(mealList1);
        }else if(rid==R.id.store_spinner02||rid == -1){
            //第二家店的第一個餐點志願序
                    spinnerM2 = (Spinner)findViewById(R.id.meal_spinner02);
            JsonArray tmpmeal2 = resource.get(storeID).getAsJsonObject().get("meals").getAsJsonArray();
            meal= new String[tmpmeal2.size()];
            for(int i=0;i<tmpmeal2.size();i++){
                meal[i] =tmpmeal2.get(i).getAsJsonObject().get("meal_name").getAsString();
            }
            ArrayAdapter<String> mealList2 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM2.setAdapter(mealList2);
        }else if(rid==R.id.store_spinner03||rid == -1){
            //第三家店的第一個餐點志願序
            spinnerM3 = (Spinner)findViewById(R.id.meal_spinner03);
            JsonArray tmpmeal3 = resource.get(storeID).getAsJsonObject().get("meals").getAsJsonArray();
            meal= new String[tmpmeal3.size()];
            for(int i=0;i<tmpmeal3.size();i++){
                meal[i] =tmpmeal3.get(i).getAsJsonObject().get("meal_name").getAsString();
            }
            ArrayAdapter<String> mealList3 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM3.setAdapter(mealList3);
        }
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
        Call<JsonArray> Model = api.getinfo();
        Model.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                resource = response.body().getAsJsonArray();
                store = new String[resource.size()];
                for(int i=0;i<resource.size();i++){
                    tmp = new JsonObject();
                    tmp = resource.get(i).getAsJsonObject();
                    store[i] =tmp.get("shop_name").getAsString();
                }
                //設定店家選項
                setshopOption();
                //哪一家店的菜單 初始必為第一家店，-1表示初始狀態
                setmealOption(-1,0);
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("error");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            // 按了 Action Bar 的返回鍵
            case android.R.id.home:
                onBackPressed();
                return true;

            // 按下購物車
            case R.id.goto_shop_cart:
                Intent intento = new Intent();
                intento.setClass(application_fillment.this, shopping_cart.class);
                startActivity(intento);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_normal, menu);
        return true;
    }
}