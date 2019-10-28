package com.example.sanjay.book_olx;

import android.os.Parcel;
import android.os.Parcelable;

public class allad implements Parcelable {
    String image, bookname, author, price, edition, roll, cont_no, name, dept, mail;

    public allad(String image, String bookname, String author, String price, String edition, String roll, String cont_no, String name, String dept, String mail) {
        this.image = image;
        this.bookname = bookname;
        this.author = author;
        this.price = price;
        this.edition = edition;
        this.roll = roll;
        this.cont_no = cont_no;
        this.name = name;
        this.dept = dept;
        this.mail = mail;
    }

    protected allad(Parcel in) {
        image = in.readString();
        bookname = in.readString();
        author = in.readString();
        price = in.readString();
        edition = in.readString();
        roll = in.readString();
        cont_no = in.readString();
        name = in.readString();
        dept = in.readString();
        mail = in.readString();
    }

    public static final Creator<allad> CREATOR = new Creator<allad>() {
        @Override
        public allad createFromParcel(Parcel in) {
            return new allad(in);
        }

        @Override
        public allad[] newArray(int size) {
            return new allad[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getBookname() {
        return bookname;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getEdition() {
        return edition;
    }

    public String getRoll() {
        return roll;
    }

    public String getCont_no() {
        return cont_no;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(bookname);
        dest.writeString(author);
        dest.writeString(price);
        dest.writeString(edition);
        dest.writeString(roll);
        dest.writeString(cont_no);
        dest.writeString(name);
        dest.writeString(dept);
        dest.writeString(mail);
    }
}
