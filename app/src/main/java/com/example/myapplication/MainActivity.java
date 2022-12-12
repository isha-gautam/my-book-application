package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    public void viewBooks(View view) {
        Intent i = new Intent(getApplicationContext(), ViewAllBooks.class);
        startActivity(i);
    }

    public void addBook(View view) {
        Intent i = new Intent(getApplicationContext(), AddBook.class);
        startActivity(i);
    }

}