package com.github.liaoheng.album.sample;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.liaoheng.album.sample.utils.GlideApp;
import com.github.liaoheng.album.sample.utils.Utils;
import com.github.liaoheng.album.ui.ImageDetailDelegate;

/**
 * @author liaoheng
 * @version 2015-12-24 09:37
 */
public class ImageViewFragment extends Fragment {

    private final String TAG = ImageViewFragment.class.getSimpleName();

    public static ImageViewFragment newInstance(Media album) {
        final ImageViewFragment f = new ImageViewFragment();
        f.setArguments(ImageDetailDelegate.getBundle(album));
        return f;
    }

    private ImageDetailDelegate<Media> detailDelegate;
    private CompleteReceiver completeReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        completeReceiver = new CompleteReceiver();
        getContext().registerReceiver(completeReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        setHasOptionsMenu(true);
        getDetailDelegate().setImageListener(new ImageDetailDelegate.ImageListener<Media>() {
            @Override
            public void load(Media album, final ImageView imageView) {
                mRetry.setVisibility(View.GONE);
                if (album.getType() == 0) {
                    GlideApp.with(ImageViewFragment.this)
                            .load(album.getUrl())
                            .listener(new RequestListener<Drawable>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target,
                                        boolean isFirstResource) {
                                    getDetailDelegate().finished();
                                    getDetailDelegate().error(e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                        DataSource dataSource,
                                        boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(new ImageViewTarget<Drawable>(imageView) {

                                @Override
                                public void onResourceReady(@NonNull Drawable resource,
                                        @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                    imageView.setImageDrawable(resource);
                                    getDetailDelegate().finished();
                                    getDetailDelegate().complete();
                                }

                                @Override
                                protected void setResource(@Nullable Drawable resource) {

                                }

                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    super.onLoadStarted(placeholder);
                                    getDetailDelegate().start();
                                }
                            });
                } else if (album.getType() == 1) {
                    GlideApp.with(ImageViewFragment.this)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .load(album.getUrl())
                            .listener(new RequestListener<GifDrawable>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<GifDrawable> target,
                                        boolean isFirstResource) {
                                    getDetailDelegate().finished();
                                    getDetailDelegate().error(e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GifDrawable resource, Object model,
                                        Target<GifDrawable> target,
                                        DataSource dataSource,
                                        boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(new ImageViewTarget<GifDrawable>(imageView) {

                                @Override
                                public void onResourceReady(@NonNull GifDrawable resource,
                                        @Nullable Transition<? super GifDrawable> transition) {
                                    super.onResourceReady(resource, transition);
                                    imageView.setImageDrawable(resource);
                                    resource.start();
                                    getDetailDelegate().finished();
                                    getDetailDelegate().complete();
                                }

                                @Override
                                protected void setResource(@Nullable GifDrawable resource) {

                                }

                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    super.onLoadStarted(placeholder);
                                    getDetailDelegate().start();
                                }
                            });
                }
            }

            @Override
            public void error(Media album, ImageView imageView, Throwable e) {
                mRetry.setVisibility(View.VISIBLE);
                Log.e(TAG, e.getMessage(), e);
            }

            @Override
            public void destroy(Media album, ImageView imageView) {
                //https://github.com/bumptech/glide/issues/803#issuecomment-163528497
                GlideApp.with(ImageViewFragment.this).clear(imageView);
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.recycle();
                }
            }
        });

    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "download ok", Toast.LENGTH_SHORT).show();
        }
    }

    private View mRetry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_album_detail, container, false);
        mRetry = view.findViewById(R.id.photo_album_detail_retry);

        if (mRetry != null) {
            mRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetailDelegate().loadImage();
                }
            });
        }
        getDetailDelegate().onCreate(view, getArguments());
        getDetailDelegate().loadImage();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.photo_album_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.photo_album_download_image) {
            Uri url = getDetailDelegate().getMedia().getUrl();
            Toast.makeText(getContext(), "start download", Toast.LENGTH_SHORT).show();
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(url);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalFilesDir(getContext(), null, Utils.getName(url.toString()));
            downloadManager.enqueue(request);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        getDetailDelegate().destroy();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(completeReceiver);
        super.onDestroy();
    }

    public ImageDetailDelegate<Media> getDetailDelegate() {
        if (detailDelegate == null) {
            detailDelegate = new ImageDetailDelegate<>();
        }
        return detailDelegate;
    }
}
