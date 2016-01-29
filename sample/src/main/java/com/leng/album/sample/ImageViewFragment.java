package com.leng.album.sample;

import java.io.File;
import java.util.concurrent.TimeUnit;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leng.album.model.Album;
import com.leng.album.sample.utils.Utils;
import com.leng.album.ui.ImageDetailDelegate;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * @author liaoheng
 * @version 2015-12-24 09:37
 */
public class ImageViewFragment extends Fragment {

    public static ImageViewFragment newInstance(Album album) {
        final ImageViewFragment f = new ImageViewFragment();
        f.setArguments(ImageDetailDelegate.getBundle(album));
        return f;
    }

    private ImageDetailDelegate detailDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDetailDelegate().onCreate(savedInstanceState, this);
        getDetailDelegate().setImageListener(new ImageDetailDelegate.ImageListener() {
            @Override
            public void load(String url, final ImageView imageView) {
                Uri path;
                if (Utils.isHtmlUrl(url)) {
                    path = Uri.parse(url);
                } else {
                    path = Uri.fromFile(new File(url));
                }

                Target target = new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageView.setImageBitmap(bitmap);
                        getDetailDelegate().finished();
                        getDetailDelegate().complete();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        getDetailDelegate().finished();
                        getDetailDelegate().error();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        getDetailDelegate().started();
                    }
                };
                imageView.setTag(target);
                Picasso.with(getContext()).load(path).into(target);
            }

            @Override
            public void destroy(String imageUrl, ImageView imageView) {
                Picasso.with(getContext()).cancelRequest((Target) imageView.getTag());
            }

            @Override
            public void downloadStart(String imageUrl) {
                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(imageUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                        | DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalFilesDir(getContext(), null, getName(imageUrl));
                downloadManager.enqueue(request);
            }
        });
    }

    public  String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';
    public  int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getDetailDelegate().onCreateView(inflater,container,savedInstanceState,this,null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getDetailDelegate().onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getDetailDelegate().onOptionsItemSelected(item, getActivity())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getDetailDelegate().onDestroyView();
    }

    public ImageDetailDelegate getDetailDelegate() {
        if (detailDelegate == null) {
            detailDelegate = new ImageDetailDelegate();
        }
        return detailDelegate;
    }
}
