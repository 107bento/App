package com.example.aishalien.bento;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;



public class main_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] shop_tittle_list = {"果峰小舖","武嶺水果","旗下燒烤","冰品","士林粥品","賀氏滷味","和風食堂","山雞肉飯"};
    private int[] shop_img_list;
    private List<ShopItemData> itemsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化imgbut
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 監聽是否按下nav_header
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main_menu);
        navHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳頁
                Intent intento = new Intent();
                intento.setClass(main_menu.this, profile.class);
                startActivity(intento);
            }
        });

        // recycler view 初始化
        initData();
        initView();

    }

    private void initView() {

        // 宣告 recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shops_recycler_view);
        // Grid型態，第二個參數控制一列顯示幾項
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 設定打開recycler view不是top
        recyclerView.setFocusable(false);
        // （可選）如果可以確定每個item的高度是固定的，設置這個選項可以提高性能
        recyclerView.setHasFixedSize(true);

        // 設定要給 Adapter 的陣列為 itemsData
        ShopAdapter mAdapter = new ShopAdapter(itemsData);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    // 塞入店家資料
    private void initData() {
        itemsData = new ArrayList<>(); // 店家標題
        shop_img_list = new int[shop_tittle_list.length]; // 店家圖片

        for (int i = 0; i < shop_tittle_list.length; i++) {

            String imgName = "store"+ Integer.toString(i);
            int imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());
            shop_img_list[i] = imgId;

            itemsData.add(new ShopItemData(shop_tittle_list[i], imgId));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.my_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            // 按下搜尋
            case R.id.my_search:
                //TODO search
                break;

            // 按下購物車
            case R.id.goto_shop_cart:
                Intent intento = new Intent();
                intento.setClass(main_menu.this, shopping_cart.class);
                startActivity(intento);
                break;

            // 按下說明
            case R.id.question_btn:
                // 彈出dialog
                new AlertDialog.Builder(main_menu.this)
                        // 標題
                        .setTitle(R.string.explanation)
                        // 訊息
                        .setMessage(R.string.explanation_content)
                        // 按下ok返回畫面
                        .setPositiveButton(R.string.ok, null)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food) {
            // 今日餐點
            Intent intent = new Intent();
            intent.setClass(main_menu.this, today_meal.class);
            startActivity(intent);

        } else if (id == R.id.nav_record) {
            // 購買紀錄
            Intent intent = new Intent();
            intent.setClass(main_menu.this, purchase_record.class);
            startActivity(intent);

        } else if (id == R.id.nav_anno) {
            // 公告欄dialog
            new AlertDialog.Builder(main_menu.this)
                // 標題
                .setTitle(R.string.announcement)
                // 訊息
                .setMessage(R.string.announcement_content)
                // 按下ok返回畫面
                .setPositiveButton(R.string.ok, null)
                .show();

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.feed_back) {

        } else if (id == R.id.nav_logout) {
            // 按下登出跳出對話方塊
            new AlertDialog.Builder(main_menu.this)
                // 標題
                .setTitle(R.string.logout)
                // 訊息
                .setMessage(R.string.want_to_logout)
                // 按下登出，離開原畫面
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                // 按下取消，返回原本畫面
                .setNegativeButton(R.string.cancel, null)
                .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}