package com.example.a87939.myapplication;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Mm extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mm);
        out = (TextView) findViewById(R.id.txtout);
        edit = (EditText) findViewById(R.id.inp);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Log.i("mm","onClick msg....");
        String str_data = edit.getText().toString();
        if("".equals(str_data)){
            str_data = "0";
        }
        double data=Double.parseDouble(str_data);
        double d2=data*33.81231221321;
        String d3=String.format("%.2f", d2);
        out.setText("华氏度是："+d3);
    }
}
