package com.week.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView titleBar;
    private BottomNavigationView bottomNavigationView;
    private  MyFragment myFragment;
    private  ScanFragment scanFragment;
    private ListFragment listFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        titleBar.setText("扫码");
                        lastfragment=0;

                    }
                    return true;
                }
                case R.id.navigation_dashboard:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        titleBar.setText("记录");
                        lastfragment=1;

                    }

                    return true;
                }
                case R.id.navigation_notifications:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        titleBar.setText("门店设置");
                        lastfragment=2;

                    }

                    return true;
                }


            }


            return false;
        }
    };


    private void initFragment()
    {

        scanFragment = new ScanFragment();
        listFragment = new ListFragment();
        myFragment=new MyFragment();
        fragments = new Fragment[]{scanFragment,listFragment,myFragment};
        lastfragment=0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,scanFragment).show(scanFragment).commit();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleBar=findViewById(R.id.titleBar);
        SharedPreferences sp=getSharedPreferences("User", Context.MODE_PRIVATE);
        String token=sp.getString("token", "token");
        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
        initFragment();
        Spinner spinner=findViewById(R.id.spinner_shop);

    }
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.mainview,fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();

    }


}
