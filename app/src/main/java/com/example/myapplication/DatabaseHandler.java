package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + Constants.TABLE_BOOKS + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_ISBN + " TEXT UNIQUE,"
                + Constants.KEY_TITLE + " TEXT COLLATE NOCASE UNIQUE ,"
                + Constants.KEY_AUTHOR + " TEXT,"
                + Constants.KEY_DESCR + " TEXT,"
                + Constants.KEY_IMGURL + " TEXT,"
                + Constants.KEY_RATING + " INTEGER,"
                + Constants.KEY_LENTTO + " TEXT,"
                + Constants.KEY_READ + " INTEGER" + ")";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_BOOKS);

        // Create tables again
        onCreate(db);

    }

    // code to add the new book
    int addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ISBN, book.get_isbn());
        values.put(Constants.KEY_TITLE, book.get_title());
        values.put(Constants.KEY_AUTHOR, book.get_author());
        values.put(Constants.KEY_DESCR, book.get_descr());
        values.put(Constants.KEY_IMGURL, book.get_imgurl());
        values.put(Constants.KEY_RATING, book.get_rating());
        values.put(Constants.KEY_LENTTO, book.get_lentto());
        values.put(Constants.KEY_READ, book.get_read());

        // Inserting Row
        try {
            db.insertOrThrow(Constants.TABLE_BOOKS, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    ArrayList<Book> getBooks(String title, String author) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_BOOKS, new String[]{Constants.KEY_ID, Constants.KEY_ISBN,
                        Constants.KEY_TITLE, Constants.KEY_AUTHOR, Constants.KEY_DESCR, Constants.KEY_IMGURL,
                        Constants.KEY_RATING, Constants.KEY_LENTTO, Constants.KEY_READ},
                Constants.KEY_TITLE + " LIKE ? AND " + Constants.KEY_AUTHOR + " LIKE ?",
                new String[]{"%" + title + "%", "%" + author + "%"}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList<Book> bookList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.set_id(cursor.getInt(0));
                book.set_isbn(cursor.getString(1));
                book.set_title(cursor.getString(2));
                book.set_author(cursor.getString(3));
                book.set_descr(cursor.getString(4));
                book.set_imgurl(cursor.getString(5));
                book.set_rating(cursor.getInt(6));
                book.set_lentto(cursor.getString(7));
                book.set_read(cursor.getInt(8));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return book list
        return bookList;
    }

    ArrayList<Book> getBooks(String title, String author, HashMap<String, Integer> filters) {
        if (filters.isEmpty())
            return getBooks(title, author);

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sb = new StringBuilder();
        for (String filter : filters.keySet()) {
            if (filter.equals(Constants.KEY_READ)) {
                int value = filters.get(filter);
                if (value == 0)
                    sb.append(" AND " + filter + " LIKE 0");
                else if (value == 1)
                    sb.append(" AND " + filter + " LIKE 1");
            }
        }
        String query = sb.toString();

        Cursor cursor = db.query(Constants.TABLE_BOOKS, new String[]{Constants.KEY_ID, Constants.KEY_ISBN,
                        Constants.KEY_TITLE, Constants.KEY_AUTHOR, Constants.KEY_DESCR, Constants.KEY_IMGURL,
                        Constants.KEY_RATING, Constants.KEY_LENTTO, Constants.KEY_READ},
                Constants.KEY_TITLE + " LIKE ? AND " +
                        Constants.KEY_AUTHOR + " LIKE ?" + query,
                new String[]{"%" + title + "%", "%" + author + "%"}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList<Book> bookList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.set_id(cursor.getInt(0));
                book.set_isbn(cursor.getString(1));
                book.set_title(cursor.getString(2));
                book.set_author(cursor.getString(3));
                book.set_descr(cursor.getString(4));
                book.set_imgurl(cursor.getString(5));
                book.set_rating(cursor.getInt(6));
                book.set_lentto(cursor.getString(7));
                book.set_read(cursor.getInt(8));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return book list
        return bookList;
    }

    // code to get the single book
    public Book getBooks(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_BOOKS, new String[]{Constants.KEY_ID, Constants.KEY_ISBN,
                        Constants.KEY_TITLE, Constants.KEY_AUTHOR, Constants.KEY_DESCR, Constants.KEY_IMGURL,
                        Constants.KEY_RATING, Constants.KEY_LENTTO, Constants.KEY_READ}, Constants.KEY_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book();
        book.set_id(cursor.getInt(0));
        book.set_isbn(cursor.getString(1));
        book.set_title(cursor.getString(2));
        book.set_author(cursor.getString(3));
        book.set_descr(cursor.getString(4));
        book.set_imgurl(cursor.getString(5));
        book.set_rating(cursor.getInt(6));
        book.set_lentto(cursor.getString(7));
        book.set_read(cursor.getInt(8));

        db.close();
        return book;
    }

    // code to get all books in a list view
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> bookList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_BOOKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.set_id(cursor.getInt(0));
                book.set_isbn(cursor.getString(1));
                book.set_title(cursor.getString(2));
                book.set_author(cursor.getString(3));
                book.set_descr(cursor.getString(4));
                book.set_imgurl(cursor.getString(5));
                book.set_rating(cursor.getInt(6));
                book.set_lentto(cursor.getString(7));
                book.set_read(cursor.getInt(8));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return book list
        return bookList;
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ISBN, book.get_isbn());
        values.put(Constants.KEY_TITLE, book.get_title());
        values.put(Constants.KEY_AUTHOR, book.get_author());
        values.put(Constants.KEY_DESCR, book.get_descr());
        values.put(Constants.KEY_IMGURL, book.get_imgurl());
        values.put(Constants.KEY_RATING, book.get_rating());
        values.put(Constants.KEY_LENTTO, book.get_lentto());
        values.put(Constants.KEY_READ, book.get_read());

        // updating row
        return db.update(Constants.TABLE_BOOKS, values, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }

    // Deleting single book
    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_BOOKS, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        Uri uri = Uri.parse(book.get_imgurl());
        File file = new File(uri.getPath());
        Log.d(TAG, "deleteBook: " + file.getPath());
        Log.d(TAG, "deleteBook: " + book.get_imgurl());
        if (file.exists())
            file.delete();
        db.close();
    }

    // Getting books Count
    public int getBooksCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
