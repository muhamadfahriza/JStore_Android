package com.example.jstore_android_muhamadfahrizanovriansyah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PesananFetchRequest extends StringRequest {
    public static final String URL_GET_INVOICE = "http://10.0.2.2:8080/invoicecustomer/";

    public PesananFetchRequest(int id, Response.Listener<String> listener) {
        super(URL_GET_INVOICE + id, listener, null);
    }
}