package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class shopping_cart extends AppCompatActivity {
    // 目標放入的MAP
    List<Map<String,Object>> mList;
    ListView cartListView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        cartListView=(ListView)findViewById(R.id.activity_shopping_cart);
        mList =  new ArrayList<>();
        String[] listFromResource = getResources().getStringArray(R.array.list_shopping_cart);
        String[] listFromMeal = getResources().getStringArray(R.array.list_shopping_cart_meal);
        String[] listFromNum = getResources().getStringArray(R.array.list_shopping_cart_num);

        Map<String, Object> item ;
        for(int i=0; i<listFromResource.length;i++) {
            item = new HashMap<String, Object>();
            item.put("store_name",listFromResource[i]);
            item.put("store_pic", R.drawable.store2_1);
            item.put("meal_name",listFromMeal[i]);
            item.put("meal_num",listFromNum[i]);
            mList.add(item);
        }
        //執行環境 this,帶入資料,layout位置,data 帶入的key,key值帶入哪個元件
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mList,
                R.layout.list_item_pictext,
                new String[]{"store_name", "store_pic","meal_name","meal_num"},
                new int[]{R.id.store_name,R.id.store_pic,R.id.meal_name,R.id.meal_num});

        cartListView.setAdapter(adapter);
        //添加點擊事件
        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //获得选中项的HashMap对象
                HashMap<String,Object> map=(HashMap<String,Object>)cartListView.getItemAtPosition(arg2);
                Object store_name = map.get("store_name");
                Object meal_name = map.get("meal_name");
                Toast.makeText(getApplicationContext(), "你選擇的是" + meal_name, Toast.LENGTH_SHORT).show();

                if(meal_name.equals("壽司")) {
                    // 建立一個Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("meal", (String) meal_name);
                    Intent intento = new Intent();
                    intento.setClass(shopping_cart.this, shopping_cart_modify.class);
                    // 將bundle傳入
                    intento.putExtras(bundle);
                    startActivity(intento);
                }
            }
        });

        //設置listview高度
        setListViewHeightBasedOnChildren(cartListView);


        //  toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
        mtoolbar.setTitle(R.string.shop_cart);
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);
//        // 設置返回按鍵作用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置返回键的点击事件：
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     * 解決listview與scrollview衝突而無法設定以其內容作為其高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

            // 按下首頁
            case R.id.goto_main:
                Intent intento = new Intent();
                intento.setClass(shopping_cart.this, main_menu.class);
                startActivity(intento);
                break;

            // 按下說明
            case R.id.question_btn:
                // 彈出dialog
                new AlertDialog.Builder(shopping_cart.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }
}
