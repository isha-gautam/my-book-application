package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class GoogleBooksSearch extends AppCompatActivity {

    private Book book;
    private LinearLayout actionBarBottomLL;
    private EditText title;
    private EditText author;
    private TextView isbn;
    private EditText lentTo;
    private EditText descr;
    private EditText rating;
    private CheckBox read;
    private TextView instructionsText;
    private ProgressBar progressBar;
    private CardView bookCard;
    private Bitmap bookCapture;
    private ImageView bookImg;
    private TextView clickPicture;
    private TextView imgText;

    private static final String TAG = "GoogleBookSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_books_search);

        this.instructionsText = findViewById(R.id.instructionsText);
        this.progressBar = findViewById(R.id.progressBar);
        this.actionBarBottomLL = findViewById(R.id.actionBarBottomLL);
        this.bookCard = findViewById(R.id.bookCard);
        this.bookImg = findViewById(R.id.queryImg);
        this.clickPicture = findViewById(R.id.clickPicture);
        this.imgText = findViewById(R.id.imgText);

        String isbn;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                isbn = null;
            } else {
                isbn = extras.getString(Constants.ISBN);
            }
        } else {
            isbn = (String) savedInstanceState.getSerializable(Constants.ISBN);
        }

        Toast.makeText(this, "isbn" + isbn, Toast.LENGTH_SHORT).show();
        this.instructionsText.setText(Constants.BOOK_LOADING);
        this.imgText.setText(Constants.IMAGE_LOADING);
        this.clickPicture.setText(Constants.TAKE_PICTURE);
        this.actionBarBottomLL.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.bookCard.setVisibility(View.GONE);

        this.title = findViewById(R.id.queryTitle);
        this.author = findViewById(R.id.queryAuthor);
        this.isbn = findViewById(R.id.queryIsbn);
        this.lentTo = findViewById(R.id.queryLentTo);
        this.read = findViewById(R.id.queryRead);
        this.rating = findViewById(R.id.queryRating);
        this.descr = findViewById(R.id.queryDescr);

        fetchBook(isbn);
        Log.d(TAG, "onCreate: here");
    }

    private void fetchBook(String isbn) {

        // creating a new array list.
//        bookInfoArrayList = new ArrayList<>();

        // below line is use to initialize
        // the variable for our request queue.
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        // below line we are  creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method we are extracting all our json data.
                try {
                    String title = "";
                    String description = "";
                    String thumbnail = "";
                    String author = "";
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {

                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");

                        if (title.isEmpty())
                            title = volumeObj.optString("title");

                        JSONArray authorsArray = null;
                        if (volumeObj.has("authors"))
                            authorsArray = volumeObj.getJSONArray("authors");

                        if (description.isEmpty())
                            description = volumeObj.optString("description");

                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");

                        if (imageLinks != null && thumbnail.isEmpty())
                            thumbnail = imageLinks.optString("thumbnail");

                        if (authorsArray != null && author.isEmpty()) {
                            StringBuilder sb = new StringBuilder();

                            if (authorsArray.length() > 1) {
                                for (int j = 0; j < authorsArray.length() - 1; j++) {
                                    sb.append(authorsArray.optString(j) + ", ");
                                }
                                sb.append(authorsArray.optString(authorsArray.length() - 1));
                            } else if (authorsArray.length() == 1)
                                sb.append(authorsArray.optString(0));

                            author = sb.toString();
                        }

                    }

                    book = new Book(title, author, isbn);
                    book.set_descr(description);
                    thumbnail = thumbnail.replaceFirst("(?i)^http://", "https://");
                    book.set_imgurl(thumbnail);
                    instructionsText.setText(Constants.BOOK_FOUND);
                    bookCard.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    actionBarBottomLL.setVisibility(View.VISIBLE);
                    populateForm();

                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
//                    Toast.makeText(GoogleBooksSearch.this, "No data found. Please enter manually!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(GoogleBooksSearch.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    instructionsText.setText(Constants.BOOK_NOT_FOUND);
                    progressBar.setVisibility(View.GONE);
                    actionBarBottomLL.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "ERROR: No network connection!\nPlease connect to Internet before trying again.", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ERROR: Server error!\nPlease try again later." + error, Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
                    //TODO
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error, Toast.LENGTH_LONG).show();
                }

                deleteImages();
                finish();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }

    private void populateForm() {

        Picasso.get().setLoggingEnabled(true);
        this.title.setText(this.book.get_title());
        this.author.setText(this.book.get_author());
        this.isbn.setText(this.book.get_isbn());
        this.descr.setText(this.book.get_descr());
        this.lentTo.setText("NO ONE :D");
        this.read.setChecked(false);
        this.rating.setText("");
        checkBoxClicked(findViewById(R.id.queryRead));

        imgText.setText(Constants.IMAGE_LOADING);
        imgText.setVisibility(View.VISIBLE);
        bookImg.setVisibility(View.GONE);

        if (!book.get_imgurl().isEmpty()) {

            Picasso.get().load(book.get_imgurl()).into(bookImg, new Callback() {
                @Override
                public void onSuccess() {
                    imgText.setVisibility(View.GONE);
                    bookImg.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    bookImg.setVisibility(View.GONE);
                    imgText.setText(Constants.NETWORK_ERROR);
                    imgText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onError: " + book.get_imgurl());
                    e.printStackTrace();
                }
            });

        } else {
            bookImg.setVisibility(View.GONE);
            imgText.setText(Constants.IMAGE_UNAVAILABLE);
            imgText.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkRating() {
        if (this.read.isChecked()) {
            if (!TextUtils.isEmpty(this.rating.getText())) {
                int ratingVal = Integer.parseInt(this.rating.getText().toString());
                return ratingVal <= 10;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkBoxClicked(View view) {
        LinearLayout RatingLayout = findViewById(R.id.RatingLayout);
        if (((CheckBox) view).isChecked())
            RatingLayout.setVisibility(View.VISIBLE);
        else
            RatingLayout.setVisibility(View.GONE);
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
            String lentTo = this.lentTo.getText().toString();
            String descr = this.descr.getText().toString();
            String rating = this.rating.getText().toString();
            int ratingVal = 0;
            if (!rating.isEmpty())
                ratingVal = Integer.parseInt(rating);
            int read = this.read.isChecked() ? 1 : 0;

            Book book = new Book(title, author, isbn);
            book.set_lentto(lentTo);
            book.set_rating(ratingVal);
            book.set_descr(descr);
            book.set_read(read);
            String bookImgPath = this.saveImg();
//            book.set_imgurl(this.book.get_imgurl());

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

    private boolean checkRequiredFields() {
        return (!TextUtils.isEmpty(this.title.getText())
                && !TextUtils.isEmpty(this.author.getText())
                && !TextUtils.isEmpty(this.isbn.getText()));
    }

    public void resetBook(View view) {
        this.bookCapture = null;
        this.clickPicture.setText(Constants.TAKE_PICTURE);
        this.populateForm();
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
//                this.bookImg.setImageBitmap(result);
//                this.bookImg.setVisibility(View.VISIBLE);
//                this.bookCapture = result;
//                this.clickPicture.setText(Constants.RETAKE_PICTURE);
                try {
                    Uri result = Uri.parse(data.getStringExtra("result"));
                    Bitmap bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result);
                    this.bookImg.setImageBitmap(bp);
                    this.bookImg.setVisibility(View.VISIBLE);
                    this.bookCapture = bp;
                    this.clickPicture.setText("Retake picture");
                } catch (Exception e) {
                    this.bookImg.setVisibility(View.GONE);
                    this.imgText.setText(Constants.NO_PICTURE);
                    this.imgText.setVisibility(View.VISIBLE);
                    this.bookCapture = null;
                    Log.d(TAG, "onActivityResult: ");
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
                this.bookImg.setVisibility(View.GONE);
                this.imgText.setText(Constants.NO_PICTURE);
                this.imgText.setVisibility(View.VISIBLE);
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