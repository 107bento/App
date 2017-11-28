package com.example.aishalien.bento;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public class goto_add_value extends AppCompatActivity {

    int x;
    JsonObject storeValue;
    static String cookie;

    /*跟API架接的架構 使用GET後面加上base url後的路徑*/
    public interface Api{
        @POST("user/store")
            // Call內部為接的資料格式  以及參數Path
        Call<JsonObject> store(@Header("Cookie") String userCookie, @Body JsonObject body);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();

        setButtonView();
    }

    private void setButtonView() {

        // 找到button
        Button btn1 = (Button) findViewById(R.id.add_100);
        Button btn2 = (Button) findViewById(R.id.add_250);
        Button btn3 = (Button) findViewById(R.id.add_400);
        Button btn4 = (Button) findViewById(R.id.add_900);
        Button btn5 = (Button) findViewById(R.id.add_1000);

        // 設置監聽
        btn1.setOnClickListener(new ButtonListener());
        btn2.setOnClickListener(new ButtonListener());
        btn3.setOnClickListener(new ButtonListener());
        btn4.setOnClickListener(new ButtonListener());
        btn5.setOnClickListener(new ButtonListener());
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
        }
        return super.onOptionsItemSelected(item);
    }

    // 多button監聽
    class ButtonListener implements View.OnClickListener {

        public void onClick(View arg) {

            int id = arg.getId();
            // 設定金額
            if (id == R.id.add_100){
                x = 100;
            } else if (id == R.id.add_250) {
                x = 250;
            } else if (id == R.id.add_400) {
                x = 400;
            } else if (id == R.id.add_900) {
                x = 900;
            } else if (id == R.id.add_1000) {
                x = 1000;
            }
            // post
            try {
                postStore(x, arg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // post
    private void postStore(int value, final View view) throws IOException {
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
        Api api = retrofit.create(Api.class);
        // 儲值的金額
        storeValue = new JsonObject();
        storeValue.addProperty("value", value);

        Call<JsonObject> Model = api.store(cookie, storeValue);
        //使用方法為異步 並且呼叫
        Model.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //成功接到 response

                if (response.code() == 200) {
                    Snackbar.make(view, "儲值成功！", Snackbar.LENGTH_LONG)
                            .setAction("前往查看餘額", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(goto_add_value.this, profile.class);
                                    startActivity(intent);
                                }
                            }).show();

                } else if (response.code() == 400) {
                    Snackbar.make(view, "儲值失敗", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(goto_add_value.this, "儲值失敗", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("Error", t.getLocalizedMessage().toString());
            }
        });
    }
}