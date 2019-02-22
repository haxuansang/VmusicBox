package com.mozia.VmusicBox.APIWeather.Retrofit;

public class APIUtils {
    public static final String baseURL="https://query.yahooapis.com/v1/public/";
    public static final String typeOfJson="json";
    public static SOService getSOService()
    {
        return RetrofitClient.getClient(baseURL).create(SOService.class);
    }
}
