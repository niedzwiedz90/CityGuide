package com.niedzwiecki.przemyslguide.ui;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by niedzwiedz on 03.09.17.
 */

public class PlaceDetailViewPager extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
/*
    final ArrayList views = new ArrayList();

    @Override
    public int getCount() {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImagePage imagePage = null;
        for (ImagePage v : views) {
            if (v.getParent() == null) {
                imagePage = v;
                break;
            }
        }

        if (imagePage == null) {
            views.add(imagePage = new ImagePage(container.getContext()));
        }

        if (scaleType != null) {
            imagePage.setDisplayMode(scaleType);
        }

        if (!loadOnlyFirstImage || position == firstImagePosition) {
            imagePage.setData(position, images);

            if (position != firstImagePosition
                    && position + EstatesAdapter.PRELOAD_IMAGES_COUNT < images.length) {
                Timber.d("Loaded image to cache. Position: %d",
                        position + EstatesAdapter.PRELOAD_IMAGES_COUNT);
                CustomPicasso.preLoad(
                        imagePage.getContext(),
                        ApplicationController.get(imagePage.getContext()).imagesUrl
                                + images[position + EstatesAdapter.PRELOAD_IMAGES_COUNT]
                );
            }
        }

        container.addView(imagePage, 0);
        imagePage.setTag(tag);
        imagePage.setTag(R.attr.position, position);
        imagePage.setOnClickListener(onClickListener);
        return imagePage;

        return super.instantiateItem(container, position);
    }*/
}
