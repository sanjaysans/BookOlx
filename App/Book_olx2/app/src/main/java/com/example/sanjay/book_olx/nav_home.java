package com.example.sanjay.book_olx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class nav_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    User user;
    ProgressDialog p;
    TextView name, mail;
    int cur_itemid;
    NavigationView navigationView;
    JSONArray res, resall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }
        setContentView(R.layout.nav_home);
        p = new ProgressDialog(this);
        user = SharedPrefManager.getInstance(this).getUser();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header =navigationView.getHeaderView(0);
        name = (TextView)header.findViewById(R.id.nav_name);
        mail = (TextView)header.findViewById(R.id.textView);
        name.setText(user.getName());
        mail.setText(user.getMail());

        navigationView.setCheckedItem(R.id.nav_home);
        cur_itemid = R.id.nav_home;
        displaySelectedScreen1(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (cur_itemid == R.id.nav_home){
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }).create().show();
        }else{
            displaySelectedScreen1(R.id.nav_home);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_yourad){
                try {
                    getad();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(item.getItemId() == R.id.nav_home){
                displaySelectedScreen1(item.getItemId());
            }else{
                displaySelectedScreen(item.getItemId());
            }
            return true;
    }

    private void displaySelectedScreen(int itemId){

        //creating fragment object
        Fragment fragment = null;
        cur_itemid = itemId;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_newad:
                fragment = new frag_newad();
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_yourad:
                fragment = new frag_myad();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void logout(){
        String URL = "https://bookolx.herokuapp.com/logout";
        p.setMessage("Logging out...");
        p.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                if(p.isShowing()){
                    p.dismiss();
                }
                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(p.isShowing()){
                    p.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void getad() throws JSONException {
        p.setMessage("Loading...");
        p.show();
        JSONObject obj = new JSONObject();
        obj.put("roll", user.getRoll());
        String URL = "https://bookolx.herokuapp.com/getuserad";
        final String mRequestBody = obj.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    res = new JSONArray(response);
                    displaySelectedScreen(R.id.nav_yourad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(p.isShowing()){
                    p.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(p.isShowing()){
                    p.dismiss();
                }
                navigationView.setCheckedItem(R.id.nav_home);
                displaySelectedScreen1(R.id.nav_home);
                Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    public JSONArray getres(){
        return res;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void displaySelectedScreen1(final int itemid){
        p.setMessage("Loading...");
        p.show();
        String URL = "https://bookolx.herokuapp.com/getallad";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    resall = new JSONArray(response);
                    openhomefrag(itemid);
                    //Toast.makeText(getApplicationContext(), resall.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(p.isShowing()){
                    p.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(p.isShowing()){
                    p.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), error.class);
                startActivity(i);
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", user.getToken());
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void openhomefrag(int itemid){
        cur_itemid = itemid;
        Fragment fragment = new frag_home();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public JSONArray getallres(){
        return resall;
    }

}
