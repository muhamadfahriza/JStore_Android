package com.example.jstore_android_muhamadfahrizanovriansyah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PesananSelesaiRequest extends StringRequest {
    public static final String URL_FINISH_TRANSACTION = "http://10.0.2.2:8080/finishtransaction/";

    public PesananSelesaiRequest(int id, Response.Listener<String> listener) {
        super(Method.POST, URL_FINISH_TRANSACTION + id, listener, null);
    }
}