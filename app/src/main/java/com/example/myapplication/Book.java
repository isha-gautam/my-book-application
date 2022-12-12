package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int _id;
    private String _title;
    private String _author;
    private String _isbn;
    private String _descr;
    private String _imgurl;
    private String _lentto;
    private int _rating;
    private int _read;

    public Book() {
        this._descr = "Add description";
        this._imgurl = "";
        this._lentto = "NO ONE :D";
        this._rating = -1;
        this._read = 0;
    }

    public Book(String title, String author, String isbn) {
        this._title = title;
        this._author = author;
        this._isbn = isbn;
        this._descr = "Add description";
        this._imgurl = "";
        this._lentto = "NO ONE :D";
        this._rating = -1;
        this._read = 0;
    }

    public Book(int id, String title, String author, String isbn) {
        this._title = title;
        this._author = author;
        this._isbn = isbn;
        this._id = id;
        this._descr = "Add description";
        this._imgurl = "";
        this._lentto = "NO ONE :D";
        this._rating = -1;
        this._read = 0;
    }

    public String get_title() {
        return _title;
    }

    public String get_author() {
        return _author;
    }

    public String get_isbn() {
        return _isbn;
    }

    public String get_imgurl() {
        return _imgurl;
    }

    public String get_lentto() {
        return _lentto;
    }

    public String get_descr() {
        return _descr;
    }

    public int get_rating() {
        return _rating;
    }

    public int get_read() {
        return _read;
    }

    public int getId() {
        return _id;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_author(String _author) {
        this._author = _author;
    }

    public void set_isbn(String _isbn) {
        this._isbn = _isbn;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void set_descr(String _descr) {
        if (_descr.isEmpty())
            this._descr = "Add description";
        else
            this._descr = _descr;
    }

    public void set_imgurl(String _imgurl) {
        this._imgurl = _imgurl;
    }

    public void set_lentto(String _lentto) {
        if (_lentto.isEmpty())
            this._lentto = "NO ONE :D";
        else
            this._lentto = _lentto;
    }

    public void set_rating(int _rating) {
        this._rating = _rating;
    }

    public void set_read(int _read) {
        this._read = _read;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._id);
        parcel.writeString(this._title);
        parcel.writeString(this._author);
        parcel.writeString(this._isbn);
        parcel.writeString(this._descr);
        parcel.writeString(this._imgurl);
        parcel.writeString(this._lentto);
        parcel.writeInt(this._rating);
        parcel.writeInt(this._read);
    }

    @Override
    public String toString() {
        return "Book{" +
                "_id=" + _id +
                ", _title='" + _title + '\'' +
                ", _author='" + _author + '\'' +
                ", _isbn='" + _isbn + '\'' +
                ", _descr='" + _descr + '\'' +
                ", _imgurl='" + _imgurl + '\'' +
                ", __lentto='" + _lentto + '\'' +
                ", _rating=" + _rating +
                ", _read=" + _read +
                '}';
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            int id = in.readInt();
            String title = in.readString();
            String author = in.readString();
            String isbn = in.readString();
            String descr = in.readString();
            String imgurl = in.readString();
            String lentto = in.readString();
            int rating = in.readInt();
            int read = in.readInt();
            Book book = new Book();
            book.set_id(id);
            book.set_title(title);
            book.set_author(author);
            book.set_isbn(isbn);
            book.set_descr(descr);
            book.set_imgurl(imgurl);
            book.set_lentto(lentto);
            book.set_rating(rating);
            book.set_read(read);

            return book;
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}

