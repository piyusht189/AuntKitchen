package com.almanac.piyush.auntkitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;

public class AuntLogin extends AppCompatActivity {
    RequestQueue requestQueue;
    String login_url,signup_url;
    String out="";
    EditText em,pa;
    EditText signupemail,signuppassword,signupconfirmpassword,signupphone,signupaddress,signupname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auntlogin);
        login_url = getResources().getString(R.string.loginaunty);
        signup_url=getResources().getString(R.string.registeraunty);
        em=(EditText) findViewById(R.id.loginemail);
        pa=(EditText) findViewById(R.id.loginpassword);
        signupaddress=(EditText) findViewById(R.id.signupaddress);
        signupconfirmpassword=(EditText) findViewById(R.id.signupconfirmpassword);
        signupemail=(EditText) findViewById(R.id.signupemail);
        signupname=(EditText) findViewById(R.id.signupname);
        signuppassword=(EditText) findViewById(R.id.signuppassword);
        signupphone=(EditText) findViewById(R.id.signupphone);
        requestQueue = Volley.newRequestQueue(this);
        if(!loadData().equals("")){
            startActivity(new Intent(AuntLogin.this,Aorders.class));
            finish();
        }
    }
    public void auntlogin(View view)
    {
if(!em.getText().toString().equals("") || pa.getText().toString().equals("")){


        if(isNetworkAvailable()){
            login();
        }else{
            Toast.makeText(this,getResources().getString(R.string.slownet), Toast.LENGTH_SHORT).show();
        }
}else {
    Toast.makeText(this, getResources().getString(R.string.fillallfields), Toast.LENGTH_SHORT).show();
}
    }
    public void auntsignup(View view) {
        if (!signupaddress.getText().toString().equals("") || !signupphone.getText().toString().equals("") || !signupemail.getText().toString().equals("")) {
            if (signupconfirmpassword.getText().toString().equals(signuppassword.getText().toString())) {
                if (signupconfirmpassword.getText().toString().equals("") || signuppassword.getText().toString().equals("") || signupname.getText().toString().equals("") || signupemail.getText().toString().equals("") || signupphone.getText().toString().equals("") || signupaddress.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.fillallfields), Toast.LENGTH_SHORT).show();
                } else {
                    if (isNetworkAvailable()) {
                        register();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.slownet), Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.passwordnotmatch), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, getResources().getString(R.string.fillallfields), Toast.LENGTH_SHORT).show();
        }
    }
    public void login()
    {
        final String email = em.getText().toString();
        final String pass = pa.getText().toString();

        final ProgressDialog pDialog = ProgressDialog.show(this,getResources().getString(R.string.logging),getResources().getString(R.string.pleasewait),false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(getResources().getString(R.string.success))){
                    pDialog.dismiss();
                    savedata(email);
                    Toast.makeText(AuntLogin.this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuntLogin.this,Aorders.class));
                    finish();
                } else{
                    pDialog.dismiss();
                    Toast.makeText(AuntLogin.this, getResources().getString(R.string.invalid), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(AuntLogin.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put("email", email);
                params.put("password", pass);
                // params.put("macid", loadData3());

                //returning parameters
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void register()
    {

        final ProgressDialog pDialog = ProgressDialog.show(this,getResources().getString(R.string.registering),getResources().getString(R.string.pleasewait),false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals(getResources().getString(R.string.success))){
                    pDialog.dismiss();
                    Toast.makeText(AuntLogin.this, getResources().getString(R.string.successregister), Toast.LENGTH_SHORT).show();

                }else{
                    pDialog.dismiss();
                    Toast.makeText(AuntLogin.this, response.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(AuntLogin.this, getResources().getString(R.string.slownet), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(AuntLogin.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put("email", signupemail.getText().toString());
                params.put("password", signuppassword.getText().toString());
                params.put("name",signupname.getText().toString());
                params.put("phone",signupphone.getText().toString());
                params.put("address",signupaddress.getText().toString());
                // params.put("macid", loadData3());

                //returning parameters
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    protected void savedata(String email){
        String FILENAME1 = "auth_auntyemail.txt";
        String verifyme=email;

        try {
            FileOutputStream fos1 = getApplication().openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            fos1.write(verifyme.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected String loadData() {
        String FILENAME = "auth_auntyemail.txt";

        try {
            out="";
            FileInputStream fis1 = getApplication().openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1 = null;

            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return out;
    }
}
