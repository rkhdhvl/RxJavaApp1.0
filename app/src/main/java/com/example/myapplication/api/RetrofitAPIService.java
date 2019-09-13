package com.example.myapplication.api;



import com.example.myapplication.model.Quotes;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitAPIService {

    // https://programming-quotes-api.herokuapp.com/quotes/lang/en

    @GET("quotes/lang/en")
    Observable<List<Quotes>> emittedItems();

}
