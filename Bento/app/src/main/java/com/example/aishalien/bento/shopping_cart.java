package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class shopping_cart extends AppCompatActivity {
    // 目標放入的MAP
    ArrayList<HashMap<String, Object>>  mList;
    ListView cartListView;
    GlobalVariable User ;
    private Toolbar mtoolbar;
    shopCart service;
    //更新該頁面使用的參數
    ViewGroup shop_cart_view;
    final int  modifyCart=0;
    //定義接口
    public interface shopCart{
        @Headers("Content-Type: application/json")
        @POST("orders")
        Call<JsonObject> repo(@Header("Cookie") String userCookie,@Body JsonObject body);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        shop_cart_view = findViewById(R.id.activity_shopping_cart);
        //listview
        cartListView=(ListView)findViewById(R.id.activity_shopping_cart);
        mList =  new ArrayList<HashMap<String, Object>> ();
        User = (GlobalVariable)getApplicationContext();
        JsonArray information = User.ch_details;
        JsonArray cartvalue = User.details;
        System.out.println("Car"+cartvalue);
        System.out.println(information);
        //拿到店家名稱
        String[] listFromResource = new String[information.size()];
        //圖片設置
        int[] listFromPic = new int[information.size()];
        //拿到餐點名稱
        String[] listFromMeal = new String[information.size()];
        //拿到餐點數量
        String[] listFromNum = new String[information.size()];
        //拿到餐點價格
        String[] listFromvalue = new String[information.size()];
        //拿到店家ID
        String[] listStoreID =  new String[information.size()];
        //拿到餐點ID
        String[] listMealID =  new String[information.size()];
        //拿到checkbox狀態
        int[] listcheck =  new int[information.size()];
        for(int i=0;i<information.size();i++){
            listFromResource[i] = information.get(i).getAsJsonObject().get("store_name").getAsString();
            listFromMeal[i]= information.get(i).getAsJsonObject().get("meal_name").getAsString();
            listFromvalue[i]= information.get(i).getAsJsonObject().get("meal_value").getAsString();
            listMealID[i] = cartvalue.get(i).getAsJsonObject().get("meal_id").getAsString();
            listStoreID[i] = information.get(i).getAsJsonObject().get("store_id").getAsString();
            String tmpv = information.get(i).getAsJsonObject().get("store_id").getAsString();
            String picName = "store"+ Integer.toString(Integer.valueOf(tmpv)-1)+"_"+cartvalue.get(i).getAsJsonObject().get("meal_id").getAsString();
            listcheck[i] = cartvalue.get(i).getAsJsonObject().get("random_pick").getAsInt();
            listFromPic[i] =getResources().getIdentifier(picName, "drawable", getPackageName());
            listFromNum[i]= information.get(i).getAsJsonObject().get("amount").getAsString();
            System.out.println(listFromNum[i]);
        }

        HashMap<String, Object> item ;
        for(int i=0; i<listFromResource.length;i++) {
            item = new HashMap<String, Object>();
            item.put("store_pic", listFromPic[i]);
            item.put("store_name",listFromResource[i]);
            item.put("store_id",listStoreID[i]);
            item.put("meal_id",listMealID[i]);
            item.put("random_pick",listcheck[i]);
            item.put("meal_name",listFromMeal[i]);
            item.put("meal_num",listFromNum[i]+"份");
            item.put("meal_amount",listFromNum[i]);
            item.put("meal_value",listFromvalue[i]);
            item.put("cart_button", R.id.ButtonCart);
            mList.add(item);
        }
        //執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
//        SimpleAdapter adapter = new SimpleAdapter(
//                this,
//                mList,
//                R.layout.list_item_cart,
//                new String[]{"store_name", "store_pic","meal_name","meal_num"},
//                new int[]{R.id.store_name,R.id.store_pic,R.id.meal_name,R.id.meal_num});
        cartAdapter  adapter  = new cartAdapter (
                this,
                mList,
                R.layout.list_item_cart,
                new String[] {"store_pic", "store_name","meal_name","meal_num","cart_button"},
                new int[] {R.id.store_pic,R.id.store_name,R.id.meal_name,R.id.meal_num,R.id.ButtonCart}
        ,User);

        // 加入購物車footer
        View footerView = ((LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_shopping_cart_footer, null, false);
        cartListView.addFooterView(footerView);
        Button sendCart = (Button)findViewById(R.id.btn_put_in_cart);

        cartListView.setAdapter(adapter);

        // 避免footer的監聽事件被item内的button搶走，
        cartListView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        //添加點擊事件
        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //获得选中项的HashMap对象
                HashMap<String,Object> map=(HashMap<String,Object>)cartListView.getItemAtPosition(arg2);
                Object Omeal_value = map.get("meal_value");
                Object meal_name = map.get("meal_name");
                Object meal_id = map.get("meal_id");
                Object store_name = map.get("store_name");
                Object store_id = map.get("store_id");
                Object meal_num = map.get("meal_amount");
                Object store_pic = map.get("store_pic");
                Object check_status = map.get("random_pick");
                // 建立一個Bundle
                Bundle bundle = new Bundle();
                bundle.putString("meal", (String) meal_name);
                bundle.putString("store_name", (String) store_name);
                bundle.putInt("meal_value",  Integer.valueOf((String)Omeal_value) );
                bundle.putString("meal_id", (String) meal_id);
                bundle.putInt("amount",Integer.valueOf((String)meal_num));
                bundle.putInt("position",arg2);
                bundle.putString("store_id",(String)store_id);
                bundle.putInt("store_pic",(int)store_pic);
                bundle.putInt("random_pick",(int)check_status);
                Intent intento = new Intent();
                intento.setClass(shopping_cart.this, shopping_cart_modify.class);
                // 將bundle傳入
                intento.putExtras(bundle);
                //startActivity(intento,"Mar");

                //從modify回來的
                String passString = "FromCart";
                intento.putExtra("FromCart", passString);
                startActivityForResult(intento, modifyCart);
            }
        });

        //設置listview高度
