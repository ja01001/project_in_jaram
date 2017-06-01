package com.example.ja010.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.VideoView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button BNT_STORE,BNT_RANDOM,BNT_DOWNLOAD,BNT_SEARCH;
    VideoView video;
    EditText ED_SEARCH;
    ListView lv;
    ArrayList<dataclass > list = new ArrayList<dataclass>();
    DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ED_SEARCH = (EditText)findViewById(R.id.ed);
        video = (VideoView)findViewById(R.id.vidio);
        list.add(new dataclass("asda","asdads"));
        adapter = new DataAdapter(this,list);
        lv.setAdapter(adapter);



    }
    public void clcl(View v){
        switch(v.getId()){
            case R.id.btnsearch:

                break;
            case R.id.btnstore:

                break;
            case R.id.btnrandom:

                break;
            case R.id.btndownload:
                Intent i = new Intent(MainActivity.this,Internet.class);
                startActivity(i);
                finish();

                break;

        }

    }
    // internet setting

}
