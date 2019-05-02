package com.example.jstore_android_muhamadfahrizanovriansyah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Supplier> listSupplier = new ArrayList<>();
    private ArrayList<Item> listItem = new ArrayList<>();
    private HashMap<Supplier, ArrayList<Item>> childMapping = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    protected void refreshList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        if (jsonResponse != null) {
                            for(int i = 0; i < jsonResponse.length(); i++){
                                JSONObject item = jsonResponse.getJSONObject(i);
                                JSONObject supplier = item.getJSONObject("supplier");
                                JSONObject location = supplier.getJSONObject("location");
                                Location tempatTinggal = new Location("Jawa Barat", "Tempat Tinggal", "Kuningan");
                                Supplier s1 = new Supplier(1, "fahriza","fahriza@gmail.com", "085755783673", tempatTinggal);

                                Item item1 = new Item(1,"SSD WD Green  240gb ",499000, "ELECTRONIC","NEW", s1);

                                childMapping.put(listSupplier.get(i),listItem);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        };
    }
}
