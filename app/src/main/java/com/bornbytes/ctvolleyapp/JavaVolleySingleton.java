package com.bornbytes.ctvolleyapp;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.babylon.certificatetransparency.CTHostnameVerifierBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


public class JavaVolleySingleton {
    private static JavaVolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private JavaVolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized JavaVolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new JavaVolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(), new HurlStack(){
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpURLConnection connection =  super.createConnection(url);
                    enableCTCheck(connection);
                    return connection;
                }
            });
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    private void enableCTCheck(HttpURLConnection connection) {
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
            HostnameVerifier verifier = new CTHostnameVerifierBuilder(httpsURLConnection.getHostnameVerifier()).includeHost("").build();
            httpsURLConnection.setHostnameVerifier(verifier);
        }
    }
}
