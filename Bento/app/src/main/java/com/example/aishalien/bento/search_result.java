package com.example.aishalien.bento;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by yuting on 2017/10/25.
 */

public class search_result extends AppCompatActivity
{
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        txt1 = (TextView) findViewById(R.id.txt1);

        // 注意這一行指令
        handleIntent(getIntent());


        // toolbar
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
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


    @Override
    protected void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }


    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            txt1.setText("傳遞的查詢字串為 "+query.toString());
        }
    }
}
