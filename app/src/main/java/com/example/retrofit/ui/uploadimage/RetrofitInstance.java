package com.example.retrofit.ui.uploadimage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {


    public static String BASE_URL= "https://personalexpo.pythonanywhere.com/";
    private static Retrofit retrofit;
    public static Retrofit getRetrofitClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

}
