package com.library.basecontroller;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;


public final class CustomBindingAdapters {

    private CustomBindingAdapters() {
        //NO-OP
    }

    @BindingAdapter({"app:imageUrl", "app:error"})
    public static void setImageUrl(ImageView imageView, String url, Drawable drawable) {
        if (imageView != null) {
            Context context = imageView.getContext();
            if (url == null) imageView.setImageDrawable(drawable);
        }
    }

    @BindingAdapter({"app:imageCircleUrl", "app:error"})
    public static void setCircleImageUrl(ImageView imageView, String url, Drawable drawable) {
        if (imageView != null) {
            Context context = imageView.getContext();
            if (url == null) imageView.setImageDrawable(drawable);
        }
    }


    @BindingAdapter("app:visibleInvisible")
    public static void setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("app:visibleGone")
    public static void setVisibleGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("app:entries")
    public static void setSpinnerStringAdapter(Spinner spinner, List<?> list) {
        if (list != null) {
            ArrayAdapter<Object> adapter = new ArrayAdapter(spinner.getContext(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    /* @BindingAdapter("app:custom_entries")
     public static void setSpinnerCustomAdapter(Spinner spinner, List<?> list) {
         if (list != null) {
             ArrayAdapter<Object> adapter = new ArrayAdapter(spinner.getContext(), android.R.layout.simple_spinner_item, list);
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             spinner.setAdapter(adapter);
         }
     }
 */
    @BindingAdapter("app:adapter")
    public static void setAdapter(RecyclerView recyclerView, BaseRecyclerViewAdapter baseRecyclerViewAdapter) {
        if (baseRecyclerViewAdapter != null && recyclerView != null) {
            recyclerView.setAdapter(baseRecyclerViewAdapter);
        }
    }

    @BindingAdapter("app:smoothScrollPosition")
    public static void scrollToPosition(RecyclerView recyclerView, Integer position) {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(position);
        }
    }


    @BindingAdapter("app:fabSrc")
    public static void setImageRes(final FloatingActionButton fab, final @DrawableRes int drawable) {
        if (fab != null && drawable != 0)
            fab.setImageResource(drawable);
    }

}
