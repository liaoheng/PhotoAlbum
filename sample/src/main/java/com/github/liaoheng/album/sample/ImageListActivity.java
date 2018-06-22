package com.github.liaoheng.album.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.liaoheng.album.sample.utils.GlideApp;
import com.github.liaoheng.common.adapter.base.BaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.base.IBaseAdapter;
import com.github.liaoheng.common.adapter.core.RecyclerViewHelper;
import com.github.liaoheng.common.adapter.holder.BaseRecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liaoheng
 * @version 2018-06-07 17:23
 */
public class ImageListActivity extends AppCompatActivity {

    private final String TAG = ImageListActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent intent = new Intent(context, ImageListActivity.class);
        context.startActivity(intent);
    }

    Adapter mAdapter;
    RecyclerViewHelper mRecyclerViewHelper;
    private String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
    private int index;
    OkHttpClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mClient = new OkHttpClient();
        mAdapter = new Adapter(this);
        mRecyclerViewHelper = RecyclerViewHelper.newBuilder(this)
                .addLoadMoreFooterView()
                .setAdapter(mAdapter)
                .setOnItemClickListener(
                        new IBaseAdapter.OnItemClickListener<Media>() {
                            @Override
                            public void onItemClick(Media item, View view, int position) {
                                ImageViewActivity.start(ImageListActivity.this,
                                        (ArrayList<Media>) mAdapter.getList(),
                                        position);
                            }
                        })
                .setLoadMoreListener(new RecyclerViewHelper.LoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        load();
                    }
                })
                .build();

        load();
    }

    private void load() {
        index++;
        Flowable.just(url + "" + index)
                .subscribeOn(Schedulers.io())
                .map(
                        new Function<String, String>() {
                            @Override
                            public String apply(String url) throws IOException {
                                Request request = new Request.Builder().url(url).build();
                                Response response = mClient.newCall(request).execute();
                                if (!response.isSuccessful()) {
                                    throw new IOException(String.format("Network Error: %s", response));
                                }
                                return response.body().string();
                            }
                        })
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, ArrayList<Media>>() {
                    @Override
                    public ArrayList<Media> apply(String json) throws JSONException {
                        ArrayList<Media> albums = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray JsonPhotos = jsonObject.getJSONArray("results");
                        for (int i = 0; i < JsonPhotos.length(); i++) {
                            JSONObject photo = JsonPhotos.getJSONObject(i);
                            String image = photo.getString("url");
                            albums.add(new Media("" + i, Uri.parse(image)));
                        }
                        return albums;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new ResourceSubscriber<ArrayList<Media>>() {
            @Override
            public void onStart() {
                mRecyclerViewHelper.setLoadMoreLoading(true);
                super.onStart();
            }

            @Override
            public void onComplete() {
                mRecyclerViewHelper.setLoadMoreLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "", e);
                mRecyclerViewHelper.setLoadMoreLoading(false);
            }

            @Override
            public void onNext(ArrayList<Media> album) {
                mRecyclerViewHelper.setLoadMoreHasLoadedAllItems(album.isEmpty());
                if (mAdapter.isEmpty()) {
                    mAdapter.setList(album);
                } else {
                    mAdapter.addAll(album);
                }
                mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), album.size());
            }
        });
    }

    public class ViewHolder extends BaseRecyclerViewHolder<Media> {

        ImageView image;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = findViewById(R.id.item_image);
            text = findViewById(R.id.item_text);
        }

        @Override
        public void onHandle(Media item, int position) {
            text.setText(item.getName());
            GlideApp.with(getContext()).load(item.getUrl()).into(image);
        }
    }

    public class Adapter extends BaseRecyclerAdapter<Media, ViewHolder> {

        public Adapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflate(R.layout.view_list_item, parent);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolderItem(ViewHolder holder, Media item, int position) {
            holder.onHandle(item, position);
        }
    }

}
