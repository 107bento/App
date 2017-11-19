package com.example.aishalien.bento;

import android.app.Application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Created by User on 2017/11/16.
 */

public class GlobalVariable extends Application {
    public String UserCookie = "";
    //SharedPreferences cart;
    public int total=0;
    public JsonArray shopping_cart;
    public JsonObject cart_item = new JsonObject();
    //紀錄傳給後端要用的details
    public JsonArray details= new JsonArray();
    //紀錄要給使用者看得deteails
    public JsonArray ch_details= new JsonArray();
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
    public void addInfo(String store_name,String store_name_id,int amount,String meal_name,String swish_id1,String swish_id2,String swish_id3,int meal_value){
        JsonObject ch_tmpcart = new JsonObject();
        ch_tmpcart.addProperty("store_name",store_name);
        ch_tmpcart.addProperty("store_name_id",store_name_id);
        ch_tmpcart.addProperty("amount", ( Integer.toString(amount)));
        ch_tmpcart.addProperty("meal_name",meal_name);
        ch_tmpcart.addProperty("meal_value",meal_value);
        ch_tmpcart.addProperty("swish_id1",swish_id1);
        ch_tmpcart.addProperty("swish_id2",swish_id2);
        ch_tmpcart.addProperty("swish_id3",swish_id3);
        ch_details.add(ch_tmpcart);//最後要拿出來給shopping_cart show listview用的
    }
//    public void storeInPhone(){
//        cart.edit()
//                .putString("cart", cartString)
//                .commit();
//    }
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
