package com.example.sanjay.book_olx;

public class ad {
    String image, bookname, author, price, edition, id;

    public ad(String image, String bookname, String author, String price, String edition, String id) {
        this.image = image;
        this.bookname = bookname;
        this.author = author;
        this.price = price;
        this.edition = edition;
        this.id = id;
    }

    public String getId() {
        return id;
    }

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
}
