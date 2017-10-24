package com.example.aishalien.bento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.aishalien.bento.R.string.cancel;
import static com.example.aishalien.bento.R.string.logout;

public class main_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化imgbut
        initimgbtn();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 監聽按下nav_header
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main_menu);
        ImageView headIv = (ImageView) navHeaderView.findViewById(R.id.user_head);
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳頁
                Toast.makeText(main_menu.this, "点击我的头像", Toast.LENGTH_SHORT).show();
                Intent intento = new Intent();
                intento.setClass(main_menu.this, profile.class);
                startActivity(intento);
            }
        });
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

    public void initimgbtn() {
        ImageButton imbtn1 = (ImageButton) findViewById(R.id.imageButton);
        imbtn1.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent();
                intento.setClass(main_menu.this, store_menu.class);
                startActivity(intento);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.question_btn) {
            // 說明dialog
            new AlertDialog.Builder(main_menu.this)
                // 標題
                .setTitle(R.string.explanation)
                // 訊息
                .setMessage(R.string.explanation_content)
                // 按下ok返回畫面
                .setPositiveButton(R.string.ok, null)
                .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food) {
            // Handle the camera action
        } else if (id == R.id.nav_record) {

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