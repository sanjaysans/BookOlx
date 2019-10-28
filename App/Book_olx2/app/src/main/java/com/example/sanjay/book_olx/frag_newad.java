package com.example.sanjay.book_olx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static android.app.Activity.RESULT_OK;

public class frag_newad extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    private static int RESULT_LOAD_IMAGE = 1;
    Button post,buttonLoadImage;
    EditText book_name, author, edition, price;
    JSONObject obj;
    TextView t1;
    byte[] b;
    User user;
    ImageView imageView;
    ProgressDialog prog;
    String imgStr;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.frag_newad, container, false);

        buttonLoadImage = (Button) parentHolder.findViewById(R.id.buttonLoadPicture);
        post = (Button) parentHolder.findViewById(R.id.post);
        book_name = (EditText) parentHolder.findViewById(R.id.bookname);
        author = (EditText) parentHolder.findViewById(R.id.author);
        edition = (EditText) parentHolder.findViewById(R.id.edition);
        price = (EditText) parentHolder.findViewById(R.id.price);
        t1 = (TextView) parentHolder.findViewById(R.id.data);
        imageView = (ImageView) parentHolder.findViewById(R.id.imgView);
        user = SharedPrefManager.getInstance(getContext()).getUser();
        prog = new ProgressDialog(getContext());

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newadcheck(book_name.getText().toString(), author.getText().toString(), edition.getText().toString(), price.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return parentHolder;
    }

    public boolean newadcheck(String n, String a, String e, String p) throws JSONException {
        if(n.length() > 5 && n.length() < 50){
            if(a.length() < 20){
                if(e.length() < 20){
                    if(p.length() < 5){
                        prog.setMessage("Posting AD...");
                        prog.show();
                        obj = new JSONObject();
                        obj.put("book_name", n);
                        obj.put("author", a);
                        obj.put("edition", e);
                        obj.put("price", p);
                        obj.put("image", imgStr);
                        obj.put("roll", user.getRoll());
                        obj.put("name", user.getName());
                        obj.put("department", user.getDept());
                        obj.put("mobile", user.getCont_no());
                        obj.put("mail", user.getMail());
                        postad(obj);
                        return true;
                    }else{
                        Toast.makeText(getContext(), "Enter a valid price", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Enter a valid Edition", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Enter a valid Author Name", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Enter a valid Book Name", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void postad(JSONObject obj){
        String URL = "https://bookolx.herokuapp.com/newad";
        final String mRequestBody = obj.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(prog.isShowing()){
                    prog.dismiss();
                }
                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), nav_home.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(prog.isShowing()){
                    prog.dismiss();
                }
                Toast.makeText(getContext(), "Server error... try again later", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("New AD");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                b = null;
                InputStream imagestream = getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap image = BitmapFactory.decodeStream(imagestream);
                image = Bitmap.createScaledBitmap(image, 100, 130, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                imgStr = Base64.encodeToString(b, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageView.setImageURI(selectedImage);
        }
    }

}
