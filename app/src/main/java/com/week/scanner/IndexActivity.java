package com.week.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.gson.Gson;
import com.week.bean.request.ReqGetShop;
import com.week.bean.response.ResGetShop;
import com.week.bean.response.ResLogin;
import com.week.scanner.utills.SpTools;
import com.week.scanner.utills.SpTools;
import com.week.scanner.utills.StreamTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IndexActivity extends AppCompatActivity {
    protected  final int FAILURE = 2;
    protected  final int SUCCESS = 1;  //ctrl + shift + X  Y
    protected  final int ERROR = 0;
    //在主线程中定义一个handler
    private Handler handler = new Handler(){
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            Intent intent;
            //[1]区分一下发送的是哪条消息
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    String content =  (String) msg.obj;
                    intent= new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                    IndexActivity.this.finish();//引导页不可在再次返回
                    break;
                case FAILURE:   //代表请求失败
                    content =  (String) msg.obj;
                    intent= new Intent(IndexActivity.this, LoginActivity.class);
                    startActivity(intent);
                    IndexActivity.this.finish();//引导页不可再次返回
                    break;
                case ERROR:   //代表请求失败,可能是网络上的一些问题，那么不妨先跳入主页面
                    content =  (String) msg.obj;
                    intent= new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_LONG).show();
                    IndexActivity.this.finish();//引导页不可再次返回
                    break;
                default:
                    break;
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_index);
        //SpTools.Set(this,"goodsList","goodsList");
        String token=SpTools.Get(this,"token");
        if(token.equals("token")){
            Intent intent= new Intent(IndexActivity.this, LoginActivity.class);
            Toast.makeText(getApplicationContext(), "token失效，请重新登录", Toast.LENGTH_LONG).show();
            startActivity(intent);
            IndexActivity.this.finish();//引导页不可再次返回
        }else{
            postDataWithParame(token);
        }

    }

    private void postDataWithParame(String token) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        ReqGetShop reqGetShop=new ReqGetShop(token);
        Gson gson = new Gson();
        String data = gson.toJson(reqGetShop);
        formBody.add("data", data);//传递键值对参数
        String URL=getString(R.string.URL);
        Request request = new Request.Builder()//创建Request 对象。
                .url(URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = ERROR;
                msg.obj="异常";
                //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    //[6]把流转换成字符串
                    String content =response.body().string();
                    Gson gson = new Gson();
                    ResGetShop resGetShop=gson.fromJson(content, ResGetShop.class);
                    if(resGetShop.getCode()==200){
                        //[7]展示结果
                        //2.9.0 创建message对象
                        @SuppressLint("WrongConstant") SharedPreferences sp = getSharedPreferences("User", Context.MODE_APPEND);
                        SharedPreferences.Editor edit = sp.edit();
                        //通过editor对象写入数据
                        edit.putString("ResGetShop",content);
                        String selectShop=sp.getString("SelectShop", "SelectShop");
                        if(resGetShop.getData().getShopList().size()>0&&selectShop.equals("SelectShop")){
                            edit.putString("SelectGetShop",resGetShop.getData().getShopList().get(0).getShopId());//初始化第一个为选中的
                            edit.putString("SelectGetShopName",resGetShop.getData().getShopList().get(0).getShopName());//初始化第一个为选中的
                        }
                        //提交数据存入到xml文件中
                        edit.commit();
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);

                    }else{
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);
                    }

                }else{
                    String content =response.body().string();
                    //[7]展示结果
                    //2.9.0 创建message对象
                    Message msg = new Message();
                    msg.what = ERROR;
                    msg.obj = code+"";
                    //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                    handler.sendMessage(msg);
                }
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    public void showToast(final String content) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //该方法一定是执行主线程
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
            }
        });

    }


}
