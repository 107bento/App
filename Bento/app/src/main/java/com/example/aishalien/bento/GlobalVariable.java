package com.example.aishalien.bento;

import android.app.Application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by User on 2017/11/16.
 */

public class GlobalVariable extends Application {
    public String UserCookie = "";
    public int total=0;
    public JsonArray shopping_cart;
    public JsonObject cart_item = new JsonObject();
    public JsonArray details= new JsonArray();
    //修改 變數字串
    public void setCookie(String UserCookie){
        this.UserCookie = UserCookie;
    }
    //顯示 變數字串
    public String getCookie() {
        return UserCookie;
    }
    public void addCart(String meal_id,int amount,int subtotal,String wish_id_1,String wish_id_2,String wish_id_3,int random_pic){
        JsonObject tmpcart = new JsonObject();
        total = total+subtotal;
        //放入details內
        //放入內部jsonobject 內
        tmpcart.addProperty("meal_id",meal_id);
        tmpcart.addProperty("amount",amount);
        tmpcart.addProperty("subtotal",subtotal);
        tmpcart.addProperty("wish_id_1",wish_id_1);
        tmpcart.addProperty("wish_id_2",wish_id_2);
        tmpcart.addProperty("wish_id_3",wish_id_3);
        tmpcart.addProperty("random_pic",random_pic);
        details.add(tmpcart);
    }
    public JsonArray remCart(){
        //先放入最外層的cart
        cart_item.addProperty("total",total);
        //放入array中
        shopping_cart.add(cart_item);
        shopping_cart.add(details);
        return shopping_cart;
    }
    public void editCart(){

    }
}
