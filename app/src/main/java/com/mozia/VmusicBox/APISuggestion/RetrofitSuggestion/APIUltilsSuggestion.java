package com.mozia.VmusicBox.APISuggestion.RetrofitSuggestion;

import android.util.Log;

import com.mozia.VmusicBox.APIWeather.Retrofit.RetrofitClient;
import com.mozia.VmusicBox.APIWeather.Retrofit.SOService;

public class APIUltilsSuggestion {
    public static final String baseURL="https://api.soundcloud.com/playlists/";




    public static SOServiceSuggestion getSOService()
    {


        return RetrofitClientSuggestion.getClient(baseURL).create(SOServiceSuggestion.class);
    }
}
