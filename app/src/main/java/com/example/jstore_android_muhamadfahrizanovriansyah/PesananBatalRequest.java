package com.example.jstore_android_muhamadfahrizanovriansyah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PesananBatalRequest extends StringRequest {
    public static final String URL_CANCEL_TRANSACTION = "http://10.0.2.2:8080/canceltransaction/";

    public PesananBatalRequest(int id, Response.Listener<String> listener) {
        super(Method.POST, URL_CANCEL_TRANSACTION + id, listener, null);
    }
}