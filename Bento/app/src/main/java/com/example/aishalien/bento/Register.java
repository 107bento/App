package com.example.aishalien.bento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class Register extends AppCompatActivity implements Validator.ValidationListener {

    JsonObject jsonToPost;

    boolean flag = false;
    Validator validator;

    @NotEmpty(message = "帳號不能為空")
    @Order(1)
    private EditText mUsername;

    @NotEmpty(sequence = 1, message = "密碼不能為空")
    @Password(sequence = 2, min = 6, message = "密碼不能少於6位")
    @Order(2)
    private EditText mPassward;

    @NotEmpty(message = "名稱不能為空")
    @Order(3)
    private EditText mName;

    @NotEmpty(sequence = 1, message = "電話號碼不能為空")
    @Length(sequence = 2, min = 10, message = "電話號碼錯誤") //可以自定義規則 @AssertTrue
    @Order(4)
    private EditText mPhone;

    @NotEmpty(sequence = 1, message = "電子郵件不能為空")
    @Email(sequence = 2, message = "Email地址錯誤")
    @Order(5)
    private EditText mEmail;

    // 驗證通過
    @Override
    public void onValidationSucceeded() {
        flag = true;
    }
    // 驗證失敗
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            ((EditText)error.getView())
                    .setError(error.getFailedRules().get(0)
                            .getMessage((Register.this))
                    );
        }
    }

    /*跟API架接的架構 使用GET後面加上base url後的路徑*/
    public interface Api{
        @POST("user")
            // Call內部為接的資料格式  以及參數Path
        Call<JsonObject> postUser(@Body JsonObject body);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 元件
        getEditText();

        // 表單驗證
        validator = new Validator(this);
        validator.setValidationListener(this);
        // 按照順序不符合規則的
        validator.setValidationMode(Validator.Mode.BURST);

        // 取消
        Button cancel = (Button)findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 返回
                onBackPressed();
            }
        });

        // 註冊
        Button register = (Button)findViewById(R.id.to_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // 驗證資料
                validator.validate();

                try {
                    // post 註冊資料
                    postJson();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postJson() throws IOException {
        /*創建一個retrofit*/
        /*OKHTTP*/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        //建立retrofit網路接口
        //設定base url
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://163.22.17.227/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        //接API
        Register.Api api = retrofit.create(Register.Api.class);

        // 先看資料有沒有驗證過了
        if ( flag ) {

            // 要post的資料
            jsonToPost = new JsonObject();
            jsonToPost.addProperty("username", mUsername.getEditableText().toString());
            jsonToPost.addProperty("password", mPassward.getEditableText().toString());
            jsonToPost.addProperty("name", mName.getEditableText().toString());
            jsonToPost.addProperty("phone", mPhone.getEditableText().toString());
            jsonToPost.addProperty("email", mEmail.getEditableText().toString());


            Call<JsonObject> Model = api.postUser(jsonToPost);
            //使用方法為異步 並且呼叫
            Model.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    //成功接到 response

                    if (response.code() == 200) {
                        Toast.makeText(Register.this, "註冊成功！", Toast.LENGTH_SHORT).show();
                        // 返回
                        onBackPressed();
                    } else if (response.code() == 400) {
                        Toast.makeText(Register.this, "使用者已存在", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("Error", t.getLocalizedMessage().toString());
                }
            });
        }
    }

    private void getEditText() {
        // 找到元件
        mUsername = (EditText) findViewById(R.id.register_account);
        mPassward = (EditText) findViewById(R.id.register_password);
        mName = (EditText) findViewById(R.id.register_name);
        mPhone = (EditText) findViewById(R.id.register_phone);
        mEmail = (EditText) findViewById(R.id.register_email);
    }
}