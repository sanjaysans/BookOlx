package com.example.sanjay.book_olx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class frag_home extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    List<allad> adList;
    ListView listView;
    JSONArray res;
    alladadapter adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.frag_home, container, false);

        adList = new ArrayList<>();
        listView = (ListView) parentHolder.findViewById(R.id.listview);
        res = ((nav_home) getActivity()).getallres();

        try {
            setlist();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new alladadapter(getContext(), R.layout.allads, adList);

        EditText search = (EditText) parentHolder.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                int textlength = cs.length();
                ArrayList<allad> tempArrayList = new ArrayList<allad>();
                for(allad c: adList){
                    if (textlength <= c.getBookname().length()) {
                        if (c.getBookname().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter = new alladadapter(getContext(), R.layout.allads, tempArrayList);
                listView.setAdapter(adapter);
                setsize();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //attaching adapter to the listview
        listView.setAdapter(adapter);
        setsize();

        return parentHolder;
    }

    public void setlist() throws JSONException {
        for(int i=0; i<res.length(); i++){
            JSONObject jsonobj = res.getJSONObject(i);
            adList.add(new allad(jsonobj.getString("image"), jsonobj.getString("book_name"), jsonobj.getString("author"), jsonobj.getString("price"), jsonobj.getString("edition"), jsonobj.getString("roll"), jsonobj.getString("mobile"), jsonobj.getString("name"), jsonobj.getString("department"), jsonobj.getString("mail")));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    public void setsize(){
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
    }

}
