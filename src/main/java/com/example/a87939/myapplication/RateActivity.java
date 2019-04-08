package com.example.a87939.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {
EditText rmb,dol,eur,han;
TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        dol=findViewById(R.id.dol);
        eur=findViewById(R.id.eur);
        han=findViewById(R.id.han);
        show=findViewById(R.id.showOut);
    }
    public void onClick(View btn){
        //获取用户输入内容
        String str = rmb.getText().toString();
        float r =0;
        float d=0,e=0,h=0;
        String str1 = dol.getText().toString();
        String str2 = eur.getText().toString();
        String str3 = han.getText().toString();

        if(str.length()>0){
         r=Float.parseFloat(str);
        }
        else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_LONG).show();
        }
        if(str1.length()>0){
            d=Float.parseFloat(str1);
        }
        else{
            Toast.makeText(this,"请输入汇率",Toast.LENGTH_LONG).show();
        }
        if(str2.length()>0){
            e=Float.parseFloat(str2);
        }
        else{
            Toast.makeText(this,"请输入汇率",Toast.LENGTH_LONG).show();
        }
        if(str3.length()>0){
            h=Float.parseFloat(str3);
        }
        else{
            Toast.makeText(this,"请输入汇率",Toast.LENGTH_LONG).show();
        }
        float val=0;

        if(btn.getId()==R.id.btn_dollar){
            val=r*d;
        }
        else if(btn.getId()==R.id.btn_euro){
            val=r*e;
        }
        else if(btn.getId()==R.id.btn_won){
            val=r*h;
        }
        String s=String.format("%.2f", val);
        show.setText(s)；
    }
}
