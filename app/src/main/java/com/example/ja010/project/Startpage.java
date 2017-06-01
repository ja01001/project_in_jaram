package com.example.ja010.project;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Startpage extends AppCompatActivity {
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Startpage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },3000);
    }
}
