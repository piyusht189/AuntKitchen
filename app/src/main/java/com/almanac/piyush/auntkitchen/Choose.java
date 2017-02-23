package com.almanac.piyush.auntkitchen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Choose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }
    public void auntyclick(View view){
        startActivity(new Intent(this,auntlogin.class));
    }
    public void customerclick(View view){
        startActivity(new Intent(this,custlogin.class));
    }
}
