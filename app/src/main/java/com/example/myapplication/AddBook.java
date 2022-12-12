package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddBook extends AppCompatActivity {

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String TAG = "Add Book";
    private EditText queryISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_book);

        this.queryISBN = findViewById(R.id.queryISBN);
    }

    public void addBookISBN(View view) {
        if (TextUtils.isEmpty(this.queryISBN.getText()))
            Toast.makeText(getApplicationContext(), "Please enter the ISBN", Toast.LENGTH_SHORT).show();
        else {
            Intent i = new Intent(getApplicationContext(), GoogleBooksSearch.class);
            i.putExtra(Constants.ISBN, this.queryISBN.getText().toString());
            startActivity(i);
        }
    }

    public void addBookScan(View view) {
        Log.d(TAG, "addBookScan: here");
        if (hasCameraPermission()) {
            enableCamera();
        } else {
            requestPermission();
        }
    }

    public void addBookManual(View view) {
        Intent i = new Intent(getApplicationContext(), AddBookManual.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
//        finish();
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
        Intent i = new Intent(getApplicationContext(), AddBookScan.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called " + hasCameraPermission());
        this.queryISBN.setText("");
    }
}