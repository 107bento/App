package com.example.aishalien.bento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class purchase_record_order_detail extends AppCompatActivity {

    Integer mealPic;
    String  store, meal, counts, status, sstore_1, sstore_2, sstore_3, sswish_1, sswish_2, sswish_3;
    TextView storeText, mealText, countsText, statusText,swish_1,swish_2,swish_3, wish_1, wish_2, wish_3;
    ImageView mealImg;
    String[] codeStatus =  {"訂單確認中，未銷帳","已成單，待領取", "已領取", "流單"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ApplicationBar.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_purchase_record_order_detail);

    /*
        *拿取資料
        */
    Bundle bundle = getIntent().getExtras();
    String jsonResource = bundle.getString("jsonObj");
    //將字串轉回Json
    JsonObject JsonObj = new JsonParser().parse(jsonResource).getAsJsonObject();

        store = JsonObj.get("meal").getAsJsonObject().get("shop_name").getAsString();
        meal = JsonObj.get("meal").getAsJsonObject().get("meal_name").getAsString();
        mealPic = setpic(JsonObj);
        counts =  JsonObj.get("amount").getAsString();
        //配合陣列位置getAsInt()-1
        int tmpcode = JsonObj.get("state").getAsInt()-1;
        status = codeStatus[tmpcode];
        sstore_1= JsonObj.get("wish_1").getAsJsonObject().get("shop_name").getAsString();
        sstore_2= JsonObj.get("wish_2").getAsJsonObject().get("shop_name").getAsString();
        sstore_3= JsonObj.get("wish_3").getAsJsonObject().get("shop_name").getAsString();
        sswish_1= JsonObj.get("wish_1").getAsJsonObject().get("meal_name").getAsString();
        sswish_2 = JsonObj.get("wish_2").getAsJsonObject().get("meal_name").getAsString();
        sswish_3 = JsonObj.get("wish_3").getAsJsonObject().get("meal_name").getAsString();


        mealImg = (ImageView) findViewById(R.id.purchase_record_detail_img);
        storeText = (TextView) findViewById(R.id.purchase_record_detail_store);
        mealText = (TextView) findViewById(R.id.purchase_record_detail_meal);
        countsText = (TextView) findViewById(R.id.purchase_record_detail_count);
        statusText = (TextView) findViewById(R.id.purchase_record_detail_status);
        swish_1=(TextView) findViewById(R.id.purchase_record_detail_application_1_shop);
        swish_2=(TextView) findViewById(R.id.purchase_record_detail_application_2_shop);
        swish_3=(TextView) findViewById(R.id.purchase_record_detail_application_3_shop);
        wish_1=(TextView) findViewById(R.id.purchase_record_detail_application_1_meal);
        wish_2=(TextView) findViewById(R.id.purchase_record_detail_application_2_meal);
        wish_3=(TextView) findViewById(R.id.purchase_record_detail_application_3_meal);


        // 設定頁面資料
        mealImg.setImageResource(mealPic);
        storeText.setText(store);
        mealText.setText(meal);
        countsText.setText("x" + counts);
        statusText.setText(status);
        swish_1.setText(sstore_1);
        swish_2.setText(sstore_2);
        swish_3.setText(sstore_3);
        wish_1.setText(sswish_1);
        wish_2.setText(sswish_2);
        wish_3.setText(sswish_3);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch(id)
        {
            case android.R.id.home: // 按了 Action Bar 的返回鍵
                onBackPressed();
                return true;    // 注意! 一定要回傳 true
        }
        return super.onOptionsItemSelected(item);
    }
    /*處理圖片*/
    private int setpic(JsonObject tmp){
        String shop_id = tmp.get("meal").getAsJsonObject().get("shop_id").getAsString();
        String meal_id = tmp.get("meal").getAsJsonObject().get("meal_id").getAsString();
        String picName =   "store"+String.valueOf(Integer.valueOf(shop_id)-1)+"_"+meal_id;
        int picId = getResources().getIdentifier(picName, "drawable", getPackageName());
        return picId;
    }
}
