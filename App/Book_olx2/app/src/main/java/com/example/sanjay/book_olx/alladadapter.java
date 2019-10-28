package com.example.sanjay.book_olx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class alladadapter extends ArrayAdapter<allad> {

    List<allad> adList;
    Context context;
    int resource;

    public alladadapter(Context context, int resource, List<allad> adList) {
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

        ImageView imageView = view.findViewById(R.id.list_image);
        TextView textViewName = view.findViewById(R.id.list_bookname);
        TextView textViewauthor = view.findViewById(R.id.list_author);
        TextView textViewprice = view.findViewById(R.id.list_price);
        TextView textViewedition = view.findViewById(R.id.list_edition);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);

        allad ad1 = adList.get(position);

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
                viewmore(position);
            }
        });

        return view;
    }

    private void viewmore(final int position) {
        allad a = adList.get(position);
        Intent i = new Intent(getContext(), viewmore.class);
        i.putExtra("obj", a);
        context.startActivity(i);
    }

}

