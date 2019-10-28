package com.example.sanjay.book_olx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class viewmore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmore);
        allad a = (allad) getIntent().getParcelableExtra("obj");

        TextView book = (TextView) findViewById(R.id.book1);
        TextView author = (TextView) findViewById(R.id.author1);
        TextView edition = (TextView) findViewById(R.id.edition1);
        TextView price = (TextView) findViewById(R.id.price1);
        TextView name = (TextView) findViewById(R.id.name1);
        TextView roll = (TextView) findViewById(R.id.roll1);
        TextView contact = (TextView) findViewById(R.id.contact1);
        TextView mail = (TextView) findViewById(R.id.mail1);
        TextView dept = (TextView) findViewById(R.id.dept1);
        ImageView image = (ImageView) findViewById(R.id.list_image);
        Button call = (Button) findViewById(R.id.call);

        String barray = a.getImage();
        byte[] b = Base64.decode(barray, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        image.setImageBitmap(bmp);
        book.setText(a.getBookname());
        author.setText(a.getAuthor());
        edition.setText(a.getEdition());
        price.setText(a.getPrice());
        name.setText(a.getName());
        roll.setText(a.getRoll());
        contact.setText(a.getCont_no());
        mail.setText(a.getMail());
        dept.setText(a.getDept());
        final String uri = "tel:"+a.getCont_no().trim();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });

    }
}
