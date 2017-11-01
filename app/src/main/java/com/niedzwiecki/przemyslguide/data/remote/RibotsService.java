package com.niedzwiecki.przemyslguide.data.remote;

import com.bumptech.glide.request.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.niedzwiecki.przemyslguide.data.model.Place;
import com.niedzwiecki.przemyslguide.data.model.Places;
import com.niedzwiecki.przemyslguide.data.model.PlacesResponse;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.data.model.SuppliesModel;
import com.niedzwiecki.przemyslguide.ui.placeDetails.ImagePage;
import com.niedzwiecki.przemyslguide.util.MyGsonTypeAdapterFactory;

public interface RibotsService {

    String ENDPOINT = "http://www.mocky.io/";
    String CITY_GUID_ENDPOINT = "http://51.15.34.44/api/";

    @GET("places")
    Observable<List<Place>> getRibots();

/*

    @GET("places")
    Observable<PlacesResponse> getRibots();
*/

/*
    @GET("v2/59e58509110000b00bec68ff")
    Observable<PlacesResponse> getRibots();
*/

    Observable<SuppliesModel> getSupplies(String format);

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static RibotsService newRibotsService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    response.header("application/json");
                    return response;
                }
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(RibotsService.CITY_GUID_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RibotsService.class);
        }

        public static GsonBuilder provideGson() {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            return gsonBuilder;
        }
    }
}
