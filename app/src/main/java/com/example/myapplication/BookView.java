package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class BookView extends AppCompatActivity {

    private Book book;
    private final String TAG = "Book View";
    private TextView imgText;
    private ImageView bookImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_view);

        Intent intent = getIntent();
        this.book = (Book) intent.getParcelableExtra("bookClicked");
        Log.d(TAG, "onCreate: " + book);

        bookImg = findViewById(R.id.bookImg);
        imgText = findViewById(R.id.imgText);

        populateFields();
    }

    public void populateFields() {
        ((TextView) findViewById(R.id.bookTitle)).setText(book.get_title());
        ((TextView) findViewById(R.id.bookAuthor)).setText(book.get_author());
        ((TextView) findViewById(R.id.bookIsbn)).setText(book.get_isbn());
        ((TextView) findViewById(R.id.bookDescr)).setText(book.get_descr());
        ((TextView) findViewById(R.id.bookLentTo)).setText(book.get_lentto().toUpperCase());
        float rating = (float) (book.get_rating() * 0.5);
        ((RatingBar) findViewById(R.id.bookRating)).setRating(rating);
        ((CheckBox) findViewById(R.id.bookRead)).setChecked(book.get_read() == 1);
        imgText.setText(Constants.IMAGE_LOADING);
        imgText.setVisibility(View.VISIBLE);
        bookImg.setVisibility(View.GONE);

        if (!book.get_imgurl().isEmpty()) {
//            if (isLocal(book.get_imgurl())) {
//                Picasso.get().load(new File(book.get_imgurl())).into(bookImg, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        imgText.setVisibility(View.GONE);
//                        bookImg.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        bookImg.setVisibility(View.GONE);
//                        imgText.setText(NO_FILE);
//                        imgText.setVisibility(View.VISIBLE);
//                        Log.d(TAG, "onError: " + book.get_imgurl());
//                        e.printStackTrace();
//                    }
//                });
//            } else {
            Picasso.get().load(book.get_imgurl()).into(bookImg, new Callback() {
                @Override
                public void onSuccess() {
                    imgText.setVisibility(View.GONE);
                    bookImg.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    bookImg.setVisibility(View.GONE);
                    if (e instanceof FileNotFoundException)
                        imgText.setText(Constants.NO_FILE);
                    else
                        imgText.setText(Constants.NETWORK_ERROR);
                    imgText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onError: " + book.get_imgurl());
                    e.printStackTrace();
                    Log.d(TAG, "onError: " + e.getClass().getSimpleName() + "\n" + e.getClass().getTypeName() + "\n" + e.getClass().getName());
                }
            });
//            }
        } else {
            bookImg.setVisibility(View.GONE);
            imgText.setText(Constants.IMAGE_UNAVAILABLE);
            imgText.setVisibility(View.VISIBLE);
        }
    }

    /*
        private boolean isLocal(String p) {
    //        return !p.startsWith("\\w+?://");
            if (URLUtil.isValidUrl(p)) {
                if (URLUtil.isFileUrl(p) || URLUtil.isContentUrl(p))
                    return true;
                else
                    return false;
            }
            return false;
        }
    */
    public void editBook(View view) {
        Intent intent = new Intent(getApplicationContext(), EditBook.class);
        intent.putExtra("fetchedBook", this.book);
        startActivity(intent);
    }

    public void deleteBook(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete this book?").setTitle("Delete " + this.book.get_title())
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.deleteBook(book);
                        Toast.makeText(getApplicationContext(), "Book deleted: " + book.get_title(), Toast.LENGTH_SHORT).show();
                        db.close();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(this);
        this.book = db.getBooks(this.book.getId());
        Log.d(TAG, "onResume: called " + this.book);
        populateFields();
        db.close();
    }
}