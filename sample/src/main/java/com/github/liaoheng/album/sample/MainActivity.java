package com.github.liaoheng.album.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * @author liaoheng
 * @version 2016-01-29 15:12
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRxPermissions = new RxPermissions(this);

        findViewById(R.id.local).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean granted) {
                        if (granted) { // Always true pre-M
                            File externalStorageDirectory = Environment.getExternalStorageDirectory();
                            File inFile = new File(externalStorageDirectory, "image.gif");
                            Media album = new Media("", Uri.fromFile(inFile));
                            album.setType(1);
                            File inFile1 = new File(externalStorageDirectory, "image.jpg");
                            Media album1 = new Media("", Uri.fromFile(inFile1));
                            ArrayList<Media> albums = new ArrayList<>();
                            albums.add(album1);
                            albums.add(album);
                            ImageViewActivity.start(MainActivity.this, albums);
                        } else {
                            // Oups permission denied
                        }
                    }
                });
            }
        });

        findViewById(R.id.network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageListActivity.start(MainActivity.this);
            }
        });
    }

}
