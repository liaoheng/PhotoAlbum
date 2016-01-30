package com.github.liaoheng.album.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.liaoheng.album.model.Album;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片查看实现 依赖com.squareup.picasso
 * @author liaoheng
 * @version 2015-04-18 01:46
 */
public class ImageDetailFragment extends Fragment {
    private final String TAG = ImageDetailFragment.class.getSimpleName();

    private ImageDetailDelegate delegate;

    public static ImageDetailFragment newInstance(Album album) {
        final ImageDetailFragment f = new ImageDetailFragment();
        f.setArguments(ImageDetailDelegate.getBundle(album));
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getDelegate().onCreate(savedInstanceState, this);
        getDelegate().setImageListener(new ImageDetailDelegate.ImageListener() {
            @Override
            public void load(final String imageUrl, final ImageView imageView) {
                Target target = new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageView.setImageBitmap(bitmap);
                        delegate.finished();
                        delegate.complete();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        delegate.finished();
                        delegate.error();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        delegate.started();
                    }
                };
                imageView.setTag(target);
                Picasso.with(getActivity()).load(imageUrl).into(target);
            }

            @Override
            public void destroy(String imageUrl, ImageView imageView) {
                Picasso.with(getActivity()).cancelRequest((Target) imageView.getTag());
            }

            @Override
            public void downloadStart(String imageUrl) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getDelegate().onCreateView(inflater, container, savedInstanceState, this,
                new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //        getDelegate().onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //        if (getDelegate().onOptionsItemSelected(item,getActivity())) {
        //            return true;
        //        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getDelegate().onDestroyView();
    }

    public ImageDetailDelegate getDelegate() {
        if (delegate == null) {
            delegate = new ImageDetailDelegate();
        }
        return delegate;
    }
}
