package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
<<<<<<< HEAD
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by AishaLien on 2017/9/23.
 */
=======

>>>>>>> 5a4cede645c5a98cc2a40690b746edcc9cc2f19f

public class store_menu extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private android.support.design.widget.TabLayout mTabs;

    private Toolbar mtoolbar;
    private ViewPager mViewPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listView;
<<<<<<< HEAD
    JsonObject resource;
    private String[] mlist;
    private Meal[] list ;// {"握壽司","鮭魚手卷","蝦捲","鮭魚定飯","天婦羅定食","茶碗蒸","刺身"};
=======
    private String[] list = {"握壽司","鮭魚手卷","蝦捲","鮭魚定飯","天婦羅定食","茶碗蒸","刺身"};
    private String[] vlist = {"90","100","50","75","68","77","58"};
>>>>>>> 5a4cede645c5a98cc2a40690b746edcc9cc2f19f

    private int[] piclist;// = {R.drawable.purch_1,R.drawable.purch_2,"50","75","68","77","58"};
    private ArrayAdapter<String> listAdapter;
    public interface Api{
        @GET("shops/{id}")
        Call<JsonObject> getinfo(@Path("id") int id);
    }

    class Meal{
        String ID;
        String name;
        int value;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.store_menu);
        try {
            getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        toolbar
=======
        setContentView(R.layout.activity_store_menu);

        setpictureList(list.length);
        initListView();

        ImageView storeImg = (ImageView)findViewById(R.id.store_img);
        Bundle bundle = getIntent().getExtras();
        storeImg.setImageResource(bundle.getInt("store_img"));


        // toolbar
>>>>>>> 5a4cede645c5a98cc2a40690b746edcc9cc2f19f
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
        mtoolbar.setTitle(bundle.getString("store_name"));
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

<<<<<<< HEAD
    private void initListView(){
        listView = (ListView)findViewById(R.id.store1_menu);//找到物件
        //利用adapter當接口 this為activity,樣式,擺入的字串
        listAdapter = new ArrayAdapter(this,R.layout.stl_menu_1,mlist);
=======
    private void setpictureList(int listLength){
        piclist = new int[listLength];
        for (int i = 0; i < listLength; i++){
            String picName = "purch_"+ Integer.toString(i);
            int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
            piclist[i] = picId;
        }
    }

    private void initListView(){
        listView = (ListView) findViewById(R.id.store1_menu); //找到物件
        // 利用adapter當接口 this為activity,樣式,擺入的字串
        listAdapter = new ArrayAdapter(this,R.layout.stl_menu_1,list);
>>>>>>> 5a4cede645c5a98cc2a40690b746edcc9cc2f19f
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //監聽
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.length; i++) {
                    if (list[position].equals(list[i])) {
                        //建立一個Bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("meal",list[i].name);
                        bundle.putInt("value",list[i].value);
                        bundle.putInt("pic",piclist[i]);
                        Intent intento = new Intent();
                        intento.setClass(store_menu.this,meal_purchase.class);
                        //將bundle傳入
                        intento.putExtras(bundle);
                        startActivity(intento);
                    }
                }
<<<<<<< HEAD

                //如果有執行以下
                Toast.makeText(getApplicationContext(), "你選擇的是" + list[position].name, Toast.LENGTH_SHORT).show();
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
        Call<JsonObject> Model = api.getinfo(1);
        Model.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                resource = response.body();


                initList();
                initListView();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
    public void initList(){
        list = new Meal[resource.entrySet().size()];
        mlist = new String[resource.entrySet().size()];
        setpictureList(list.length);
        for(int i=0;i<resource.entrySet().size();i++){
            list[i] = new Meal();
            System.out.println(" 嗨囉"+resource.getAsJsonObject(Integer.toString(i+1)).getAsJsonObject().getAsJsonPrimitive("meal_id").toString());
            list[i].ID = resource.getAsJsonObject(Integer.toString(i+1)).getAsJsonObject().getAsJsonPrimitive("meal_id").getAsString();
            list[i].name = resource.getAsJsonObject(Integer.toString(i+1)).getAsJsonObject().getAsJsonPrimitive("meal_name").getAsString();
            mlist[i] = resource.getAsJsonObject(Integer.toString(i+1)).getAsJsonObject().getAsJsonPrimitive("meal_name").getAsString();
            list[i].value = resource.getAsJsonObject(Integer.toString(i+1)).getAsJsonObject().getAsJsonPrimitive("meal_price").getAsInt();
        }
    }
    /*處理圖片*/
    private void setpictureList(int listLength){
        piclist = new int[listLength];
        for(int i=0;i<listLength;i++){
            String picName = "purch_"+ Integer.toString(i);
            int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
            piclist[i] = picId;
        }
=======
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch(id)
        {
            // 按了 Action Bar 的返回鍵
            case android.R.id.home:
                onBackPressed();
                return true;

            // 按下購物車
            case R.id.goto_shop_cart:
                Intent intento = new Intent();
                intento.setClass(store_menu.this, shopping_cart.class);
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
>>>>>>> 5a4cede645c5a98cc2a40690b746edcc9cc2f19f
    }
}
