package com.niedzwiecki.przemyslguide.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public interface GuideService {

    String CITY_GUID_ENDPOINT = "http://51.15.34.44/api/";

    @GET("places")
    Observable<List<PlaceOfInterest>> getPlaces();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static Retrofit newRibotsService() {
            Gson gson = new GsonBuilder()
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
                    .baseUrl(GuideService.CITY_GUID_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit;
        }

        public static GsonBuilder provideGson() {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            return gsonBuilder;
        }
    }
}
