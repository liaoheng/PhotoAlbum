package com.github.liaoheng.album.sample;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;

import com.github.liaoheng.album.model.Album;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * @author liaoheng
 * @version 2016-01-29 15:12
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    Subscription subscription;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final OkHttpClient client = new OkHttpClient();


        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (subscription != null) {
                    subscription.unsubscribe();
                }
            }
        });

        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscriber<Album> subscriber=new Subscriber<Album>() {
                    @Override
                    public void onStart() {
                        mProgressDialog.show();
                    }

                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "", e);
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onNext(Album album) {
                        ImageViewActivity.start(MainActivity.this, album);
                    }
                };
                subscription = Observable.just("https://api.douban.com/v2/album/103756651/photos")
                        .observeOn(Schedulers.io()).map(new Func1<String, String>() {
                            @Override
                            public String call(String url) {
                                Request request = new Request.Builder().url(url).build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    if (!response.isSuccessful()) {
                                        throw new IOException(String.format("Network Error: %s", response));
                                    }
                                    return response.body().string();
                                } catch (IOException e) {
                                    throw new AndroidRuntimeException("error", e);
                                }
                            }
                        }).observeOn(Schedulers.computation()).map(new Func1<String, Album>() {

                            @Override
                            public Album call(String json) {
                                Album album = new Album();
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    JSONArray JsonPhotos = jsonObject.getJSONArray("photos");
                                    for (int i = 0; i < JsonPhotos.length(); i++) {
                                        JSONObject photo = JsonPhotos.getJSONObject(i);
                                        String image = photo.getString("image");
                                        album.setItem("" + i, image);
                                    }
                                } catch (JSONException e) {
                                    Log.w(TAG, e);
                                }
                                return album;
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (subscription!=null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
