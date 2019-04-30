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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{


private final String TAG ="Rate";
private float dollarRate=0.1f;
private float euroRate=0.2f;
private String updateData="";
private float wonRate=0.3f;
    EditText rmb;
    TextView show;
    Handler handler;
    private String str1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showOut);
        //获取so里保存的数据
        final SharedPreferences sharedPreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        //一个app建议一个配置文件，第二个方法名称不能改，少量数据可以用配置文件，其他的放在其他文件
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateData = sharedPreferences.getString("update_data","");
          //创建一个当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        final String todayStr=sdf.format(today);
        Log.i(TAG,"onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG,"onCreate: sp euroRate=" + euroRate);
        Log.i(TAG,"onCreate: sp wonRate=" + wonRate);
        Log.i(TAG,"onCreate: sp updateDate=" + updateData);
        Log.i(TAG,"onCreate:todayStr=" + todayStr);
        //判断时间
        if(todayStr.equals(updateData)){
            Log.i(TAG,"onCreate=需要更新");
            //开启子线程
            Thread t =new Thread(this);
            t.start();
        }
        else{
            Log.i(TAG,"onCreate 不需要更新");
        }
        //开启子线程,当前对象不要忘掉，不然不能执行run
        Thread t = new Thread(this);
        t.start();
        //直接写父类改写,等价于单独创建类，不写handler就空的
         handler = new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 if (msg.what == 5) {
                     Bundle bdl= (Bundle) msg.obj;
                     dollarRate=bdl.getFloat("dollar-rate");
                     euroRate=bdl.getFloat("euro-rate");
                     wonRate=bdl.getFloat("won-rate");

                     Log.i(TAG,"handleMessage:dollarRate:"+dollarRate);
                     Log.i(TAG,"handleMessage:euroRate:"+euroRate);
                     Log.i(TAG,"handleMessage:wonRate:"+wonRate);

                     //保存更新的日期
                     SharedPreferences sharedPreferences1=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                     SharedPreferences.Editor editor=sharedPreferences.edit();
                     editor.putFloat("dollar_rate",dollarRate);
                     editor.putFloat("euro_rate",euroRate);
                     editor.putFloat("won_rate",wonRate);
                     editor.putString("update_date",todayStr);
                     editor.apply();

                     Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();;

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
        else if(item.getItemId()==R.id.open_list)
        {
            Intent list =new Intent(this,RateListActivity.class);


            // startActivity(config);
            startActivity(list);
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
            try{
                //当前停止两秒钟,2000毫秒
                Thread.sleep(3000);

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            //用于保存获取的汇率
            Bundle bundle=new Bundle();

        //获取Msg对象,用于返回主线程
     /*   Message msg = handler.obtainMessage(5);
        //what数据是整数，用于标记当前messa属性，便于接受信息进行比对,可把what放在上面写
        //msg.what = 5;
        msg.obj="Hello from run()";
        handler.sendMessage(msg);
*/

        //获取网络数据
       /* URL url=null;
        try {
             url = new URL("https://www.currencydo.com/bank_zg/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
             //需要进行转换，返回一个输入流
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG,"run:html="+html );
            Document doc=Jsoup.parse(html);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
*/
       bundle= getFromUsdCny();
    //bundle中保存所获取的汇率
        Message msg = handler.obtainMessage(5);
        //what数据是整数，用于标记当前messa属性，便于接受信息进行比对,可把what放在上面写
        msg.obj=bundle;
        handler.sendMessage(msg);
    }
//从bankofchina获取数据
    private Bundle getFromBOC() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try{
     doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
        Log.i(TAG, "run: "+doc.title());
      Elements tables= doc.getElementsByTag("table");
      Element table2=tables.get(1);
    Log.i(TAG,"run:table2="+table2);
    Elements tds=table2.getElementsByTag("td");

    for(int i=0;i<tds.size();i+=8){
        Element td1=tds.get(i);
        Element td2=tds.get(i+5);
        Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
        String str1 = td1.text();
        String val = td2.text();

        if("美元".equals(td1.text())){
            bundle.putFloat("dollar-rate",100/Float.parseFloat(val));
        }
       else if("欧元".equals(td1.text())){
            bundle.putFloat("euro-rate",100/Float.parseFloat(val));
        }
       else if("韩元".equals(td1.text())){
            bundle.putFloat("won-rate",100/Float.parseFloat(val));
        }
    }
   /* for(Element td:tds){
        Log.i(TAG,"run:td="+td);
        Log.i(TAG,"run:text="+td.text());
        Log.i(TAG,"run:html="+td.html());
    }*/
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private Bundle getFromUsdCny() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try{
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables= doc.getElementsByTag("table");
      /*int i=1;
      for(Element table : tables){
          Log.i(TAG,"run:table["+i+"]="+table);
          i++;
      }*/
            Element table6=tables.get(0);
            Log.i(TAG,"run:table6="+table6);
            Elements tds=table6.getElementsByTag("td");

            for(int i=0;i<tds.size();i+=6){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if("美元".equals(td1.text())){
                    bundle.putFloat("dollar-rate",100/Float.parseFloat(val));
                }
                else if("欧元".equals(td1.text())){
                    bundle.putFloat("euro-rate",100/Float.parseFloat(val));
                }
                else if("韩元".equals(td1.text())){
                    bundle.putFloat("won-rate",100/Float.parseFloat(val));
                }
            }
   /* for(Element td:tds){
        Log.i(TAG,"run:td="+td);
        Log.i(TAG,"run:text="+td.text());
        Log.i(TAG,"run:html="+td.html());
    }*/
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }
    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
