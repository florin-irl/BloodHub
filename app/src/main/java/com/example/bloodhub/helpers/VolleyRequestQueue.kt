package com.example.bloodhub.helpers

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.bloodhub.ApplicationController

object VolleyRequestQueue {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(ApplicationController.instance.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}