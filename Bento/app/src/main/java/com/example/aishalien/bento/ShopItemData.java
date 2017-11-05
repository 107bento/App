package com.example.aishalien.bento;

/**
 *  店家資料設定
 */

public class ShopItemData {

    private final String title; //店名
    int imgId; // 店家圖片
    int colorball; // 訂購人數顏色提示球

    public ShopItemData(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public String getTitle(){
        return title;
    }
}
