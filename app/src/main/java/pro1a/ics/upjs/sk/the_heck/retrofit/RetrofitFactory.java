package pro1a.ics.upjs.sk.the_heck.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    public static Retrofit getRetrofit() {
        String baseUrl = "http://192.168.0.106:8076/heck/";

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HeckAPI getApi() {
        return getRetrofit().create(HeckAPI.class);
    }
}
