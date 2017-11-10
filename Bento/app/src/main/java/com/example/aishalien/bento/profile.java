package com.example.aishalien.bento;

/**
 * Created by yuting on 2017/10/3.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        // Title
        toolbar.setTitle("Aisha Lien");
        // nav返回鍵
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        // 設置返回按鍵作用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // collapsingToolbar
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Aisha Lien");


        // 相機按鈕FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_photo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // 接收 sub_profile_modify 傳回的資料
        Bundle bData = getIntent().getExtras();
        if (bData != null) {
            TextView mName = (TextView) findViewById(R.id.name);
            TextView mEmail = (TextView) findViewById(R.id.email);
            TextView mPhone = (TextView) findViewById(R.id.phone);

            // 回寫資料
            mName.setText(bData.getString("mName"));
            mEmail.setText(bData.getString("mEmail"));
            mPhone.setText(bData.getString("mPhone"));
        }

        // 去儲值
        Button mAddValueButton = (Button) findViewById(R.id.goto_add_value);
        mAddValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent();
                intent.setClass(profile.this, goto_add_value.class);
                startActivity(intent);
            }
        });
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
            String p_account = (((TextView) findViewById(R.id.account)).getText()).toString();
            String p_name = (((TextView) findViewById(R.id.name)).getText()).toString();
            String p_email = (((TextView) findViewById(R.id.email)).getText()).toString();
            String p_phone = (((TextView) findViewById(R.id.phone)).getText()).toString();

            // 準備資料給下一頁
            Bundle bag = new Bundle();
            bag.putString("mAccount", p_account);
            bag.putString("mName", p_name);
            bag.putString("mEmail", p_email);
            bag.putString("mPhone", p_phone);

            // 跳頁
            Intent intent = new Intent();
            intent.setClass(profile.this, profile_modify.class);
            intent.putExtras(bag);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    };
}

