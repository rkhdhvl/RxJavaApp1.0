package com.example.myapplication.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFactory {

    private static Retrofit retrofitClientFactoryInstance;

    public static Retrofit getInstance()
    {
       if(retrofitClientFactoryInstance == null)
       {
           retrofitClientFactoryInstance = new Retrofit.Builder()
                                            .baseUrl("https://programming-quotes-api.herokuapp.com/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                            .build();
       }
       return retrofitClientFactoryInstance;
    }

    public RetrofitClientFactory() {
    }
}
