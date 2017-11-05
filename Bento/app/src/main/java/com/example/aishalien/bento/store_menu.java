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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class store_menu extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private android.support.design.widget.TabLayout mTabs;

    private Toolbar mtoolbar;

    private ViewPager mViewPager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView listView;
    private String[] list = {"握壽司","鮭魚手卷","蝦捲","鮭魚定飯","天婦羅定食","茶碗蒸","刺身"};
    private String[] vlist = {"90","100","50","75","68","77","58"};

    private int[] piclist;// = {R.drawable.purch_1,R.drawable.purch_2,"50","75","68","77","58"};
    private ArrayAdapter<String> listAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_menu);

        setpictureList(list.length);
        initListView();

        ImageView storeImg = (ImageView)findViewById(R.id.store_img);
        Bundle bundle = getIntent().getExtras();
        storeImg.setImageResource(bundle.getInt("store_img"));


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
    private void setpictureList(int listLength){
        piclist = new int[listLength];
        for (int i = 0; i < listLength; i++){
            String picName = "purch_"+ Integer.toString(i);
            int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
            piclist[i] = picId;
        }
    }

    private void initListView(){
        listView = (ListView)findViewById(R.id.store1_menu); //找到物件
        // 利用adapter當接口 this為activity,樣式,擺入的字串
        listAdapter = new ArrayAdapter(this,R.layout.stl_menu_1,list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //監聽
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.length; i++) {
                    if (list[position].equals(list[i])) {
                        //建立一個Bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("meal",list[i]);
                        bundle.putString("value",vlist[i]);
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
}
