package com.lambdastack.go;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lambdastack.go.models.ValueStreamMap;

import java.io.IOException;

public class RestClient {

    public ValueStreamMap getValueStreamMapFromGoServer(String url) throws IOException {
        HttpRequestFactory httpRequestFactory = getHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser(getJsonFactory()));
            }
        });

        GenericUrl requestUrl = new GenericUrl(url);

        HttpRequest request = httpRequestFactory.buildGetRequest(requestUrl);
        return request.execute().parseAs(ValueStreamMap.class);
    }

    protected HttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    protected JsonFactory getJsonFactory() {
        return new JacksonFactory();
    }
}
