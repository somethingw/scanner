package com.week.scanner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.week.bean.request.ReqGetPrintOrderDetails;
import com.week.bean.request.ReqGetPrintOrderList;
import com.week.bean.response.ResGetPrintOrderDetails;
import com.week.bean.response.ResGetPrintOrderList;
import com.week.scanner.utills.SpTools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsDetailActivity extends AppCompatActivity {

    protected final int FAILURE = 2;
    protected final int SUCCESS = 1;  //ctrl + shift + X  Y
    protected final int ERROR = 0;
    private ResGetPrintOrderDetails resGetPrintOrderDetails;
    private ListView lv;
    private TextView tv_orderid;
    private TextView tv_shop_nme;
    private TextView tv_cretetime;
    private TextView tv_print_time;
    private TextView tv_print_status;
    String[] status_str=new String[]{"未打印","已打印","异常"};
    ProgressDialog progressDialog;//loading
    String orderid;
    private Handler handler = new Handler() {
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    tv_orderid.setText("订单编号："+resGetPrintOrderDetails.getData().getPrintOrderDetails().getPrintOrderId());
                    tv_shop_nme.setText("门店名称："+resGetPrintOrderDetails.getData().getPrintOrderDetails().getShopName());
                    tv_cretetime.setText("创建时间："+resGetPrintOrderDetails.getData().getPrintOrderDetails().getCreateTime());
                    tv_print_time.setText("打印时间"+resGetPrintOrderDetails.getData().getPrintOrderDetails().getPrintTime());
                    int status=resGetPrintOrderDetails.getData().getPrintOrderDetails().getStatus();
                    tv_print_status.setText("打印状态："+status_str[status]);
                    lv.setAdapter(new MyAdapter());
                    break;
                case FAILURE:   //代表请求失败
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:   //代表请求失败,可能是网络上的一些问题，那么不妨先跳入主页面
                    Toast.makeText(getApplicationContext(), "登录失效，请重新登录", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        Intent intent=getIntent();
        lv=findViewById(R.id.lv_detail_goods_list);
        tv_orderid=findViewById(R.id.tv_order_ID);
        tv_shop_nme=findViewById(R.id.tv_shop_nme);
        tv_cretetime=findViewById(R.id.tv_cretetime);
        tv_print_time=findViewById(R.id.tv_print_time);
        tv_print_status=findViewById(R.id.tv_print_status);
        orderid =(String) intent.getStringExtra("com.week.scanner.orderid");
        getDetail();
    }
    private void getDetail(){
        progressDialog = ProgressDialog.show(this, "", "获取打印单详情中...");
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。
        final ReqGetPrintOrderDetails reqGetPrintOrderDetails = new ReqGetPrintOrderDetails(SpTools.Get(this, "token"));
        Gson gson = new Gson();
        String data = gson.toJson(reqGetPrintOrderDetails);
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
                progressDialog.dismiss();
                Message msg = new Message();
                msg.what = ERROR;
                msg.obj = "异常";
                //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                handler.sendMessage(msg);
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                int code = response.code();
                if (code == 200) {
                    //[6]把流转换成字符串
                    String content = response.body().string();
                    Gson gson = new Gson();
                    ResGetPrintOrderDetails OrderDetails = gson.fromJson(content, ResGetPrintOrderDetails.class);
                    if (OrderDetails.getCode() == 200) {
                        resGetPrintOrderDetails=OrderDetails;
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = OrderDetails.getMessage();
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);
                    }
                } else {
                    String content = response.body().string();
                    //[7]展示结果
                    //2.9.0 创建message对象
                    Message msg = new Message();
                    msg.what = ERROR;
                    msg.obj = code + "";
                    //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                    handler.sendMessage(msg);
                }
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }
    //定义数据适配器
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resGetPrintOrderDetails.getData().getPrintOrderDetails().getGoodsList().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.layout_detail_goods_item,
                        null);
            } else {
                view = convertView;

            }
            // [1]找到控件 显示集合里面的数据

            TextView tv_goodsname = (TextView) view.findViewById(R.id.txt_detail_goodsname);
            TextView tv_code = (TextView) view.findViewById(R.id.txt_detail_code);
            TextView tv_total = (TextView) view.findViewById(R.id.txt_detail_total);
            tv_goodsname.setText(resGetPrintOrderDetails.getData().getPrintOrderDetails().getGoodsList().get(position).getGoodsName());
            tv_code.setText(resGetPrintOrderDetails.getData().getPrintOrderDetails().getGoodsList().get(position).getBarcode());
            tv_total.setText("x"+resGetPrintOrderDetails.getData().getPrintOrderDetails().getGoodsList().get(position).getTotal());

            return view;
        }

    }

}
