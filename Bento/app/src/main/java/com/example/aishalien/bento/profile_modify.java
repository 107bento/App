package com.example.aishalien.bento;

/**
 * Created by yuting on 2017/10/3.
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class profile_modify extends AppCompatActivity {
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_modify);

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


        // 接收 profile 的資料
        Bundle bData = getIntent().getExtras();
        if (bData != null) {
            TextView mAccount = (TextView) findViewById(R.id.account) ;
            EditText mName = (EditText) findViewById(R.id.name);
            EditText mEmail = (EditText) findViewById(R.id.email);
            EditText mPhone = (EditText) findViewById(R.id.phone);

            // 回寫資料
            mAccount.setText(bData.getString("mAccount"));
            mName.setText(bData.getString("mName"));
            mEmail.setText(bData.getString("mEmail"));
            mPhone.setText(bData.getString("mPhone"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_profile_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {

            case android.R.id.home: // 按了 Action Bar 的返回鍵
                onBackPressed();
                return true;    // 注意! 一定要回傳 true

            case R.id.action_ok:  // 點擊 menu 上的 ok
                // 取得頁面資料
                String mAccount = (((TextView) findViewById(R.id.account)).getText()).toString();
                String modified_name = (((EditText) findViewById(R.id.name)).getText()).toString();
                String old_pwd = (((EditText) findViewById(R.id.old_password)).getText()).toString();
                String new_pwd = (((EditText) findViewById(R.id.new_password)).getText()).toString();
                String check_new_pwd = (((EditText) findViewById(R.id.check_new_pwd)).getText()).toString();
                String modified_email = (((EditText) findViewById(R.id.email)).getText()).toString();
                String modified_phone = (((EditText) findViewById(R.id.phone)).getText()).toString();

                // 準備回傳前頁的資料
                Bundle bag = new Bundle();
                bag.putString("mName", modified_name);
                bag.putString("mEmail", modified_email);
                bag.putString("mPhone", modified_phone);

                // 送出資料，存回資料庫


                // 返回前頁
                Intent mIntent = new Intent();
                mIntent.setClass(profile_modify.this, profile.class);
                mIntent.putExtras(bag);
                startActivity(mIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}

