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
        startService(new Intent(Choose.this,BackgroundService.class));
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    public void auntyclick(View view){
        startActivity(new Intent(this,auntlogin.class));
    }
    public void customerclick(View view){
        startActivity(new Intent(this,custlogin.class));
    }
}
