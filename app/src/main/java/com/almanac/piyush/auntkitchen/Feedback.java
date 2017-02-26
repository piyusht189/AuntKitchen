package com.almanac.piyush.auntkitchen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        requestQueue = Volley.newRequestQueue(this);
        fb=(EditText) findViewById(R.id.feedback);
    }


    public void feed(View view){
        if(!fb.getText().toString().equals("")) {
            final ProgressDialog pDialog = ProgressDialog.show(this, "Logging...", "Please wait...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kgbvbundu.org/capstone/feedback.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        pDialog.dismiss();
                        Toast.makeText(Feedback.this, "Query Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(Feedback.this, "Internet Not Connected !", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    Toast.makeText(Feedback.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Creating parameters
                    Map<String, String> params = new Hashtable<>();

                    //Adding parameters
                    params.put("feedback", "By:" + loadData() + " :- " + fb.getText().toString());

                    // params.put("macid", loadData3());

                    //returning parameters
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(this, "Kindly fill Query!", Toast.LENGTH_SHORT).show();
        }
    }

    protected String loadData() {
        String FILENAME = "auth_auntyemail.txt";
        String out = "";

        try {
            FileInputStream fis1 = getApplication().openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;
            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }
}
