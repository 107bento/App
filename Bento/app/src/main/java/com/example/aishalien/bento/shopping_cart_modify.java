package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class shopping_cart_modify extends AppCompatActivity {
    private Toolbar mtoolbar;
    //設定檔 from application
    String mMeal;
    int counter; //數量
    JsonArray resource;
    JsonObject tmp;
    String[] store;//志願序用設定店家adapter
    String[] storeID;//志願序用設定店家ID
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
    int position;
    String meal_id;
    JsonArray cartvalue;
    JsonArray information;
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
        setContentView(R.layout.sub_shopping_car_modify);
        initimgbtn();
        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cartvalue = User.details;
        information = User.ch_details;
        initspinner();
        // 接收 shopping_cart 過來的資料
        Bundle bundle = getIntent().getExtras();
        mMeal = bundle.getString("meal");

        //設定相關圖片
        ImageView setpic = (ImageView) findViewById(R.id.imageView2);
        setpic.setImageResource(bundle.getInt("store_pic"));
        //設定相關文字
        TextView meal_valueT = (TextView)findViewById(R.id.meal_purchase_value);
        meal_value = bundle.getInt("meal_value");
        meal_valueT.setText(Integer.toString(bundle.getInt("meal_value"))+"元");
        //接相關資料
        meal_id = bundle.getString("meal_id");
        //
        position = bundle.getInt("position");
        amount = bundle.getInt("amount");
        // toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
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
        //監聽是否要電腦隨機選擇
        CheckBox autorandom = (CheckBox)findViewById(R.id.checkBox);
        autorandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            random_pick++;
            random_pick = random_pick%2;
            }
        });
        // 數量控件
        AmountView mAmountView = (AmountView) findViewById(R.id.amountView);
        mAmountView.setMaxValue(99);  // 設置最大數量
        mAmountView.setListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(int value) {
                //  紀錄數量
                counter = value;
            }
        });
    }

    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.confirm_change);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更改jsononbject 回寫到shopping cart
                Intent intento = new Intent();
                intento.setClass(shopping_cart_modify.this,shopping_cart.class);
                startActivity(intento);
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
    public int set_store_init(int position,int spin){
        String tmp_id[] = {"swish_id1","swish_id2","swish_id3"};
        for(int i=0;i<storeID.length;i++){
            if(information.get(position).getAsJsonObject().get(tmp_id[spin]).getAsString().equals(storeID[i])){
                return i;
            }
        }
        return 0;
    }
    public int set_meal_init(int position,String[] meal,int spin){
        String tmp_id[] = {"wish_id_1","wish_id_2","wish_id_3"};
        for(int i=0;i<meal.length;i++){
            if(cartvalue.get(position).getAsJsonObject().get(tmp_id[spin]).getAsString().equals(meal[i])){
                return i;
            }
        }
        return 0;
    }
    public void setshopOption(){
        spinnerS1 = (Spinner)findViewById(R.id.store_spinner01);
        final ArrayAdapter<String> shopList = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS1.setAdapter(shopList);
        spinnerS1.setSelection(set_store_init(position,0));
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
        final ArrayAdapter<String> shopList2 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS2.setAdapter(shopList2);
        spinnerS2.setSelection(set_store_init(position,1));
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
        final ArrayAdapter<String> shopList3 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS3.setAdapter(shopList3);
        spinnerS3.setSelection(set_store_init(position,2));
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
            ArrayAdapter<String> mealList1 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM1.setAdapter(mealList1);
            spinnerM1.setSelection(set_meal_init(position,meal_id1,0));
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
            ArrayAdapter<String> mealList2 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM2.setAdapter(mealList2);
            spinnerM2.setSelection(set_meal_init(position,meal_id2,1));
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
            ArrayAdapter<String> mealList3 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM3.setAdapter(mealList3);
            spinnerM3.setSelection(set_meal_init(position,meal_id3,2));
            //監聽事件
            spinnerM3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wish_id[2] = meal_id3[position];
                    Toast.makeText(shopping_cart_modify.this, "你選的一" +  wish_id[0]+"二."+ wish_id[1]+"三."+ wish_id[2], Toast.LENGTH_SHORT).show();
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
        application_fillment.Api api = retrofit.create(application_fillment.Api.class);
        Call<JsonArray> Model = api.getinfo();
        Model.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
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
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("error");
            }
        });
    }
}