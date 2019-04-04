package com.week.scanner;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.week.bean.base.GoodsList;
import com.week.bean.request.ReqGetGoodsByBarcode;
import com.week.bean.request.ReqGetPrintOrderDetails;
import com.week.bean.request.ReqLogin;
import com.week.bean.request.ReqSetPrint;
import com.week.bean.response.ResGetGoodsByBarcode;
import com.week.bean.response.ResGetPrintOrderDetails;
import com.week.bean.response.ResLogin;
import com.week.bean.response.ResSetPrint;
import com.week.scanner.utills.SpTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
public class ScanFragment extends Fragment {

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
    protected final int FAILURE = 2;
    protected final int SUCCESS = 1;  //ctrl + shift + X  Y
    protected final int ERROR = 0;
    private Button btn_postprint;//提交打印单的按钮
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;
    private List<ResGetGoodsByBarcode.Data> goodsList;
    private ReqSetPrint reqSetPrint;
    private Map<String, Integer> goodsMap;
    private ListView lv;//主要的listView
    private TextView tv_count;//主要的listView
    ProgressDialog progressDialog;//loading
    private String long_bar_code;
    private int long_position;
    private Handler handler = new Handler() {
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    Gson gson = new Gson();
                    SpTools.Set(getActivity(), "goodsList", gson.toJson(goodsList));
                    tv_count.setText("共计" + goodsList.size() + "种。");
                    lv.setAdapter(new MyAdapter());
                    break;
                case FAILURE:   //代表请求失败
                    Toast.makeText(getActivity().getApplicationContext(), "未找到该商品或网络异常", Toast.LENGTH_SHORT).show();
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
    private Handler handler_scoll = new Handler() {
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            int position = (int) msg.obj;
            lv.setAdapter(new MyAdapter());
            lv.smoothScrollToPosition(position);

        }

        ;
    };
    private Handler handler_set = new Handler() {
        //这个方法是在主线程里面执行的
        public void handleMessage(android.os.Message msg) {
            //所以就可以在主线程里面更新ui了
            switch (msg.what) {
                case SUCCESS:   //代表请求成功
                    Toast.makeText(getActivity().getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    goodsList.clear();
                    goodsMap.clear();
                    SpTools.Set(getActivity(),"goodsList","goodsList");
                    lv.setAdapter(new MyAdapter());
                    //清理所有
                    break;
                case FAILURE:   //代表请求失败
                    Toast.makeText(getActivity().getApplicationContext(), "未找到该商品或网络异常", Toast.LENGTH_SHORT).show();
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
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barcode, 0, barcodelen);
            scan(barcodeStr);
        }

    };

    public ScanFragment() {
        // Required empty public constructor
    }

    public void initListView() {

    }


    private void scan(String barcodeStr) {
        progressDialog = ProgressDialog.show(getActivity(), "", "获取商品中...");
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。
        final ReqGetGoodsByBarcode reqGetGoodsByBarcode = new ReqGetGoodsByBarcode(SpTools.Get(getActivity(), "token"));
        reqGetGoodsByBarcode.getParams().setBarCode(barcodeStr);
        reqGetGoodsByBarcode.getParams().setShopId(SpTools.Get(getActivity(), "SelectShop"));
        Gson gson = new Gson();
        String data = gson.toJson(reqGetGoodsByBarcode);
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
                    ResGetGoodsByBarcode resGetGoodsByBarcode = gson.fromJson(content, ResGetGoodsByBarcode.class);
                    if (resGetGoodsByBarcode.getCode() == 200) {
                        ResGetGoodsByBarcode.Data good = resGetGoodsByBarcode.getData();
                        good.setNum(1);
                        if (!goodsMap.containsKey(good.getBarCode())) {
                            goodsList.add(good);
                            goodsMap.put(good.getBarCode(), goodsList.size() - 1);
                        }
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = resGetGoodsByBarcode.getMessage();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        goodsList = new ArrayList<ResGetGoodsByBarcode.Data>();
        goodsMap = new HashMap<String, Integer>();
        String list_str = SpTools.Get(getActivity(), "goodsList");
        if (!list_str.equals("goodsList")) {//将之前保存过的list取出来，并且初始化
            Gson gson = new Gson();
            ResGetGoodsByBarcode.Data[] goodsArray = gson.fromJson(list_str, ResGetGoodsByBarcode.Data[].class);
            goodsList = new ArrayList<>(Arrays.asList(goodsArray));
            for (int i = 0; i < goodsList.size(); i++) {
                if (!goodsMap.containsKey(goodsList.get(i).getBarCode())) {
                    goodsMap.put(goodsList.get(i).getBarCode(), i);
                }
            }
        }
        btn_postprint =view.findViewById(R.id.btn_postprint);
        btn_postprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("备注")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("说点什么", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                        setPrint(input.toString());
                            }
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
            }
        });
        tv_count = view.findViewById(R.id.tv_count);
        tv_count.setText("共计" + goodsList.size() + "种。");
        lv = view.findViewById(R.id.lv_scan);
        lv.setAdapter(new MyAdapter());
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long_bar_code =goodsList.get(position).getBarCode();
                long_position=position;
                new MaterialDialog.Builder(getActivity())
                        .title("是否删除？")
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                goodsList.remove(long_position);
                                goodsMap.remove(long_bar_code);
                                Gson gson = new Gson();
                                SpTools.Set(getActivity(), "goodsList", gson.toJson(goodsList));
                                tv_count.setText("共计" + goodsList.size() + "种。");
                                lv.setAdapter(new MyAdapter());
                            }
                        })

                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                // TODO
                            }
                        })
                        .show();



                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getActivity(), GoodEditActivity.class);
                intent.putExtra("com.week.scanner.position", position);
                intent.putExtra("com.week.scanner.num", goodsList.get(position).getNum());
                intent.putExtra("com.week.scanner.total", goodsList.get(position).getTotal());
                startActivityForResult(intent,100);

            }
        });
        return view;
    }
    public void setPrint(String input){
        progressDialog = ProgressDialog.show(getActivity(), "", "获取打印单详情中...");
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();//创建OkHttpClient对象。
        ReqSetPrint reqSetPrint = new ReqSetPrint(SpTools.Get(getActivity(), "token"));
        reqSetPrint.getParams().getPrintOrder().setMark(input);
        reqSetPrint.getParams().getPrintOrder().setShopId(SpTools.Get(getActivity(),"SelectShop"));
        for(int i=0;i<goodsList.size();i++){
            GoodsList good=new GoodsList();
            good.setBarcode(goodsList.get(i).getBarCode());
            good.setGoodsName(goodsList.get(i).getGoodsName());
            good.setTotal(goodsList.get(i).getTotal());
            reqSetPrint.getParams().getPrintOrder().getGoodsList().add(good);
        }
        Gson gson = new Gson();
        String data = gson.toJson(reqSetPrint);
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
                    ResSetPrint resSetPrint = gson.fromJson(content, ResSetPrint.class);
                    if (resSetPrint.getCode() == 200) {
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        msg.obj = content;
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler_set.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj = resSetPrint.getMessage();
                        //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                        handler_set.sendMessage(msg);
                    }
                } else {
                    String content = response.body().string();
                    //[7]展示结果
                    //2.9.0 创建message对象
                    Message msg = new Message();
                    msg.what = ERROR;
                    msg.obj = code + "";
                    //2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
                    handler_set.sendMessage(msg);
                }
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    int position =data.getIntExtra("com.week.scanner.position",0);
                    int num =data.getIntExtra("com.week.scanner.num",1);
                    goodsList.get(position).setNum(num);
                    lv.setAdapter(new MyAdapter());
                    lv.smoothScrollToPosition(position);
                    return;
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initScan();
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        getActivity().registerReceiver(mScanReceiver, filter);
    }

    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        getActivity().unregisterReceiver(mScanReceiver);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return goodsList.size();
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
                view = View.inflate(getActivity().getApplicationContext(), R.layout.layout_goods_item,
                        null);
            } else {
                view = convertView;

            }
            // [1]找到控件 显示集合里面的数据
            TextView tv_goodsName = (TextView) view.findViewById(R.id.txt_goodsname);
            TextView tv_code = (TextView) view.findViewById(R.id.txt_code);
            TextView tv_total = (TextView) view.findViewById(R.id.txt_total);
            tv_goodsName.setText(goodsList.get(position).getGoodsName());
            tv_code.setText(goodsList.get(position).getBarCode());
            tv_total.setText("X" + goodsList.get(position).getNum());
            return view;
        }

    }


}
