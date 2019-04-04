package com.week.scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.week.scanner.utills.AmountView;

public class GoodEditActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private int amount = 1; //购买数量
    private int goods_storage = 50; //商品库存
    private int position=0;

    private TextView text_store;
    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;
    private Button btn_cancel;
    private Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_edit);
        Intent intent=getIntent();
        position =intent.getIntExtra("com.week.scanner.position",1);
        amount =intent.getIntExtra("com.week.scanner.num",1);
        goods_storage=intent.getIntExtra("com.week.scanner.total",1);
        etAmount = (EditText) findViewById(R.id.etAmount);
        text_store=findViewById(R.id.text_store);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_ok=(Button) findViewById(R.id.btn_ok);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        etAmount.addTextChangedListener(this);
        text_store.setText("剩余库存（"+goods_storage+"）");
        etAmount.setText(amount + "");

    }


    public void setGoods_storage(int goods_storage) {
        this.goods_storage = goods_storage;
    }





    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        amount = Integer.valueOf(s.toString());
        if (amount > goods_storage) {
            etAmount.setText(goods_storage + "");
            return;
        }

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > 1) {
                amount--;
                etAmount.setText(amount + "");
            }
        } else if (i == R.id.btnIncrease) {
            if (amount < goods_storage) {
                amount++;
                etAmount.setText(amount + "");
            }
        }else if(i==R.id.btn_ok){
            Intent intent = new Intent();
            intent.putExtra("com.week.scanner.position", position);
            intent.putExtra("com.week.scanner.num", amount);
            setResult(RESULT_OK,intent);
            finish();
        }else if(i==R.id.btn_cancel){
            finish();
        }

        etAmount.clearFocus();
    }
}
