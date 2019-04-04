package com.week.scanner;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.week.bean.response.ResGetShop;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({ "NewApi", "ValidFragment" })
/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment  {

    private Spinner spinner;
    private Button button_yes;

    private ArrayAdapter<String> adapter;//适配器adapter
    private Button button_exit;
    private List<String> list;//数据源
    ResGetShop resGetShop =new ResGetShop();
    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            @SuppressLint("WrongConstant") SharedPreferences sp = MyFragment.this.getActivity().getSharedPreferences("User", Context.MODE_APPEND);
            SharedPreferences.Editor edit = sp.edit();
            //通过editor对象写入数据
            edit.putString("SelectShop",resGetShop.getData().getShopList().get(position).getShopId());
            edit.putString("SelectGetShopName",resGetShop.getData().getShopList().get(position).getShopName());
            edit.commit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,null);
        spinner=view.findViewById(R.id.spinner_shop);
        button_yes=view.findViewById(R.id.button_yes);
        button_exit=view.findViewById(R.id.button_exit);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("WrongConstant") SharedPreferences sp = MyFragment.this.getActivity().getSharedPreferences("User", Context.MODE_APPEND);
                SharedPreferences.Editor edit = sp.edit();
                //通过editor对象写入数据
                edit.putString("SelectShop",resGetShop.getData().getShopList().get(spinner.getSelectedItemPosition()).getShopId());
                edit.putString("SelectGetShopName",resGetShop.getData().getShopList().get(spinner.getSelectedItemPosition()).getShopName());
                edit.commit();
                Toast.makeText(getActivity(), "更改完成", Toast.LENGTH_SHORT).show();
            }
        });
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert=new AlertDialog.Builder(getActivity()).create();

                alert.setTitle("退出？");
                alert.setMessage("真的要退出吗？");
                //添加取消按钮
                alert.setButton(DialogInterface.BUTTON_NEGATIVE,"不",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                //添加"确定"按钮
                alert.setButton(DialogInterface.BUTTON_POSITIVE,"是的", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent= new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();//引导页不可再次返回
                    }
                });
                alert.show();

            }
        });

        //spinner.setOnItemClickListener((AdapterView.OnItemClickListener) listener);

        @SuppressLint("WrongConstant") SharedPreferences sp = this.getActivity().getSharedPreferences("User", Context.MODE_APPEND);
        String selectShop=sp.getString("SelectShop", "SelectShop");
        String selectGetShopName=sp.getString("SelectGetShopName", "SelectGetShopName");
        String resGetShop_str=sp.getString("ResGetShop", "ResGetShop");
        Gson gson = new Gson();
        int n=0;
        list = new ArrayList<String>();



        if(!resGetShop_str.equals("ResGetShop")){
            resGetShop=gson.fromJson(resGetShop_str, ResGetShop.class);
            for(int i=0;i<resGetShop.getData().getShopList().size();i++){
                list.add(resGetShop.getData().getShopList().get(i).getShopName());
                if(resGetShop.getData().getShopList().get(i).getShopId().equals(selectShop)){
                    n=i;
                }
            }
        }
        adapter =  new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,list);
        //设置适配器
        adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(n,true);
        //spinner.setOnItemClickListener((AdapterView.OnItemClickListener) listener);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //spinner.setOnItemClickListener((AdapterView.OnItemClickListener) listener);
    }


}
