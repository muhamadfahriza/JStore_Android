package com.example.jstore_android_muhamadfahrizanovriansyah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ItemRequest extends StringRequest {
    public static final String URL_ITEMS = "http://10.0.2.2:8080/items/";

    public ItemRequest(int id, Response.Listener<String> listener) {
        super(URL_ITEMS + id, listener, null);
    }
}