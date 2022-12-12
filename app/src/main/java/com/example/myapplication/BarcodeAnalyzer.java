package com.example.myapplication;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    private static final String TAG = "BARCODE_ANALYSER";
    private boolean isDetected;
    private Barcode finalBarcode;
    private AddBookScan addBookScan;
    private BarcodeScannerOptions options;
    private BarcodeScanner scanner;

    public BarcodeAnalyzer(AddBookScan addBookScan) {
        this.addBookScan = addBookScan;
        this.isDetected = false;
        this.options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_EAN_8,
                                Barcode.FORMAT_EAN_13)
                        .build();

        this.scanner = BarcodeScanning.getClient(options);
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            if (!isDetected) {
                                for (Barcode barcode : barcodes) {
//                                    Rect bounds = barcode.getBoundingBox();
//                                    Point[] corners = barcode.getCornerPoints();
                                    String rawValue = barcode.getRawValue();
//                                    int valueType = barcode.getValueType();

//                                    Log.d(TAG, "onSuccess: " + barcode.getClass().getDeclaredFields());
//                                    Log.d(TAG, "rawValue:" + rawValue);
                                    if (!isDetected)
                                        processBarcode(barcode);
                                    else
                                        break;
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error scanning", "onFailure: can't scan barcode");
                            e.printStackTrace();
                            // Task failed with an exception
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            imageProxy.close();
                        }
                    });
        }
    }

    private void processBarcode(Barcode barcode) {
        this.isDetected = true;
        Log.d(TAG, "rawValue:" + barcode.getRawValue());
        this.finalBarcode = barcode;
        this.addBookScan.readBarcode(this.finalBarcode);

    }

    private String print(List<Barcode> list) {
        StringBuilder sb = new StringBuilder();
        for (Barcode barcode : list) {
            sb.append(barcode.getRawValue());
        }
        return sb.toString();
    }
}
