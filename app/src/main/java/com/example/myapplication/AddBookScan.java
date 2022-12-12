package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;

import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.concurrent.ExecutionException;

public class AddBookScan extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final String TAG = "ADD_BOOK_SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_book_scan);
        setupBarcodeReader();

//        readBarcode();
    }

    private void setupBarcodeReader() {
        previewView = findViewById(R.id.previewView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                    bindImageAnalysis(cameraProvider, cameraSelector, preview);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getApplicationContext()));
    }

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider, @NonNull CameraSelector cameraSelector, @NonNull Preview preview) {

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new BarcodeAnalyzer(this));

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);
//        readBarcode();
    }

    protected void readBarcode(Barcode barcode) {
//        Log.d(TAG, "barcode detected: " + barcode);
//        Toast.makeText(getApplicationContext(), "Barcode detected!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), GoogleBooksSearch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.ISBN, barcode.getRawValue());
        startActivity(intent);
        finish();

//        long start = System.currentTimeMillis();
//
//        boolean isDetected = false;
//        Barcode barcode = null;
//        int i = 10000;
//        while (System.currentTimeMillis() - start < 10000) {
//            if (i % 1000 == 0)
//                Log.d(TAG, "time: " + i);
//
//            if (this.barcodeAnalyzer.isDetected) {
//                isDetected = true;
//                barcode = this.barcodeAnalyzer.finalBarcode;
//                break;
//            }
//            start = System.currentTimeMillis();
//            i--;
//        }
//
//        if (isDetected) {
//            Toast.makeText(getApplicationContext(), "Barcode detected!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), Google_Books_Search.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("BARCODE", barcode.getRawValue());
//            startActivity(intent);
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), "Barcode not detected!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

}