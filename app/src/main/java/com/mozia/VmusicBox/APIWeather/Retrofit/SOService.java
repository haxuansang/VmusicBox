package com.mozia.VmusicBox.APIWeather.Retrofit;



import com.mozia.VmusicBox.APIWeather.SOAnswersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SOService {
    @GET("yql")
    Call<SOAnswersResponse> getAnswers(@Query("q") String q, @Query("format") String format);

}
