package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
    int random_pick;
    int amount;
    int meal_value;
    int position;
    String store_id;
    GlobalVariable User;
    String meal_id;
    String store_name;
    JsonArray cartvalue;
    JsonArray information;
    String swish_id[] = {"1","1","1"};
    String wish_id[] = {"1","1","1"};
    //控制數量
    int max = 99; //最大數量
    int min = 1; //最小數量
    int current = 1; //要顯示的值
    int orign;//原本該筆項目的總額
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
        User = (GlobalVariable)getApplicationContext();
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
        meal_valueT.setText("NT." + Integer.toString(bundle.getInt("meal_value"))+"元");
        //接相關資料
        meal_id = bundle.getString("meal_id");
        store_name = bundle.getString("store_name");
        position = bundle.getInt("position");
        amount = bundle.getInt("amount");
        orign = amount*meal_value;
        store_id = bundle.getString("store_id");
        current = amount;
        random_pick = bundle.getInt("random_pick");
        //判斷是不是不要wish
        String Jignore1 = cartvalue.get(position).getAsJsonObject().get("wish_id_1").getAsString();
        String Jignore2 = cartvalue.get(position).getAsJsonObject().get("wish_id_2").getAsString();
        String Jignore3 = cartvalue.get(position).getAsJsonObject().get("wish_id_3").getAsString();
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
        if(random_pick==1){//有被勾選的狀態
            autorandom.setChecked(true);
        }else{
            autorandom.setChecked(false);
        }

        //判斷randompick目前的狀況
        autorandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {//0為沒有選  1為有選的
            if(isChecked) {
                random_pick = 1;
            }
            else{
                random_pick=0;
            }
            }
        });
        // 數量控件
        AmountView mAmountView = (AmountView) findViewById(R.id.amountView);
        mAmountView.setMaxValue(max); // 設置最大數量
        mAmountView.setMinValue(min); // 設置最小值
        mAmountView.setCurrentValue(current); //設置目前要顯示的數量
        counter = current;
        mAmountView.setListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(int amount) {
                //  紀錄數量
                counter = amount;
            }
        });
    }

    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.confirm_change);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((swish_id[0].equals(swish_id[1])||swish_id[1].equals(swish_id[2])||swish_id[2].equals(swish_id[0]))&& (!swish_id[0].equals("0")&&!swish_id[1].equals("0")|| !swish_id[1].equals("0")&& !swish_id[2].equals("0")|| !swish_id[2].equals("0")&&!swish_id[0].equals("0"))){
                    new AlertDialog.Builder(shopping_cart_modify.this)
                            .setTitle("請不要選擇相同店家")
                            .setMessage("志願序考量各店家營業狀況不同，希望使用者選擇其他店家以防無法成單")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    System.out.println(swish_id[0]+" "+swish_id[1]+" "+swish_id[2]);
                }
                else if(random_pick == 1&&(swish_id[0].equals(swish_id[1])&&swish_id[1].equals(swish_id[2])&&swish_id[2].equals(swish_id[0])&&swish_id[0].equals("0"))){
                    new AlertDialog.Builder(shopping_cart_modify.this)
                            .setTitle("請選擇志願序")
                            .setMessage("請選擇志願序至少一個志願序")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                else{
                    // 抓取頁面資料，保存到本地端（購物車資料）
                    User.editCart(position,orign,meal_id,counter*meal_value,counter,wish_id[0],wish_id[1],wish_id[2],random_pick);
                    User.editInfo(position,store_name,store_id,counter,mMeal,swish_id[0],swish_id[1],swish_id[2],meal_value);
                    // 提示訊息
                    Toast toast = Toast.makeText(shopping_cart_modify.this,
                            "已修改", Toast.LENGTH_SHORT);
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
    //找到目前adapter中該店家的擺放編號 並回傳是adpter內的第幾個 回傳的為要擺入陣列的第i個
    public int set_store_init(int position,int spin){//spin為第幾項 志願序
        String tmp_id[] = {"swish_id1","swish_id2","swish_id3"};
        for(int i=0;i<storeID.length;i++){
            if(information.get(position).getAsJsonObject().get(tmp_id[spin]).getAsString().equals(storeID[i])){
                return i;
            }
        }
        return 0;
    }
    //三個志願序的餐點志願序紀錄 回傳的為要擺入陣列的第i個
    public int set_meal_init(int position,String[] meal,int spin){
        String tmp_id[] = {"wish_id_1","wish_id_2","wish_id_3"};
        for(int i=0;i<meal.length;i++){
            if(cartvalue.get(position).getAsJsonObject().get(tmp_id[spin]).getAsString().equals(meal[i])){
                return i;
            }
        }
        return 0;
    }
    //設定有甚麼店家是可以選擇的
    public void setshopOption(){
        spinnerS1 = (Spinner)findViewById(R.id.store_spinner01);
        final ArrayAdapter<String> shopList = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,store);
        spinnerS1.setAdapter(shopList);
        //設定預設值
        spinnerS1.setSelection(set_store_init(position,0));
        //設定預設的store是哪家店
        swish_id[0] = storeID[set_store_init(position,0)];
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
        //設定預設值
        spinnerS2.setSelection(set_store_init(position,1));
        //設定預設的store是哪家店
        swish_id[1] = storeID[set_store_init(position,1)];
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
        swish_id[2] = storeID[set_store_init(position,2)];
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
        //扣掉預設空白值
        int nowStore = storeID-1;
        if(rid==R.id.store_spinner01||rid == -1){
            String[] meal;
            //第一家店的餐點志願序
            spinnerM1 = (Spinner)findViewById(R.id.meal_spinner01);
            if(storeID == 0){
                meal = new String[1];
                meal_id1 = new String[1];
                meal[0]="   ";
                meal_id1[0] = "0";
            }else{
                //第一家店的餐點志願序
                JsonArray tmpmeal1 = resource.get(nowStore).getAsJsonObject().get("meals").getAsJsonArray();
                meal= new String[tmpmeal1.size()];
                meal_id1 = new String[tmpmeal1.size()];
                for(int i=0;i<tmpmeal1.size();i++){
                    meal[i] =tmpmeal1.get(i).getAsJsonObject().get("meal_name").getAsString();
                    meal_id1[i] = tmpmeal1.get(i).getAsJsonObject().get("meal_id").getAsString();
                }
            }
            ArrayAdapter<String> mealList1 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM1.setAdapter(mealList1);
            //設定初始值
            spinnerM1.setSelection(set_meal_init(position,meal_id1,0));
            wish_id[0] = meal_id1[set_meal_init(position,meal_id1,0)];
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
            spinnerM2 = (Spinner)findViewById(R.id.meal_spinner02);
            String[] meal;
            if(storeID == 0){
                meal = new String[1];
                meal_id2 = new String[1];
                meal[0]="    ";
                meal_id2[0] = "0";
            }else{
                //第二家店的第一個餐點志願序
                JsonArray tmpmeal2 = resource.get(nowStore).getAsJsonObject().get("meals").getAsJsonArray();
                meal= new String[tmpmeal2.size()];
                meal_id2 = new String[tmpmeal2.size()];
                for(int i=0;i<tmpmeal2.size();i++){
                    meal[i] =tmpmeal2.get(i).getAsJsonObject().get("meal_name").getAsString();
                    meal_id2[i] = tmpmeal2.get(i).getAsJsonObject().get("meal_id").getAsString();
                }
            }
            ArrayAdapter<String> mealList2 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM2.setAdapter(mealList2);
            spinnerM2.setSelection(set_meal_init(position,meal_id2,1));
            wish_id[1] = meal_id2[set_meal_init(position,meal_id2,1)];
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
            String[] meal;
            //第三家店的第一個餐點志願序
            spinnerM3 = (Spinner)findViewById(R.id.meal_spinner03);
            if(storeID == 0){
                meal = new String[1];
                meal_id3 = new String[1];
                meal[0]="    ";
                meal_id3[0] = "0";
            }else{
                //第三家店的第一個餐點志願序
                JsonArray tmpmeal3 = resource.get(nowStore).getAsJsonObject().get("meals").getAsJsonArray();
                meal= new String[tmpmeal3.size()];
                meal_id3 = new String[tmpmeal3.size()];
                for(int i=0;i<tmpmeal3.size();i++){
                    meal[i] =tmpmeal3.get(i).getAsJsonObject().get("meal_name").getAsString();
                    meal_id3[i] = tmpmeal3.get(i).getAsJsonObject().get("meal_id").getAsString();
                }
            }
            ArrayAdapter<String> mealList3 = new ArrayAdapter<>(shopping_cart_modify.this, android.R.layout.simple_spinner_dropdown_item,meal);
            spinnerM3.setAdapter(mealList3);
            spinnerM3.setSelection(set_meal_init(position,meal_id3,2));
            wish_id[2] = meal_id3[set_meal_init(position,meal_id3,2)];
            //監聽事件
            spinnerM3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    wish_id[2] = meal_id3[position];
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
                if(response.code()==200){
                    resource = response.body().getAsJsonArray();
                    store = new String[resource.size()];
                    storeID = new String[resource.size()];
                    //加一個為最前方預設的空白值
                    store = new String[resource.size()+1];
                    storeID = new String[resource.size()+1];
                    //設定預設選項
                    store[0] = " 請選擇店家";
                    storeID[0]="0";
                    for(int i=0;i<resource.size();i++){
                        tmp = new JsonObject();
                        tmp = resource.get(i).getAsJsonObject();
                        store[i+1] =tmp.get("shop_name").getAsString();
                        storeID[i+1] =tmp.get("shop_id").getAsString();
                    }
                    //設定店家選項
                    setshopOption();
                    //哪一家店的菜單 初始必為第一家店，-1表示初始狀態
                    setmealOption(-1,0);
                }else{
                    Toast.makeText(shopping_cart_modify.this, "系統錯誤", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(shopping_cart_modify.this, "系統錯誤", Toast.LENGTH_SHORT).show();
            }
        });
    }
}