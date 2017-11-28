package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class profile extends AppCompatActivity {

    TextView tv_account,tv_name, tv_remain, tv_email, tv_phone;
    String user_id, password, name, money, email, phone;

    JsonObject resource;
    static String cookie;

    /*跟API架接的架構 使用GET後面加上base url後的路徑*/
    public interface Api{
        @GET("user")
        // Call內部為接的資料格式  以及參數Path
        Call<JsonObject> getinfo(@Header("Cookie") String userCookie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getView();

        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();
        try {
            getJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        // Title
        toolbar.setTitle(name);
        // nav返回鍵
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        // 設置返回按鍵作用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // collapsingToolbar
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);


        // 相機按鈕FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_photo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // 找到元件
    private void getView() {
        tv_account = (TextView) findViewById(R.id.profile_account);
        tv_name = (TextView) findViewById(R.id.profile_name);
        tv_email = (TextView) findViewById(R.id.profile_email);
        tv_phone = (TextView) findViewById(R.id.profile_phone);
//        tv_remain = (TextView) findViewById(R.id.remain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) { // 點擊 menu 上的編輯
            String p_account = (((TextView) findViewById(R.id.profile_account)).getText()).toString();
            String p_name = (((TextView) findViewById(R.id.profile_name)).getText()).toString();
            String p_email = (((TextView) findViewById(R.id.profile_email)).getText()).toString();
            String p_phone = (((TextView) findViewById(R.id.profile_phone)).getText()).toString();

            // 傳jsonObject
            Intent intent = new Intent();
            intent.setClass(profile.this, profile_modify.class);
            intent.putExtra("json", resource.toString());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    };

    public void getJson() throws IOException {
        /*創建一個retrofit*/
        /*OKHTTP*/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        //建立retrofit網路接口
        //設定base url
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://163.22.17.227/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        //接API
        profile.Api api = retrofit.create(profile.Api.class);
        Call<JsonObject> Model = api.getinfo(cookie);
        //使用方法為異步 並且呼叫
        Model.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //成功接到 response
                resource = response.body();
                //初始化
                if (resource != null) {
                    initText(resource);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void initText(JsonObject object) {

         //根據json 架構接入並轉成string
         user_id = object.get("user_id").getAsString();
         password = object.get("password").getAsString();
         name = object.get("name").getAsString();
         money = object.get("money").getAsString();
         email = object.get("email").getAsString();
         phone = object.get("phone").getAsString();

         // 設定文字
         tv_account.setText(user_id);
         tv_name.setText(name);
         tv_remain.setText("$" + money);
         tv_email.setText(email);
         tv_phone.setText(phone);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case RESULT_OK:
                // 接收 profile_modify 的資料
                Intent intent = getIntent();
                String jsonString = intent.getStringExtra("jsonResult");

                // 重新serialize 傳進來的 json.toString
                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonString);
                initText(gsonObject);
                break;
        }
    }
}

