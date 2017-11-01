package com.example.aishalien.bento;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 2017/11/1.
 */

public class LoginError {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("status_code")
    @Expose
    private String statusCode;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
