package com.niedzwiecki.przemyslguide.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.niedzwiecki.przemyslguide.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;


public class CustomPicasso {

    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private static CustomPicasso instance;

    public static void init(Context context) {
        Picasso.setSingletonInstance(new Picasso.Builder(context)
//                .downloader(new OkHttp3Downloader(context.getCacheDir(), MAX_DISK_CACHE_SIZE))
                .build());
        if (instance == null) {
            instance = new CustomPicasso();
        }
    }

    public static CustomPicasso getInstance() {
        return instance;
    }

    //Method for setup mock picasso
    public static void setInstance(CustomPicasso instance) {
        CustomPicasso.instance = instance;
    }

    public static void preLoad(Context context, String url) {
        Picasso.with(context).load(url).fetch();
    }

    public void loadImage(final ImageView imageView, String url, final Callback callback) {
        loadImage(imageView, url, callback, true);
    }

    public void loadImage(final ImageView imageView,
                          final String url,
                          final Callback callback,
                          final boolean fadeIn) {
      /*  if (ApplicationController.get(imageView.getContext()).testMode) {
            imageView.setImageResource(R.drawable.test_place_holder);
            return;
        }
*/
        RequestCreator load = Picasso.with(imageView.getContext()).load(url);
        load.fetch(callback);
        load.noFade();
        load.into(imageView, new Callback() {
            @Override
            public void onSuccess() {
  /*              if (!url.equals(imageView.getTag(R.attr.image_tag_url))) {
                    if (fadeIn) {
                        imageView.setAlpha(0f);
                        imageView.animate().alpha(1).setDuration(400).start();
                    }
                    imageView.setTag(R.attr.image_tag_url, url);
                }
  */          }

            @Override
            public void onError() {

            }
        });
    }

    public void loadCircularImage(ImageView profileImageView, String userImage) {
        Picasso.with(profileImageView.getContext())
                .load(userImage)
                .transform(new CircleTransform()).into(profileImageView);
    }

    public void loadCircularImage(ImageView profileImageView, @DrawableRes int drawableRes) {
        Picasso.with(profileImageView.getContext())
                .load(drawableRes)
                .transform(new CircleTransform()).into(profileImageView);
    }

    class CircleTransform implements Transformation {
        Bitmap bitmap;

        @Override
        public Bitmap transform(Bitmap source) {
//            if (!source.isMutable()) return source;
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);

            int minEdge = Math.min(source.getWidth(), source.getHeight());
            int dx = (source.getWidth() - minEdge) / 2;
            int dy = (source.getHeight() - minEdge) / 2;

            // Init shader
            Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setTranslate(-dx, -dy);   // Move the target area to center of the source bitmap
            shader.setLocalMatrix(matrix);

            // Init paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(shader);

            // Create and draw circle bitmap
            Bitmap output = Bitmap.createBitmap(minEdge, minEdge, bitmap.getConfig());
            Canvas canvas = new Canvas(output);
            canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint);

            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);
            paint1.setStrokeWidth(4);
            canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint1);

            // Recycle the source bitmap, because we already generate a new one
            source.recycle();
            bitmap.recycle();

            return output;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}