//        setListViewHeightBasedOnChildren(cartListView);


        //  toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
        mtoolbar.setTitle(R.string.shop_cart);
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);
//        //不顯示返回符號，在onOptionsItemSelected須設定
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置返回键的点击事件：
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
        /*購物車確定購買*/
        sendCart.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                callJson();
            }

        });
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     * 解決listview與scrollview衝突而無法設定以其內容作為其高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
                finish();
                return true;

            // 按下首頁
            case R.id.goto_main:
                Intent intento = new Intent();
                intento.setClass(shopping_cart.this, main_menu.class);
                startActivity(intento);
                finish();
                break;

            // 按下說明
            /*
            case R.id.question_btn:
                // 彈出dialog
                new AlertDialog.Builder(shopping_cart.this)
                        // 標題
                        .setTitle(R.string.explanation)
                        // 訊息
                        .setMessage(R.string.explanation_content)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                break;
            */
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    public void callJson(){
        /*創建一個retrofit*/
        /*OKHTTP*/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        /*連接*/
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://163.22.17.227/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(shopCart.class);
        if(User.total==0){
            Toast.makeText(this, "購物車是空的", Toast.LENGTH_SHORT).show();
        }else{
            Call<JsonObject> Model = service.repo(User.getCookie(),User.sendCart());
            Model.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code()==200) {
                        JsonObject resource = response.body();
                        User.clean_cart();
                        finish();
                        startActivity(getIntent());
                    }
                    else if(response.code()==400){
                        Toast.makeText(shopping_cart.this, "目前帳戶金額不足", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(shopping_cart.this, "系統錯誤", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println("error");
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case  modifyCart://是從modify_cart那邊返回
                finish();
                startActivity(getIntent());
                break;
        }
    }
}
