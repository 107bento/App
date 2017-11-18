package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
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
import retrofit2.http.Path;

public class store_menu extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private android.support.design.widget.TabLayout mTabs;
    private Toolbar mtoolbar;
    private ListView listView;
    JsonArray resource;
    int store_id;
    private String[] mlist;
    private Meal[] list ;
    private int[] piclist;
    private ArrayAdapter<String> listAdapter;
    public interface Api{
        @GET("shops/{id}")
        Call<JsonArray> getinfo(@Path("id") int id);
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
        setContentView(R.layout.activity_store_menu);
        Bundle bundle = getIntent().getExtras();
        ImageView storeImg = (ImageView)findViewById(R.id.store_img);
        storeImg.setImageResource(bundle.getInt("store_img"));
        store_id = bundle.getInt("store_id");
        try {
            getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // toolbar
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


    private void initListView(){
        listView = (ListView)findViewById(R.id.store1_menu);//找到物件
        //利用adapter當接口 this為activity,樣式,擺入的字串
        listAdapter = new ArrayAdapter(this,R.layout.stl_menu_1,mlist);
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
        Call<JsonArray> Model = api.getinfo(store_id);
        Model.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                resource = response.body();
                initList();
                initListView();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }
    public void initList(){
        list = new Meal[resource.size()];
        mlist = new String[resource.size()];
        setpictureList(list.length);
        for(int i=0;i<resource.size();i++){
            JsonObject tmp = new JsonObject();
            tmp = resource.get(i).getAsJsonObject();
            list[i] = new Meal();
            list[i].ID = tmp.get("meal_id").getAsString();
            list[i].name = tmp.get("meal_name").getAsString();
            mlist[i] = tmp.get("meal_name").getAsString();
            list[i].value =tmp.get("meal_price").getAsInt();
        }
    }
    /*處理圖片*/
    private void setpictureList(int listLength){
        piclist = new int[listLength];
        for(int i=0;i<listLength;i++){
            String picName = "store"+Integer.toString(store_id-1)+"_"+ Integer.toString(i);
            int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
            piclist[i] = picId;
        }
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
            case R.id.menuItem_shoppingCart:
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
    }
}
