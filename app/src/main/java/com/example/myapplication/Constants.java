package com.example.myapplication;

import android.Manifest;

public class Constants {
    public static final String BOOK_FOUND = "Please confirm the details.\nEdit if necessary:";
    public static final String BOOK_NOT_FOUND = "Book not found in Google Books :(\nPlease enter data manually!";
    public static final String BOOK_LOADING = "Please wait\nwhile we fetch the data...";
    public static final String RETAKE_PICTURE = "Retake picture";
    public static final String NO_PICTURE = "No image";
    public static final String TAKE_PICTURE = "Take picture of book";
    public static final String IMAGE_LOADING = "Loading\nImage";
    public static final String NETWORK_ERROR = "Network\nError";
    public static final String IMAGE_UNAVAILABLE = "No Image\nAvailable";
    public static final String NO_FILE = "No Such\nFile Exists";
    public static final String ISBN = "ISBN";
    public static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    public static final int CAMERA_REQUEST_CODE = 10;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "booksManager";
    public static final String TABLE_BOOKS = "books";
    public static final String KEY_ID = "id";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCR = "description";
    public static final String KEY_IMGURL = "imgUrl";
    public static final String KEY_RATING = "rating";
    public static final String KEY_LENTTO = "lentTo";
    public static final String KEY_READ = "read";
}
