package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.niedzwiecki.przemyslguide.BuildConfig;
import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.ui.base.FrameBaseItemView;
import com.niedzwiecki.przemyslguide.ui.custom.CustomLoadingProgressBar;
import com.niedzwiecki.przemyslguide.util.CustomPicasso;
import com.squareup.picasso.Callback;

import butterknife.BindView;
import timber.log.Timber;

public class ImagePage extends FrameBaseItemView<String[]> {

    @BindView(R.id.page_image)
    ImageView imageView;
    @BindView(R.id.progress_bar)
    CustomLoadingProgressBar progressBar;
    @BindView(R.id.error_button)
    Button errorButton;

    String[] images = new String[0];
    int page;
    private Callback callback;

    public ImagePage(Context context) {
        super(context);
    }

    public ImagePage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int contentId() {
        return R.layout.page_image;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPageImage((Integer) v.getTag(), imageView, progressBar, errorButton);
            }
        });
        callback = new Callback() {
            @Override
            public void onSuccess() {
                progressBar.hide();
            }

            @Override
            public void onError() {
                progressBar.hide();
                errorButton.setTag(page);
                errorButton.setVisibility(VISIBLE);
            }
        };
    }

    @Override
    public void setData(int position, String[] images) {
        this.images = images;
        this.page = position;
        loadPageImage(position, imageView, progressBar, errorButton);
    }

    private void loadPageImage(final int position, ImageView imageView,
                               final CustomLoadingProgressBar progressBar,
                               final Button errorButton) {

        if (BuildConfig.DEBUG) {
            Timber.d("Load image position: %d", position);
        }

        progressBar.show();
        errorButton.setVisibility(GONE);
        if (images == null || images.length == 0) {
            imageView.setImageBitmap(null);
            progressBar.setVisibility(GONE);
        } /*else {
            CustomPicasso.getInstance().loadImage(imageView,
                    String.format("%s%s",
                            ApplicationController.get(getContext()).imagesUrl,
                            images[position]), callback);
        }*/
    }


    public void setDisplayMode(ImageView.ScaleType scaleType) {
        imageView.setScaleType(scaleType);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
