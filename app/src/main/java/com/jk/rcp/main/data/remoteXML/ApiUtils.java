package com.jk.rcp.main.data.remoteXML;

import com.jk.rcp.main.data.remote.APIService;

public class ApiUtils {

    public static final String BASE_URL = "http://contenidos.inpres.gov.ar/";

    private ApiUtils() {
    }

    public static APIService getAPIService() {

        return RetrofitXMLClient.getClient(BASE_URL).create(APIService.class);
    }
}