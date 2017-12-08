package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;

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
    CheckBox ignoreBox;
    int ignoreIndex=0;
    String mMeal;
    JsonArray resource;
    JsonObject tmp;
    String[] store;//志願序用設定店家adapter
    String[] storeID;//志願序用設定店家ID
    String store_name;//哪個店家的餐點
    String store_name_id;//
    String[] meal_id1;//設定餐點ID
    String[] meal_id2;//設定餐點ID
    String[] meal_id3;//設定餐點ID
    //下拉選項元件宣告(店家)
    Spinner spinnerS1;
    Spinner spinnerS2;
    Spinner spinnerS3;
    Spinner spinnerM1;
    Spinner spinnerM2;
    Spinner spinnerM3;
    //放入購物車的元素
    int random_pick = 0;
    int amount;
    int meal_value;
    String meal_id;
    String swish_id[] = {"1","1","1"};
    String wish_id[] = {"1","1","1"};
    public interface Api{
        @GET("shops/all")
        Call<JsonArray> getinfo();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_fillment);
        initspinner();
        initimgbtn();
        // 接收 meal_purchase 過來的資料
        Bundle bundle = getIntent().getExtras();
        mMeal = bundle.getString("meal");
        store_name= bundle.getString("store");
        store_name_id= bundle.getString("store_name_id");
        meal_id = bundle.getString("meal_id");
        meal_value = bundle.getInt("value");
        amount = bundle.getInt("amount");

        int count = bundle.getInt("amount");

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
        // 不顯示返回符號，在onOptionsItemSelected須設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //監聽是否要電腦隨機選擇
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    random_pick = 1;
                }
                else{
                    random_pick=0;
                }
            }
        });
        //監聽是否要跳過志願序
        ignoreBox = (CheckBox) findViewById(R.id.ignore);
        ignoreBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ignoreIndex=1;
                }
                else{
                    ignoreIndex=0;
                }
            }
        });
    }

    // 完成按鈕
    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.finish_fillment);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ignoreIndex==1){
                    if(random_pick == 1){
                        new AlertDialog.Builder(application_fillment.this)
                                .setTitle("請選擇")
                                .setMessage("你給我選一個ヽ(#ﾟДﾟ)ﾉ┌┛)`Дﾟ)･;")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }else{
                        // 抓取頁面資料，保存到本地端（購物車資料）
                        GlobalVariable User = (GlobalVariable)getApplicationContext();
                        User.addCart(meal_id,amount,amount*meal_value,"0","0","0",random_pick);
                        User.addInfo(store_name,store_name_id,amount,mMeal,"0","0","0",meal_value);
                        System.out.println(mMeal+"志願序"+"0"+" "+"0"+" "+"0"+" ");
                        // 提示訊息
                        Toast toast = Toast.makeText(application_fillment.this,
                                "已加入購物車", Toast.LENGTH_LONG);
                        //System.out.println("User : "+User.details);
                        toast.show();
                        onBackPressed();
                    }
                }else{
                    // 抓取頁面資料，保存到本地端（購物車資料）
                    GlobalVariable User = (GlobalVariable)getApplicationContext();
                    User.addCart(meal_id,amount,amount*meal_value,wish_id[0],wish_id[1],wish_id[2],random_pick);
                    User.addInfo(store_name,store_name_id,amount,mMeal,swish_id[0],swish_id[1],swish_id[2],meal_value);
                    System.out.println(mMeal+"志願序"+swish_id[0]+" "+swish_id[1]+" "+swish_id[2]+" ");
                    // 提示訊息
                    Toast toast = Toast.makeText(application_fillment.this,
                            "已加入購物車", Toast.LENGTH_LONG);
                    //System.out.println("User : "+User.details);
                    toast.show();
                    onBackPressed();
                }
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
        final ArrayAdapter<String> shopList = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS1.setAdapter(shopList);
        //監聽事件
        spinnerS1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                swish_id[0] = storeID[position];
                setmealOption(R.id.store_spinner01,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerS2 = (Spinner)findViewById(R.id.store_spinner02);
        final ArrayAdapter<String> shopList2 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS2.setAdapter(shopList2);
        //監聽事件
        spinnerS2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                swish_id[1] = storeID[position];
                setmealOption(R.id.store_spinner02,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerS3 = (Spinner)findViewById(R.id.store_spinner03);
        final ArrayAdapter<String> shopList3 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS3.setAdapter(shopList3);
        //監聽事件
        spinnerS3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                swish_id[2] =storeID[position];
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
            String[] meal= new String[tmpmeal1.size()];
            meal_id1 = new String[tmpmeal1.size()];
            for(int i=0;i<tmpmeal1.size();i++){
                meal[i] =tmpmeal1.get(i).getAsJsonObject().get("meal_name").getAsString();
                meal_id1[i] = tmpmeal1.get(i).getAsJsonObject().get("meal_id").getAsString();
            }
            ArrayAdapter<String> mealList1 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM1.setAdapter(mealList1);
            //監聽事件
            spinnerM1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wish_id[0] = meal_id1[position];
                    setmealOption(R.id.meal_spinner01,position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if(rid==R.id.store_spinner02||rid == -1){
            //第二家店的第一個餐點志願序
            spinnerM2 = (Spinner)findViewById(R.id.meal_spinner02);
            JsonArray tmpmeal2 = resource.get(storeID).getAsJsonObject().get("meals").getAsJsonArray();
            String[] meal= new String[tmpmeal2.size()];
            meal_id2 = new String[tmpmeal2.size()];
            for(int i=0;i<tmpmeal2.size();i++){
                meal[i] =tmpmeal2.get(i).getAsJsonObject().get("meal_name").getAsString();
                meal_id2[i] = tmpmeal2.get(i).getAsJsonObject().get("meal_id").getAsString();
            }
            ArrayAdapter<String> mealList2 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM2.setAdapter(mealList2);
            //監聽事件
            spinnerM2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wish_id[1] = meal_id2[position];
                    setmealOption(R.id.meal_spinner02,position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if(rid==R.id.store_spinner03||rid == -1){
            //第三家店的第一個餐點志願序
            spinnerM3 = (Spinner)findViewById(R.id.meal_spinner03);
            JsonArray tmpmeal3 = resource.get(storeID).getAsJsonObject().get("meals").getAsJsonArray();
            String[] meal= new String[tmpmeal3.size()];
            meal_id3 = new String[tmpmeal3.size()];
            for(int i=0;i<tmpmeal3.size();i++){
                meal[i] =tmpmeal3.get(i).getAsJsonObject().get("meal_name").getAsString();
                meal_id3[i] = tmpmeal3.get(i).getAsJsonObject().get("meal_id").getAsString();
            }
            ArrayAdapter<String> mealList3 = new ArrayAdapter<>(application_fillment.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM3.setAdapter(mealList3);
            //監聽事件
            spinnerM3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wish_id[2] = meal_id3[position];
                    Toast.makeText(application_fillment.this, "你選的一" +  wish_id[0]+"二."+ wish_id[1]+"三."+ wish_id[2], Toast.LENGTH_SHORT).show();
                    setmealOption(R.id.meal_spinner03,position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
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
                if (response.code()==200) {
                    resource = response.body().getAsJsonArray();
                    store = new String[resource.size()];
                    storeID = new String[resource.size()];
                    for(int i=0;i<resource.size();i++){
                        tmp = new JsonObject();
                        tmp = resource.get(i).getAsJsonObject();
                        store[i] =tmp.get("shop_name").getAsString();
                        storeID[i] =tmp.get("shop_id").getAsString();
                    }
                    //設定店家選項
                    setshopOption();
                    //哪一家店的菜單 初始必為第一家店，-1表示初始狀態
                    setmealOption(-1,0);
                }else{
                    Toast.makeText(application_fillment.this, "系統錯誤", Toast.LENGTH_SHORT).show();
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
        switch (id)
        {
            // 按了 Action Bar 的返回鍵
            case android.R.id.home:
                onBackPressed();
                return true;

            // 按下購物車
            case R.id.menuItem_shoppingCart:
                Intent intento = new Intent();
                intento.setClass(application_fillment.this, shopping_cart.class);
                startActivity(intento);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}