package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

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
import retrofit2.http.PATCH;

public class profile_modify extends AppCompatActivity {
    private Toolbar mtoolbar;

    String user_id, password, name, money, email, phone;
    static String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_profile_modify);

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
                // 檢查 > patch > back
                if (checkOK()){
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改個人資料
     * 還沒patch回資料庫
     * @return
     */
    private boolean checkOK() {
        boolean isOk = false;

        // 取得頁面資料
        String mAccount = (((TextView) findViewById(R.id.account)).getText()).toString();
        String modified_name = (((EditText) findViewById(R.id.name)).getText()).toString();
        String old_pwd = (((EditText) findViewById(R.id.old_password)).getText()).toString();
        String new_pwd = (((EditText) findViewById(R.id.new_password)).getText()).toString();
        String check_new_pwd = (((EditText) findViewById(R.id.check_new_pwd)).getText()).toString();
        String modified_email = (((EditText) findViewById(R.id.email)).getText()).toString();
        String modified_phone = (((EditText) findViewById(R.id.phone)).getText()).toString();

        /**
         * 若有修改密碼:
         * 1. 檢查舊密碼是否正確
         * 2. double check 新密碼
         */
        if ( !(old_pwd.equals("")) || !(new_pwd.equals("")) || !(check_new_pwd.equals("")) ) {

            // 舊密碼錯誤
            if ( !(old_pwd.equals(password)) ) {
                // 彈出dialog
                new AlertDialog.Builder(profile_modify.this)
                        // 標題
                        .setTitle(R.string.pwd_error)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                return isOk;

            // 新密碼不相符
            } else if ( !(new_pwd.equals(check_new_pwd)) ) {
                // 彈出dialog
                new AlertDialog.Builder(profile_modify.this)
                        // 標題
                        .setTitle(R.string.pwd_error_check)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                return isOk;
            }
        }

        /**
         * patch回資料庫
         */
        isOk = true;


        return isOk;
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
        TextView mAccount = (TextView) findViewById(R.id.account) ;
        EditText mName = (EditText) findViewById(R.id.name);
        EditText mEmail = (EditText) findViewById(R.id.email);
        EditText mPhone = (EditText) findViewById(R.id.phone);

        // 設定文字
        mAccount.setText(user_id);
        mName.setText(name);
        mEmail.setText(email);
        mPhone.setText(phone);
    }
}
