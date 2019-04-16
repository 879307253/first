package com.example.a87939.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends AppCompatActivity implements Runnable{


private final String TAG ="Rate";
private float dollarRate=0.1f;
private float euroRate=0.2f;
private float wonRate=0.3f;
    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showOut);
        //获取so里保存的数据
        SharedPreferences sharedPreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        //一个app建议一个配置文件，第二个方法名称不能改，少量数据可以用配置文件，其他的放在其他文件
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        Log.i(TAG,"onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG,"onCreate: sp euroRate=" + euroRate);
        Log.i(TAG,"onCreate: sp wonRate=" + wonRate);
        //开启子线程,当前对象不要忘掉，不然不能执行run
        Thread t = new Thread(this);
        t.start();
        //直接写父类改写,等价于单独创建类，不写handler就空的
         handler = new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 if (msg.what == 5) {
                     String str = (String) msg.obj;
                     Log.i(TAG, "handleMessage: getMessage msg =" + str);
                     show.setText(str);
                 }
                 super.handleMessage(msg);
             }
         };
         //父类要；
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

    public boolean onOptionsItemSelected(MenuItem item) {
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
            //将设置的汇率写到sp里
           SharedPreferences sharedPreferences=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
           SharedPreferences.Editor editor=sharedPreferences.edit();
           editor.putFloat("dollar_rate",dollarRate);
           editor.putFloat("euro_rate",euroRate);
           editor.putFloat("won_rate",wonRate);
           editor.commit();
           Log.i(TAG,"onActivityResult:数据以保存到sharedPreferences");
       }
       super.onActivityResult(requestCode,resultCode,data);
    }
    public void run(){
        Log.i(TAG,"run: run()......");
        for(int i =1;i<3;i++){
            Log.i(TAG,"run: i=" +i);
            try{
                //当前停止两秒钟,2000毫秒
                Thread.sleep(2000);

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        //获取Msg对象,用于返回主线程
        Message msg = handler.obtainMessage(5);
        //what数据是整数，用于标记当前messa属性，便于接受信息进行比对,可把what放在上面写
        //msg.what = 5;
        msg.obj="Hello from run()";
        handler.sendMessage(msg);


        //获取网络数据
        URL url=null;
        try {
             url = new URL("https://www.currencydo.com/bank_zg/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
             //需要进行转换，返回一个输入流
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG,"run:html="+html );
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
