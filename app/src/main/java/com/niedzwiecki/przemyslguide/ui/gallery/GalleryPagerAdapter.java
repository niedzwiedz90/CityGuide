package com.niedzwiecki.przemyslguide.ui.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.niedzwiecki.przemyslguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.niedzwiecki.przemyslguide.util.Utils.convertDpToPixel;

/**
 * Created by niedzwiedz on 03.09.17.
 */

public class GalleryPagerAdapter extends PagerAdapter {

    private List<String> images;
    private Context context;
    private LayoutInflater layoutInflater;

    public GalleryPagerAdapter(Context context) {
        images = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(
                R.layout.fragment_screen_slide_page,
                container,
                false
        );

        ImageView imageView = (ImageView) itemView.findViewById(R.id.placeImage);
        Picasso.with(context)
                .load(images.get(position))
                .resize(convertDpToPixel(400f, context), convertDpToPixel(250f, context))
                .centerCrop()
                .into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setItems(List<String> images) {
        this.images = images;
    }
}
