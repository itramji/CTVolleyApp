package com.bornbytes.ctvolleyapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import com.babylon.certificatetransparency.certificateTransparencyHostnameVerifier
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class VolleySingleton private constructor(private val mContext: Context) {
    private var mRequestQueue: RequestQueue?
    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue =
                    Volley.newRequestQueue(mContext.applicationContext, object : HurlStack() {
                        override fun createConnection(url: URL): HttpURLConnection {
                            val connection = super.createConnection(url)
                            if (connection is HttpsURLConnection) {
                                connection.hostnameVerifier = certificateTransparencyHostnameVerifier(connection.hostnameVerifier) {
                                    // Add ur base url here
                                    +"*.google.com"
                                }
                            }
                            return connection
                        }
                    })
            }
            // Return RequestQueue
            return mRequestQueue
        }

    fun <T> addToRequestQueue(request: Request<T>?) { // Add the specified request to the request queue
        requestQueue!!.add(request)
    }

    companion object {
        private var mInstance: VolleySingleton? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): VolleySingleton? { // If Instance is null then initialize new Instance
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance
        }
    }

    init {
        mRequestQueue = requestQueue
    }
}