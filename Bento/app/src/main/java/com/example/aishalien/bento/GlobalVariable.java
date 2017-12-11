package com.example.aishalien.bento;

import android.app.Application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Created by User on 2017/11/16.
 */

public class GlobalVariable extends Application {
    public String username;
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
    public void setUsename(String username) {
        this.username = username;
    }
    //顯示 變數字串
    public String getCookie() {
        return UserCookie;
    }
    public String getUsername(){
        return username;
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
        tmpcart.addProperty("random_pick",random_pic);
        details.add(tmpcart);

    }
    public void addInfo(String store_name,String store_name_id,int amount,String meal_name,String swish_id1,String swish_id2,String swish_id3,int meal_value){
        JsonObject ch_tmpcart = new JsonObject();
        ch_tmpcart.addProperty("store_name",store_name);
        ch_tmpcart.addProperty("store_id",store_name_id);
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
    //清空購物車
    public void clean_cart(){
        total = 0;
        details = new JsonArray();
        ch_details = new JsonArray();
    }
    //送出購物車
    public JsonObject sendCart(){
        //先放入最外層的cart
        cart_item.addProperty("total",total);
        //放入array中
        cart_item.add("details",details);
        return cart_item;
    }
    //刪除某項購物車項目
    public void remCart(int index){
        total = total-Integer.valueOf(details.get(index).getAsJsonObject().get("subtotal").getAsString());
        details.remove(index);
        ch_details.remove(index);
    }
    public void editCart(int index,int orin,String meal_id,int subtotal,int amount,String wish_id_1,String wish_id_2,String wish_id_3,int random_pic){
        JsonObject tmp = new JsonObject();
        total = total-orin+subtotal;
        //放入details內
        //放入內部jsonobject 內
        tmp.addProperty("meal_id",meal_id);
        tmp.addProperty("amount",amount);
        tmp.addProperty("subtotal",subtotal);
        tmp.addProperty("wish_id_1",wish_id_1);
        tmp.addProperty("wish_id_2",wish_id_2);
        tmp.addProperty("wish_id_3",wish_id_3);
        tmp.addProperty("random_pick",random_pic);
        details.set(index,tmp);

    }
    public void editInfo(int index,String store_name,String store_name_id,int amount,String meal_name,String swish_id1,String swish_id2,String swish_id3,int meal_value){
        JsonObject ch_tmpcart = new JsonObject();
        ch_tmpcart.addProperty("store_name",store_name);
        ch_tmpcart.addProperty("store_id",store_name_id);
        ch_tmpcart.addProperty("amount", ( Integer.toString(amount)));
        ch_tmpcart.addProperty("meal_name",meal_name);
        ch_tmpcart.addProperty("meal_value",meal_value);
        ch_tmpcart.addProperty("swish_id1",swish_id1);
        ch_tmpcart.addProperty("swish_id2",swish_id2);
        ch_tmpcart.addProperty("swish_id3",swish_id3);
        //ch_details.remove(index);
        ch_details.set(index,ch_tmpcart);//最後要拿出來給shopping_cart show listview用的
    }
}
