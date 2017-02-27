package com.almanac.piyush.auntkitchen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class atoday extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
EditText iname,imenu,iprice;
    RequestQueue requestQueue;
    Spinner icategory;

    String arr[]={"Odisha","Maharashtrian","Punjabi","Bengali","Kashmiri","Bihari","Jharkhandi","Tamilian","Hyderabadi","Gujrathi","Rajasthani"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atoday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         iname=(EditText) findViewById(R.id.itemname);
         imenu=(EditText) findViewById(R.id.vfevff);
        iprice=(EditText) findViewById(R.id.itemprice);
        icategory=(Spinner) findViewById(R.id.categoryspinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(atoday.this, android.R.layout.simple_spinner_item, arr);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        icategory.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(atoday.this);



        final ProgressDialog p = ProgressDialog.show(atoday.this,"Fetching All Data","Please Wait",false,false);

        JSONObject params = new JSONObject();
        try {
            params.put("email", loadData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String load_url = "http://kgbvbundu.org/capstone/fetchtoday.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url,params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                p.dismiss();
                try {
                    JSONArray arr = response.getJSONArray("itemdetails");
                    JSONObject a = arr.getJSONObject(0);
                    iname.setText(a.getString("atodayitemname"));
                    iprice.setText(a.getString("atodayitemprice"));
                    imenu.setText(a.getString("atodayitemmenu"));


                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(atoday.this,"Internet is slow. Please try again with good internet speed.",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pDialog = ProgressDialog.show(atoday.this,"Logging...","Please wait...",false,false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kgbvbundu.org/capstone/settoday.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            pDialog.dismiss();
                            saveitemname(iname.getText().toString());
                            saveitemmenu(imenu.getText().toString());
                            saveitemprice(iprice.getText().toString());
                            Toast.makeText(atoday.this, "Todays Menu Updated!", Toast.LENGTH_SHORT).show();

                        } else{
                            pDialog.dismiss();
                            Toast.makeText(atoday.this, "Internet Not Connected!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(atoday.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //Creating parameters
                        Map<String,String> params = new Hashtable<>();

                        //Adding parameters
                        params.put("email", loadData());
                        params.put("icat", icategory.getSelectedItem().toString());
                        params.put("iname",iname.getText().toString());
                        params.put("iprice",iprice.getText().toString());
                        params.put("imenu",imenu.getText().toString());
                        // params.put("macid", loadData3());

                        //returning parameters
                        return params;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    protected void saveitemname(String name){
        String FILENAME1 = "itemname.txt";
        String verifyme=name;

        try {
            FileOutputStream fos1 = getApplication().openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            fos1.write(verifyme.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void saveitemmenu(String menu){
        String FILENAME1 = "itemmenu.txt";
        String verifyme=menu;

        try {
            FileOutputStream fos1 = getApplication().openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            fos1.write(verifyme.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void saveitemprice(String price){
        String FILENAME1 = "itemname.txt";
        String verifyme=price;

        try {
            FileOutputStream fos1 = getApplication().openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            fos1.write(verifyme.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atoday, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            startActivity(new Intent(this,Feedback.class));

        }else if(id == R.id.action_aboutdev){
            startActivity(new Intent(this,AboutDev.class));
        }else{
            File dir = getFilesDir();
            File file = new File(dir, "auth_auntyemail.txt");
            file.delete();
            startActivity(new Intent(this,auntlogin.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aorders) {
            startActivity(new Intent(this,aorders.class));
            finish();
        } else if (id == R.id.nav_atoday) {
            startActivity(new Intent(this,atoday.class));
            finish();
        } else if (id == R.id.nav_aprofile) {
            startActivity(new Intent(this,aprofile.class));
            finish();
        } else {
            startActivity(new Intent(this,aabout.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
