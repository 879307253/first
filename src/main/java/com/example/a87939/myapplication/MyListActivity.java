package com.example.a87939.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<String> data=new ArrayList<String>();
    private String TAG = "MyList";
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        GridView listView=(GridView) findViewById(R.id.mylist);
        //init data
     //   for(int i=0;i<10;i++){
       //     data.add("item"+i);
       // }
       // String data[]={"111","2222"};
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"onItemClick:position="+position);
        Log.i(TAG,"onItemClick:=parent"+parent);
        adapter.remove(parent.getItemAtPosition(position));
    }
}
