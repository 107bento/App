package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public class profile_modify extends AppCompatActivity {
    private Toolbar mtoolbar;

    String mAccount, modified_name, old_pwd, new_pwd, check_new_pwd, modified_email, modified_phone;
    String user_id, password, name, money, email, phone;
    static String cookie;
    JsonObject paramObject;

    public interface PatientInfoAPI{
        @PATCH("user")
        Call<JsonObject> update(@Header("Cookie") String userCookie, @Body JsonObject updatedDATA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_profile_modify);

        GlobalVariable User = (GlobalVariable)getApplicationContext();
        cookie = User.getCookie();

        // toolbar
        mtoolbar = (Toolbar) findViewById(R.id.toolbar_profile_modify);
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);
        // 設置返回按鍵作用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 接收 profile 來的資料
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("json");

        // 重新serialize 傳進來的 json.toString
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonString);
        initText(gsonObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_profile_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            // 按了 Action Bar 的返回鍵
            case android.R.id.home:
                onBackPressed();
                return true;

            // 點擊 menu 上的 ok
            case R.id.action_ok:
                // 檢查 > patch > 資料映射回去
                if (check()) {
                    Intent intent = new Intent(profile_modify.this, profile.class);
                    intent.putExtra("jsonResult", paramObject.toString());
                    startActivityForResult(intent, RESULT_OK);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改個人資料：檢查後patch
     * @return
     */
    private boolean check() {
        boolean flag = false;

        // 取得頁面資料
        mAccount = (((TextView) findViewById(R.id.proMod_account)).getText()).toString();
        modified_name = (((EditText) findViewById(R.id.new_name)).getText()).toString();
        old_pwd = (((EditText) findViewById(R.id.old_password)).getText()).toString();
        new_pwd = (((EditText) findViewById(R.id.new_password)).getText()).toString();
        check_new_pwd = (((EditText) findViewById(R.id.check_new_pwd)).getText()).toString();
        modified_email = (((EditText) findViewById(R.id.new_email)).getText()).toString();
        modified_phone = (((EditText) findViewById(R.id.new_phone)).getText()).toString();

        /**
         * 若有修改密碼:
         * 1. 檢查舊密碼是否正確
         * 2. double check 新密碼
         * 3. 更新密碼
         */
        if ( !(old_pwd.isEmpty()) || !(new_pwd.isEmpty()) || !(check_new_pwd.isEmpty()) ) {

            // 舊密碼錯誤
            if ( !(old_pwd.equals(password)) ) {
                // 彈出dialog
                new AlertDialog.Builder(profile_modify.this)
                        // 標題
                        .setTitle(R.string.pwd_error)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                return flag;

            // 新密碼不相符
            } else if ( !(new_pwd.equals(check_new_pwd)) ) {
                // 彈出dialog
                new AlertDialog.Builder(profile_modify.this)
                        // 標題
                        .setTitle(R.string.pwd_error_check)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                return flag;

            } else {
                password = new_pwd;
            }
        }

        /**
         * patch回資料庫
         */
        try {
            patchJson();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * patch
     */
    public void patchJson() throws IOException {
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
        PatientInfoAPI patchService =  retrofit.create(PatientInfoAPI.class);

        // 要patch的資料
        paramObject = new JsonObject();
        paramObject.addProperty("name", modified_name);
        paramObject.addProperty("password", password);
        paramObject.addProperty("phone", modified_phone);
        paramObject.addProperty("email", modified_email);

        //Passing the tableId along with the data to update
        Call<JsonObject> Model = patchService.update(cookie, paramObject);
        //使用方法為異步 並且呼叫
        Model.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //成功接到 response

                if (response.code() == 200){
                    Toast.makeText(profile_modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400){
                    Toast.makeText(profile_modify.this, "修改失敗", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("Error", t.getLocalizedMessage().toString());
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

        // 找到元件
        TextView mAccount = (TextView) findViewById(R.id.proMod_account) ;
        EditText mName = (EditText) findViewById(R.id.new_name);
        EditText mEmail = (EditText) findViewById(R.id.new_email);
        EditText mPhone = (EditText) findViewById(R.id.new_phone);

        // 設定文字
        mAccount.setText(user_id);
        mName.setText(name);
        mEmail.setText(email);
        mPhone.setText(phone);
    }
}