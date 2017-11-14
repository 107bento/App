package com.example.aishalien.bento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


public class today_meal_detail extends AppCompatActivity {

    Integer mealPic;
    String  store, meal, counts, status;
    TextView storeText, mealText, countsText, statusText;
    ImageView mealImg, qrcodeImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_today_meal_detail);

        /**
         * 接收一般 Map 物件：
         *
        Bundle bundle = getIntent().getExtras();
        today_meal.SerializableMap serializableMap = (today_meal.SerializableMap) bundle.get("map");
         */

        /*
        *  接收 HashMap 物件
        */
        HashMap<String, Object> hashMap = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hashMap = (HashMap<String, Object>) bundle.getSerializable("HashMap");
            mealPic = (Integer) hashMap.get("meal_pic");
            store = (String) hashMap.get("store_name");
            meal = (String) hashMap.get("meal_name");
            counts = (String) hashMap.get("meal_num");
            status = (String) hashMap.get("status");
        }

        mealImg = (ImageView) findViewById(R.id.today_detail_img);
        storeText = (TextView) findViewById(R.id.today_detail_store);
        mealText = (TextView) findViewById(R.id.today_detail_meal);
        countsText = (TextView) findViewById(R.id.today_detail_count);
        statusText = (TextView) findViewById(R.id.today_detail_status);
        qrcodeImg = (ImageView) findViewById(R.id.today_deatil_barcode);

        // 設定頁面資料
        mealImg.setImageResource(mealPic);
        storeText.setText(store);
        mealText.setText(meal);
        countsText.setText(counts);
        statusText.setText(status);

        /**
         * 無效果
         */
        // 設定使用url顯示internet圖片
        // qrcodeImg.setImageBitmap(GetURLBitmap(new URL("http://goqr.me/_Resources/Persistent/c51e9cb94c6c3e953542c764bb97a0e1cf033ca3/qrcode-logo-design-starbucks.png")));

    }

    /**
     * 向 ImageView 指定 http 圖片的方法
     */
    public static Bitmap GetURLBitmap(URL url)
    {
        try
        {
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream isCover = conn.getInputStream();
            Bitmap bmpCover = BitmapFactory.decodeStream(isCover);
            isCover.close();
            return bmpCover;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
