package com.example.aishalien.bento;

/**
 *  店家資料設定
 */

public class ShopItemData {

    private final String title; //店名
    int imgId; // 店家圖片
    int storeId;//店家ID
    int colorball; // 訂購人數顏色提示球

    public ShopItemData(String title, int imgId,int storeId) {
        this.title = title;
        this.imgId = imgId;
        this.storeId = storeId;
    }

    public String getTitle(){
        return title;
    }
}
