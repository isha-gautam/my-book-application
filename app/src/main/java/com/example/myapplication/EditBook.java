package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class EditBook extends AppCompatActivity {

    private Book book;
    private static final String TAG = "edit book";
    private TextView title;
    private TextView author;
    private TextView isbn;
    private EditText lentTo;
    private EditText descr;
    private EditText rating;
    private CheckBox read;
    private ImageView queryImg;
    private TextView clickPicture;
    private TextView imgText;
    private Bitmap bookCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_book);

        Intent intent = getIntent();
        this.book = (Book) intent.getParcelableExtra("fetchedBook");
        Log.d(TAG, "onCreate: " + book);
        this.title = findViewById(R.id.bookTitle);
        this.author = findViewById(R.id.bookAuthor);
        this.isbn = findViewById(R.id.bookIsbn);
        this.lentTo = findViewById(R.id.queryLentTo);
        this.read = findViewById(R.id.queryRead);
        this.rating = findViewById(R.id.queryRating);
        this.descr = findViewById(R.id.queryDescr);
        this.queryImg = findViewById(R.id.queryImg);
        this.clickPicture = findViewById(R.id.clickPicture);
        this.imgText = findViewById(R.id.imgText);

        populateFields();
    }

    public void populateFields() {
        this.clickPicture.setText("Take picture of book");
        this.queryImg.setVisibility(View.GONE);
        this.imgText.setVisibility(View.GONE);

        this.title.setText(this.book.get_title());
        this.author.setText(this.book.get_author());
        this.isbn.setText(this.book.get_isbn());
        this.lentTo.setText(this.book.get_lentto());
        this.descr.setText(this.book.get_descr());
        this.read.setChecked(this.book.get_read() == 1);

        if (this.book.get_read() == 0)
            findViewById(R.id.RatingLayout).setVisibility(View.GONE);
        else {
            findViewById(R.id.RatingLayout).setVisibility(View.VISIBLE);
            this.rating.setText(String.valueOf(this.book.get_rating()));
        }

        loadImage();
    }

    public void loadImage() {

        imgText.setText(Constants.IMAGE_LOADING);
        imgText.setVisibility(View.VISIBLE);
        queryImg.setVisibility(View.GONE);

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
            Picasso.get().load(book.get_imgurl()).into(queryImg, new Callback() {
                @Override
                public void onSuccess() {
                    imgText.setVisibility(View.GONE);
                    queryImg.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    queryImg.setVisibility(View.GONE);
                    imgText.setText(Constants.NETWORK_ERROR);
                    imgText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onError: " + book.get_imgurl());
                    e.printStackTrace();
                }
            });
//            }
        } else {
            queryImg.setVisibility(View.GONE);
            imgText.setText(Constants.IMAGE_UNAVAILABLE);
            imgText.setVisibility(View.VISIBLE);
        }
    }

    public void editBook(View view) {
        if (!checkRating())
            Toast.makeText(getApplicationContext(), "Rating should be less than 10!", Toast.LENGTH_SHORT).show();
        else {
            int id = this.book.getId();
            String title = this.title.getText().toString();
            String author = this.author.getText().toString();
            String isbn = this.isbn.getText().toString();
            String lentTo = this.lentTo.getText().toString();
            String descr = this.descr.getText().toString();
            String rating = this.rating.getText().toString();

            int read = this.read.isChecked() == true ? 1 : 0;
            int ratingVal = 0;
            if (this.read.isChecked() == true && !rating.isEmpty())
                ratingVal = Integer.valueOf(rating);

            String bookImgPath = this.saveImg();

            Book book = new Book(id, title, author, isbn);
            book.set_lentto(lentTo);
            book.set_rating(ratingVal);
            book.set_descr(descr);
            book.set_read(read);
            book.set_imgurl(bookImgPath);

            DatabaseHandler db = new DatabaseHandler(this);
            db.updateBook(book);

            Toast.makeText(getApplicationContext(), "Book updated!", Toast.LENGTH_SHORT).show();

            db.close();
            deleteImages();
            finish();
        }
    }

    private boolean checkRating() {
        if (this.read.isChecked()) {
            if (!TextUtils.isEmpty(this.rating.getText())) {
                int ratingVal = Integer.parseInt(this.rating.getText().toString());
                if (ratingVal <= 10)
                    return true;
                else
                    return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkBoxClicked(View view) {
        LinearLayout RatingLayout = (findViewById(R.id.RatingLayout));
        if (((CheckBox) view).isChecked())
            RatingLayout.setVisibility(View.VISIBLE);
        else
            RatingLayout.setVisibility(View.GONE);
    }

    public void toastDelete(View view) {
        Toast.makeText(getApplicationContext(), "Sorry! Please delete this book and add again to change book title/author/isbn!", Toast.LENGTH_SHORT).show();
    }

    public void clickPicture(View view) {

        if (hasCameraPermission()) {
            enableCamera();
        } else {
            requestPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        Log.d(TAG, "requestPermission: here");
        ActivityCompat.requestPermissions(
                this,
                Constants.CAMERA_PERMISSION,
                Constants.CAMERA_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                enableCamera();
            else
                Toast.makeText(getApplicationContext(), "Please give permission to access camera!", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void enableCamera() {
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, CaptureImage.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
//        Intent i = new Intent(getApplicationContext(), CaptureImage.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
//                Bitmap result = data.getParcelableExtra("result");
//                this.queryImg.setImageBitmap(result);
//                this.queryImg.setVisibility(View.VISIBLE);
//                this.bookCapture = result;
//                this.clickPicture.setText("Retake picture");
                try {
                    Uri result = Uri.parse(data.getStringExtra("result"));
                    Bitmap bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result);
                    this.queryImg.setImageBitmap(bp);
                    this.queryImg.setVisibility(View.VISIBLE);
                    this.bookCapture = bp;
                    this.clickPicture.setText(Constants.RETAKE_PICTURE);
                } catch (Exception e) {
                    loadImage();
                    this.bookCapture = null;
                    Log.d(TAG, "onActivityResult: ");
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
//                this.queryImg.setVisibility(View.VISIBLE);
//                this.clickPicture.setText("Retake picture");
                loadImage();
                this.bookCapture = null;

            }
        }
    }

    private String saveImg() {
        String path = "";
        if (this.bookCapture != null) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Image_" + System.currentTimeMillis() + ".jpg");
            path = file.toURI().toString();
            try {
                FileOutputStream out = new FileOutputStream(file);
                this.bookCapture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!this.book.get_imgurl().isEmpty())
            path = this.book.get_imgurl();
        return path;
    }

    public void resetBook(View view) {
        this.populateFields();
    }

    private void deleteImages() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();

        for (File file : files) {
            if (!file.getName().startsWith("Image")) {
                file.delete();
            }
        }
    }
}