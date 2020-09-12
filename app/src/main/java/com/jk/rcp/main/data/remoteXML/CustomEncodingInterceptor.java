package com.jk.rcp.main.data.remoteXML;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CustomEncodingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        MediaType oldMediaType = MediaType.parse(response.header("Content-Type"));
        // update only charset in mediatype
        MediaType newMediaType = MediaType.parse(oldMediaType.type() + "/" + oldMediaType.subtype() + "; charset='ISO-8859-1'");
        // update body
        ResponseBody newResponseBody = ResponseBody.create(newMediaType, response.body().bytes());

        return response.newBuilder()
                .removeHeader("Content-Type")
                .addHeader("Content-Type", newMediaType.toString())
                .body(newResponseBody)
                .build();
    }
}