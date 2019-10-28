package com.example.sanjay.book_olx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class frag_myad extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    List<ad> adList;
    ListView listView;
    JSONArray res;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.frag_myad, container, false);

        adList = new ArrayList<>();
        listView = (ListView) parentHolder.findViewById(R.id.listview);

        res = ((nav_home) getActivity()).getres();

        try {
            setlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyListAdapter adapter = new MyListAdapter(getContext(), R.layout.custom_list, adList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);

        ListAdapter listadp = listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
        return parentHolder;
    }

    public void setlist() throws JSONException {
        for(int i=0; i<res.length(); i++){
            JSONObject jsonobj = res.getJSONObject(i);
            adList.add(new ad(jsonobj.getString("image"), jsonobj.getString("book_name"), jsonobj.getString("author"), jsonobj.getString("price"), jsonobj.getString("edition"), jsonobj.getString("_id")));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My AD's");
    }
}
