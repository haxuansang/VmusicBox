package com.mozia.VmusicBox.APISuggestion.RetrofitSuggestion;

import com.mozia.VmusicBox.APISuggestion.ResponseSuggestion;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface SOServiceSuggestion {

     @GET
    Call<ResponseSuggestion> getAnswers(@Url String idOfPlaylist,@Query("client_id") String client_id, @Query("offset") String offset, @Query("limit") String limit);
}
