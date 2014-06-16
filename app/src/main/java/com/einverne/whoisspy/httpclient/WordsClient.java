package com.einverne.whoisspy.httpclient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by EinVerne on 2014/6/13.
 */
public class WordsClient{
    public static final String BASE_URL = "http://dl.dropboxusercontent.com/s/4ksafxbceji2fk4/words.json";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(JsonHttpResponseHandler responseHandler){
        client.get(BASE_URL, responseHandler);
    }
}
