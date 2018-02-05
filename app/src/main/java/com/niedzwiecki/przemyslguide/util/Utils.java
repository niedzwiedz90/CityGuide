package com.niedzwiecki.przemyslguide.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Transformation;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import rx.Subscription;

/**
 * Created by niedzwiedz on 17.08.17.
 */

public class Utils {

    public static final int TOAST_OFFSET = 150;

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(@Nullable final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(@Nullable final Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    private static final String EMAIL_PATTERN =
            "[A-Z0-9a-z._%+-]{2,}";

    public static boolean isValidEmail(CharSequence target) {
        return target != null && Pattern.compile(EMAIL_PATTERN).matcher(target).matches();
    }

    public static boolean isPasswordLengthMinimumFourChars(@Nullable CharSequence str) {
        return str != null && str.length() >= 4;
    }


    public static Transformation getPicassoCircularTransformations() {
        return new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                return Utils.getCircularBitmapImage(source);
            }

            @Override
            public String key() {
                return "picasso_circular";
            }
        };
    }

    public static Bitmap getCircularBitmapImage(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    public static String encode(String key, @NonNull String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Base64.encodeToString(sha256_HMAC.doFinal(data.getBytes()), Base64.URL_SAFE);
    }

    public static View setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }

        return view;
    }

    public static void unsubscribe(@Nullable Subscription subscribe) {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    public static int convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }
}
