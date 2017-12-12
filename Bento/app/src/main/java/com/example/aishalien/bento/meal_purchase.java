package com.example.aishalien.bento;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class meal_purchase extends AppCompatActivity {
    TextView value,num;
    String store;
    String store_id;
    String mMeal;
    String meal_id;
    int meal_value;
    int counter; //記錄數量
    int max = 99; //最大數量
    int min = 1; //最小數量
    int current = 1; //要顯示的值
    private Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_purchase);
        //找到元件
        value= (TextView) findViewById(R.id.meal_purchase_value);
        //num= (TextView) findViewById(R.id.count);
        ImageView img= (ImageView) findViewById(R.id.meal_purchase_img);
        //接收傳過來的東西
        Bundle bundle = getIntent().getExtras();
        value.setText("NT."+bundle.getInt("value"));
        mMeal = bundle.getString("meal");
        store = bundle.getString("store");
        store_id= bundle.getString("store_name_id");
        meal_id = bundle.getString("meal_id");
        meal_value = bundle.getInt("value");
        img.setImageResource(bundle.getInt("pic"));
        initimgbtn();

        // toolbar
        mtoolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        // 設置toolbar標題
        mtoolbar.setTitle(mMeal);
        // 設置標題顏色
        mtoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        // 設置狀態欄透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 設置啟用toolbar
        setSupportActionBar(mtoolbar);


        // 數量控件
        AmountView amountView1 = (AmountView) findViewById(R.id.amountview1);
        amountView1.setMaxValue(max); // 設置最大數量
        amountView1.setMinValue(min); // 設置最小值
        amountView1.setCurrentValue(current); //設置目前要顯示的數量
        amountView1.setListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(int amount) {
                //  紀錄數量
                counter = amount;
            }
        });
    }

    public void initimgbtn(){
        Button btn = (Button) findViewById(R.id.btn_put_in_cart);
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 建立一個Bundle
                Bundle bundle = new Bundle();
                bundle.putString("store",store);
                bundle.putString("store_name_id",store_id);
                bundle.putString("meal",mMeal);
                if (counter == 0) { //沒有按過就用頁面顯示的值
                    counter = current;
                }
                bundle.putInt("amount", counter);
                bundle.putString("meal_id",meal_id);
                bundle.putInt("value",meal_value);
                Intent intento = new Intent();
                intento.setClass(meal_purchase.this, application_fillment.class);
                // 將bundle傳入
                intento.putExtras(bundle);
                startActivityForResult(intento,0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) {
            //-1為呼叫前一頁 刪除他自己
            setResult(-1);
            finish();
            onBackPressed();
        }
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

            // 按下購物車
            case R.id.menuItem_shoppingCart:
                Intent intento = new Intent();
                intento.setClass(meal_purchase.this, shopping_cart.class);
                startActivity(intento);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_normal, menu);
        return true;
    }
}