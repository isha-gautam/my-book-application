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

import java.io.File;
import java.io.FileOutputStream;

public class AddBookManual extends AppCompatActivity {

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String TAG = "Add Book Manual";
    private EditText title;
    private EditText author;
    private EditText isbn;
    private ImageView img;
    private TextView clickPicture;
    private TextView noBookImg;
    private Bitmap bookCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_book_manual);
        this.title = findViewById(R.id.queryTitle);
        this.author = findViewById(R.id.queryAuthor);
        this.isbn = findViewById(R.id.queryIsbn);
        this.img = findViewById(R.id.queryImg);
        this.clickPicture = findViewById(R.id.clickPicture);
        this.noBookImg = findViewById(R.id.noBookImg);

        this.clickPicture.setText("Take picture of book");
        this.img.setVisibility(View.GONE);
        this.noBookImg.setVisibility(View.GONE);
    }

    public void addBook(View view) {

        if (!checkRequiredFields())
            Toast.makeText(getApplicationContext(), "Please fill the required fields!", Toast.LENGTH_SHORT).show();
        else if (!checkRating())
            Toast.makeText(getApplicationContext(), "Rating should be less than 10!", Toast.LENGTH_SHORT).show();
        else {
            String title = this.title.getText().toString();
            String author = this.author.getText().toString();
            String isbn = this.isbn.getText().toString();
            String lentTo = ((EditText) findViewById(R.id.queryLentTo)).getText().toString();
            String descr = ((EditText) findViewById(R.id.queryDescr)).getText().toString();
            String rating = ((EditText) findViewById(R.id.queryRating)).getText().toString();
//            String imgURL = this.img.toString();
//            Toast.makeText(getApplicationContext(), "imageurl" + imgURL, Toast.LENGTH_SHORT).show();

            int ratingVal = 0;
            if (!rating.isEmpty())
                ratingVal = Integer.valueOf(rating);
            int read = ((CheckBox) findViewById(R.id.queryRead)).isChecked() == true ? 1 : 0;

            String bookImgPath = this.saveImg();

            Book book = new Book(title, author, isbn);
            book.set_lentto(lentTo);
            book.set_rating(ratingVal);
            book.set_descr(descr);
            book.set_read(read);
            book.set_imgurl(bookImgPath);

            DatabaseHandler db = new DatabaseHandler(this);
            int id = db.addBook(book);

            if (id == -1)
                Toast.makeText(getApplicationContext(), "Book already exists!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Book added!", Toast.LENGTH_SHORT).show();

            db.close();
            deleteImages();
            finish();
        }
    }

    private boolean checkRating() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.queryRead);
        if (checkBox.isChecked()) {
            String rating = ((EditText) findViewById(R.id.queryRating)).getText().toString();
            if (!rating.isEmpty()) {
                int ratingVal = Integer.valueOf(rating);
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

    private boolean checkRequiredFields() {
        return (!TextUtils.isEmpty(this.title.getText())
                && !TextUtils.isEmpty(this.author.getText())
                && !TextUtils.isEmpty(this.isbn.getText())) == true;
    }

    public void checkBoxClicked(View view) {
        LinearLayout RatingLayout = ((LinearLayout) (findViewById(R.id.RatingLayout)));
        if (((CheckBox) view).isChecked())
            RatingLayout.setVisibility(View.VISIBLE);
        else
            RatingLayout.setVisibility(View.GONE);
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
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
//                Bitmap result = data.getParcelableExtra("result");
//                this.img.setImageBitmap(result);
//                this.img.setVisibility(View.VISIBLE);
//                this.bookCapture = result;
//                this.clickPicture.setText("Retake picture");
                try {
                    Uri result = Uri.parse(data.getStringExtra("result"));
                    Bitmap bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result);
                    this.img.setImageBitmap(bp);
                    this.img.setVisibility(View.VISIBLE);
                    this.bookCapture = bp;
                    this.clickPicture.setText("Retake picture");
                } catch (Exception e) {
                    this.noBookImg.setVisibility(View.VISIBLE);
                    this.bookCapture = null;
                    Log.d(TAG, "onActivityResult: ");
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
                this.noBookImg.setVisibility(View.VISIBLE);
                this.bookCapture = null;
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                Picasso.get().load(resultUri).into(this.img);
//                Log.d(TAG, "onActivityResult: " + resultUri);
////                this.img.setImageBitmap(result);
//                this.img.setVisibility(View.VISIBLE);
////                this.bookCapture = result;
//                this.clickPicture.setText("Retake picture");
//            }
//        } else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//            CropImage.getActivityResult(data);
//            Log.d(TAG, "onActivityResult: "+data);
//        }
//        Log.d(TAG, "onActivityResult: "+resultCode);

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
        }
        return path;
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