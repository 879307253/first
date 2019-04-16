package com.example.a87939.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {


private final String TAG ="Rate";
private float dollarRate=0.1f;
private float euroRate=0.1f;
private float wonRate=0.1f;
    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showOut);
    }
    public void onClick(View btn){
        //获取用户输入内容
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        float r =0;
        if(str.length()>0){
         r=Float.parseFloat(str);
        }
        else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, "onClick: r=" + r);
        float val=0;

        if(btn.getId()==R.id.btn_dollar){
            val=r*dollarRate;
        }
        else if(btn.getId()==R.id.btn_euro){
            val=r*euroRate;
        }
        else if(btn.getId()==R.id.btn_won){
            val=r*wonRate;
        }
        String s=String.format("%.2f", val);
        show.setText(s);
    }
    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        Intent config =new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);
        Log.i(TAG,"openOne:dollarRate="+dollarRate);
        Log.i(TAG,"openOne:euroRate="+euroRate);
        Log.i(TAG,"openOne:wonRate="+wonRate);

       // startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    public boolean onCreateOptionsMenu(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==1 && resultCode==2){
           Bundle bundle=data.getExtras();
           dollarRate=bundle.getFloat("key_dollar",0.1f);
           euroRate=bundle.getFloat("key_euro",0.1f);
           wonRate= bundle.getFloat("key_won",0.1f);
           Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
           Log.i(TAG,"onActivityResult:euroRate="+euroRate);
           Log.i(TAG,"onActivityResult:wonRate="+wonRate);

       }
    }
}
