package com.week.scanner;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.week.bean.request.ReqGetGoodsByBarcode;
import com.week.bean.request.ReqGetPrintOrderList;
import com.week.bean.response.ResGetGoodsByBarcode;
import com.week.bean.response.ResGetPrintOrderList;
import com.week.scanner.utills.SpTools;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    protected final int FAILURE = 2;
    protected final int SUCCESS = 1;  //ctrl + shift + X  Y
    protected final int ERROR = 0;
    private ResGetPrintOrderList resGetPrintOrderList;
    private ListView lv;
    ProgressDialog progressDialog;//loading
    public ListFragment() {
        // Required empty public constructor
    }
    private Handler handler = new Handler() {
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    lv.setAdapter(new MyAdapter());
                    break;
                case FAILURE:   //代表请求失败
                    Toast.makeText(getActivity().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:   //代表请求失败,可能是网络上的一些问题，那么不妨先跳入主页面
                    Toast.makeText(getActivity().getApplicationContext(), "登录失效，请重新登录", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }


        }

        ;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);

        lv=view.findViewById(R.id.lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("com.week.scanner.orderid", resGetPrintOrderList.getData().getPrintOrderList().get(position).getPrintOrderId());
                startActivity(intent);
            }
        });
        getOrderList();
        return view;
    }

    private void getOrderList(){
        progressDialog = ProgressDialog.show(getActivity(), "", "获取商品中...");
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。
        final ReqGetPrintOrderList reqGetPrintOrderList = new ReqGetPrintOrderList(SpTools.Get(getActivity(), "token"));
        Gson gson = new Gson();
        String data = gson.toJson(reqGetPrintOrderList);
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
                    ResGetPrintOrderList OrderList = gson.fromJson(content, ResGetPrintOrderList.class);
                    if (OrderList.getCode() == 200) {
                        resGetPrintOrderList=OrderList;
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = OrderList.getMessage();
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
            return resGetPrintOrderList.getData().getPrintOrderList().size();
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
                view = View.inflate(getActivity().getApplicationContext(), R.layout.layout_list_item,
                        null);
            } else {
                view = convertView;

            }
            // [1]找到控件 显示集合里面的数据

            TextView tv_item = (TextView) view.findViewById(R.id.txt_orderid);
            TextView tv_time = (TextView) view.findViewById(R.id.txt_time);
            TextView tv_status = (TextView) view.findViewById(R.id.txt_status);
            tv_time.setText(resGetPrintOrderList.getData().getPrintOrderList().get(position).getCreateTime());
            tv_item.setText(resGetPrintOrderList.getData().getPrintOrderList().get(position).getShopName()+"："+resGetPrintOrderList.getData().getPrintOrderList().get(position).getPrintOrderId());
            int status=resGetPrintOrderList.getData().getPrintOrderList().get(position).getStatus();
            if(status==0){
                tv_status.setText("未打印");
                tv_status.setTextColor(getResources().getColor(R.color.orange1));
            }else if(status==1){
                tv_status.setText("已打印");
                tv_status.setTextColor(getResources().getColor(R.color.greeny));
            }else {
                tv_status.setText("异常");
                tv_status.setTextColor(getResources().getColor(R.color.red));
            }
            return view;
        }

    }

}
