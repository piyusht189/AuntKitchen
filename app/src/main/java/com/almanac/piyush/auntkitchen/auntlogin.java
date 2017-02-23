package com.almanac.piyush.auntkitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.renderscript.Script;
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

public class auntlogin extends AppCompatActivity {
    RequestQueue requestQueue;
    String login_url = "http://kgbvbundu.org/capstone/loginaunty.php",signup_url="http://kgbvbundu.org/capstone/registeraunty.php";
    String out="";
    EditText em,pa;
    EditText signupemail,signuppassword,signupconfirmpassword,signupphone,signupaddress,signupname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auntlogin);
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
            startActivity(new Intent(auntlogin.this,aorders.class));
            finish();
        }
    }
    public void auntlogin(View view)
    {

        if(isNetworkAvailable()){
            login();
        }else{
            Toast.makeText(this, "Internet Not Connected !", Toast.LENGTH_SHORT).show();
        }
    }
    public void auntsignup(View view){

        if(signupconfirmpassword.getText().toString().equals(signuppassword.getText().toString())){
            if(signupconfirmpassword.getText().toString().equals("") || signuppassword.getText().toString().equals("") || signupname.getText().toString().equals("") || signupemail.getText().toString().equals("") || signupphone.getText().toString().equals("") || signupaddress.getText().toString().equals(""))
            {
                Toast.makeText(this, "Kindly fill all the fields!", Toast.LENGTH_SHORT).show();
            }
            else {
                if(isNetworkAvailable()){
                    register();
                }else{
                    Toast.makeText(this, "Internet Not Connected !", Toast.LENGTH_SHORT).show();
                }

            }
        }
        else{
            Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
        }

    }
    public void login()
    {
        final String email = em.getText().toString();
        final String pass = pa.getText().toString();

        final ProgressDialog pDialog = ProgressDialog.show(this,"Logging...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    pDialog.dismiss();
                    savedata(email);
                    Toast.makeText(auntlogin.this, "Welcome to AuntKitchen!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(auntlogin.this,aorders.class));
                    finish();
                } else{
                    pDialog.dismiss();
                    Toast.makeText(auntlogin.this, "Invalid Username/Password !", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(auntlogin.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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

        final ProgressDialog pDialog = ProgressDialog.show(this,"Registering...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success")){
                    pDialog.dismiss();
                    Toast.makeText(auntlogin.this, "Successfully Registered , kindly login now!", Toast.LENGTH_SHORT).show();

                }else if(response.equals("already"))
                {
                    pDialog.dismiss();
                    Toast.makeText(auntlogin.this, "Email Already Present", Toast.LENGTH_SHORT).show();
                }else{
                    pDialog.dismiss();
                    Toast.makeText(auntlogin.this, response.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(auntlogin.this, "Failed to register, Try Again !", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(auntlogin.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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
