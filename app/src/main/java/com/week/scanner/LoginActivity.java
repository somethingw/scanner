package com.week.scanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.week.bean.request.ReqLogin;
import com.week.bean.response.ResLogin;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    protected  final int FAILURE = 2;
    protected  final int SUCCESS = 1;  //ctrl + shift + X  Y
    protected  final int ERROR = 0;

    private SharedPreferences sp ;//本地缓存
    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Handler handler = new Handler(){
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            Intent intent;
            View focusView = null;
            //[1]区分一下发送的是哪条消息
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    String content =  (String) msg.obj;
                    Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                    intent= new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case FAILURE:   //代表请求失败
                    content =  (String) msg.obj;
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    focusView = mPasswordView;
                    break;
                case ERROR:   //代表请求失败
                    content =  (String) msg.obj;
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    focusView = mPasswordView;
                    break;
                default:
                    break;
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView=findViewById(R.id.login_form);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }
    //登录函数
    @SuppressLint("WrongConstant")
    private void attemptLogin() {

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            sp = getSharedPreferences("User", Context.MODE_APPEND);
            SharedPreferences.Editor edit = sp.edit();
            //通过editor对象写入数据
            edit.putString("username",username);
            edit.putString("password",password);
            //提交数据存入到xml文件中
            edit.commit();

            login(username,password);

        }
    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 登录
     * the user.
     */
    private void login(String username,String password) {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。
        ReqLogin reqLogin=new ReqLogin(username,password);
        Gson gson = new Gson();
        String data = gson.toJson(reqLogin);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("data", data);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(getString(R.string.URL))
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

            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    //[6]把流转换成字符串
                    String content =response.body().string();
                    Gson gson = new Gson();
                    ResLogin resLogin=gson.fromJson(content, ResLogin.class);
                    if(resLogin.getCode()==200){
                        sp = getSharedPreferences("User", Context.MODE_APPEND);
                        SharedPreferences.Editor edit = sp.edit();
                        //通过editor对象写入数据
                        edit.putString("token",resLogin.getToken());
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
                        msg.obj = resLogin.getMessage();
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

}

