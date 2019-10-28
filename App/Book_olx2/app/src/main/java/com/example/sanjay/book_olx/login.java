package com.example.sanjay.book_olx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class login extends AppCompatActivity {

//Declaration
    Boolean flag = true;
    EditText roll, pw;
    ImageButton showpw;
    Button signup, login;
    User user;
    String b, token, mRequestBody;
    ProgressDialog p;

//inbuilt oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//to check whether logged in or not
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent i = new Intent(getApplicationContext(), nav_home.class);
            startActivity(i);
        }
//values assignment
        setContentView(R.layout.activity_login);
        roll = (EditText) findViewById(R.id.roll);
        pw = (EditText) findViewById(R.id.password);
        showpw = (ImageButton) findViewById(R.id.showpw);
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        p = new ProgressDialog(this);
//text watcher for roll
        roll.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 10) {
                    roll.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, R.drawable.ic_error_black_24dp, 0);
                } else {
                    roll.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
                }
            }
        });
//show password functionality
        showpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
        showpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    showpw.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                } else {
                    showpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
                    pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                }
            }
        });
//onclick for signup
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.example.sanjay.book_olx.signup.class);
                startActivity(i);
            }
        });
//onclick for login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = roll.getText().toString();
                b = pw.getText().toString();
                if (check_value(a, b) == 0) {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("roll", a);
                        obj.put("password", b);
                        String URL = "https://bookolx.herokuapp.com/login";
                        p.setMessage("Logging in...");
                        p.show();
//new post request for login using volley
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        mRequestBody = obj.toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    decoderes(response);
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(p.isShowing()){
                                    p.dismiss();
                                }
                                Intent i = new Intent(getApplicationContext(), nav_home.class);
                                startActivity(i);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(p.isShowing()){
                                    p.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Check values entered or Try again later", Toast.LENGTH_SHORT).show();
                            }
                        }) {
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
                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {

                                    try {
                                        responseString = new String(response.data , "UTF-8");
                                        token = response.headers.get("x-auth");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };
                        requestQueue.add(stringRequest);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    //to decode the response obj and set all values
    public void decoderes(String response) throws JSONException {
        JSONObject jsonObj = new JSONObject(response);
        String roll = jsonObj.getString("roll");
        String password = b;
        String name = jsonObj.getString("name");
        String dept = jsonObj.getString("department");
        String mail = jsonObj.getString("mail");
        String cont_no = jsonObj.getString("mobile");
        user = new User(roll, cont_no, name, dept, mail, password, token);
    }

    //to check the password and roll length
    public int check_value(String roll, String pw) {
        if (roll.length() != 10) {
            Toast.makeText(this, "Enter a valid Roll Number", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (pw.length() < 8) {
            Toast.makeText(this, "Enter a valid Password", Toast.LENGTH_SHORT).show();
            return 1;
        } else {
            return 0;
        }
    }

}
