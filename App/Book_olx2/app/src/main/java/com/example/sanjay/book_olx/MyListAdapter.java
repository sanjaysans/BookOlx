package com.example.sanjay.book_olx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

public class MyListAdapter extends ArrayAdapter<ad> {

    List<ad> adList;
    Context context;
    int resource;
    ProgressDialog p;
    User user;

    public MyListAdapter(Context context, int resource, List<ad> adList) {
        super(context, resource, adList);
        this.context = context;
        this.resource = resource;
        this.adList = adList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);
        p = new ProgressDialog(getContext());
        user = SharedPrefManager.getInstance(getContext()).getUser();
        ImageView imageView = view.findViewById(R.id.list_image);
        TextView textViewName = view.findViewById(R.id.list_bookname);
        TextView textViewauthor = view.findViewById(R.id.list_author);
        TextView textViewprice = view.findViewById(R.id.list_price);
        TextView textViewedition = view.findViewById(R.id.list_edition);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);

        ad ad1 = adList.get(position);

        String barray = ad1.getImage();
        byte[] b = Base64.decode(barray, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bmp);
        textViewName.setText(ad1.getBookname());
        textViewauthor.setText(ad1.getAuthor());
        textViewprice.setText(ad1.getPrice());
        textViewedition.setText(ad1.getEdition());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletead(position);
            }
        });

        return view;
    }

    private void deletead(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    deletereq(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deletereq(final int position) throws JSONException {
        p.setMessage("Loading...");
        p.show();
        ad a = adList.get(position);
        JSONObject obj = new JSONObject();
        obj.put("id", a.getId());
        String URL = "https://bookolx.herokuapp.com/deluserad";
        final String mRequestBody = obj.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adList.remove(position);
                notifyDataSetChanged();
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
}
