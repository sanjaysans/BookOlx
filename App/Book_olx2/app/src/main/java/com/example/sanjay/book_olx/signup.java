package com.example.sanjay.book_olx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class signup extends AppCompatActivity {

    Boolean flag = true, flagc = true;
    EditText fname, lname, roll, mob_no, mail, pw, cpw;
    Spinner dept;
    ImageButton showpw, showcpw;
    Button signup;
    String mRequestBody, p, token;
    User user;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dept = (Spinner) findViewById(R.id.dept_spin);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        roll = (EditText) findViewById(R.id.roll);
        mob_no = (EditText) findViewById(R.id.mob_no);
        mail = (EditText) findViewById(R.id.mail);
        pw = (EditText) findViewById(R.id.pw);
        cpw = (EditText) findViewById(R.id.cpw);
        showpw = (ImageButton) findViewById(R.id.showpw);
        showcpw = (ImageButton) findViewById(R.id.showcpw);
        signup = (Button) findViewById(R.id.signup);
        prog = new ProgressDialog(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dept_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        dept.setAdapter(adapter);

        mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_black_24dp, 0, 0, 0);
        fname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_perm_identity_black_24dp, 0, 0, 0);
        lname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_add_black_24dp, 0, 0, 0);
        pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        roll.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
        cpw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, 0, 0);

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

        mob_no.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 10) {
                    mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, R.drawable.ic_error_black_24dp, 0);
                } else {
                    mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, 0, 0);
                }
            }
        });

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

        showcpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
        showcpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagc) {
                    showcpw.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    cpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagc = false;
                } else {
                    showcpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
                    cpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagc = true;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = roll.getText().toString();
                p = pw.getText().toString();
                String cp = cpw.getText().toString();
                String fn = fname.getText().toString();
                String ln = lname.getText().toString();
                String dp = dept.getSelectedItem().toString();
                String mob = mob_no.getText().toString();
                String ml = mail.getText().toString();
                String name = fn+" "+ln;
                if (check_value(r, p, cp, fn, ln, dp, mob, ml) == 0) {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("roll", r);
                        obj.put("password", p);
                        obj.put("name", name);
                        obj.put("department", dp);
                        obj.put("mobile", mob);
                        obj.put("mail", ml);
                        String URL = "https://bookolx.herokuapp.com/signup";
                        prog.setMessage("signing up");
                        prog.show();
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
                                if(prog.isShowing()){
                                    prog.dismiss();
                                }
                                Intent i = new Intent(getApplicationContext(), nav_home.class);
                                startActivity(i);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(prog.isShowing()){
                                    prog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Server error... Try again later", Toast.LENGTH_SHORT).show();
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void decoderes(String response) throws JSONException {
        JSONObject jsonObj = new JSONObject(response);
        String roll = jsonObj.getString("roll");
        String password = p;
        String name = jsonObj.getString("name");
        String dept = jsonObj.getString("department");
        String mail = jsonObj.getString("mail");
        String cont_no = jsonObj.getString("mobile");
        user = new User(roll, cont_no, name, dept, mail, password, token);
    }

    public int check_value(String roll, String pw, String cpw, String fname, String lname, String dept, String mob_no, String mail) {
        if (roll.length() != 10) {
            Toast.makeText(this, "Enter a valid Roll Number", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (pw.length() < 8) {
            Toast.makeText(this, "Enter a valid Password with min 8 char", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (!cpw.equals(pw)) {
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (mob_no.length() != 10) {
            Toast.makeText(this, "Enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (fname.isEmpty() || lname.isEmpty() || mail.isEmpty()) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            return 1;
        } else {
            return 0;
        }
    }

}