package com.jk.rcp.main.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://api.rcp.margaale.ddnss.de/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}