package com.example.aishalien.bento;

import android.app.Application;

/**
 * Created by User on 2017/11/16.
 */

public class GlobalVariable extends Application {
    public String UserCookie = "";
    //修改 變數字串
    public void setCookie(String UserCookie){
        this.UserCookie = UserCookie;
    }
    //顯示 變數字串
    public String getCookie() {
        return UserCookie;
    }
}
