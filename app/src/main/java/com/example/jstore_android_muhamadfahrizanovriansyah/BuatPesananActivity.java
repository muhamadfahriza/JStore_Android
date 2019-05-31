package com.example.jstore_android_muhamadfahrizanovriansyah;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuatPesananActivity extends AppCompatActivity {

    Intent intent = getIntent();

    private int currentUserId;
    private int itemId;
    private String itemName = "0";
    private String itemCategory = "0";
    private String itemStatus = "0";
    private double itemPrice = 0;
    private int installmentPeriod = 0;
    private String selectedPayment = "paid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pesanan);

        final TextView textPeriod = (TextView) findViewById(R.id.textPeriod);
        final TextView item_name = (TextView) findViewById(R.id.item_name);
        final TextView item_category = (TextView) findViewById(R.id.item_category);
        final TextView item_status = (TextView) findViewById(R.id.item_status);
        final TextView item_price = (TextView) findViewById(R.id.item_price);

        final EditText installment_period = (EditText) findViewById(R.id.installment_period);

        final TextView total_price = (TextView) findViewById(R.id.total_price);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        final Button hitung = (Button) findViewById(R.id.hitung);
        final Button pesan = (Button) findViewById(R.id.pesan);

        if(getIntent().getExtras()!=null)
        {
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId", 0);
            itemId = intent.getIntExtra("itemId", 0);
            itemName = intent.getStringExtra("itemName");
            itemCategory = intent.getStringExtra("itemCategory");
            itemStatus = intent.getStringExtra("itemStatus");
            itemPrice = intent.getIntExtra("itemPrice", 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(BuatPesananActivity.this);
            builder.setMessage("Current Id = " + currentUserId).create().show();
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,248,0,0);
        total_price.setLayoutParams(params);

        pesan.setVisibility(View.GONE);
        textPeriod.setVisibility(View.GONE);
        installment_period.setVisibility(View.GONE);

        item_name.setText(itemName);
        item_category.setText(itemCategory);
        item_status.setText(itemStatus);
        item_price.setText(Double.toString(itemPrice));
        total_price.setText("0");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.installment)
                {
                    textPeriod.setVisibility(View.VISIBLE);
                    installment_period.setVisibility(View.VISIBLE);
                    params.setMargins(0,0,0,0);
                    total_price.setLayoutParams(params);
                    selectedPayment = "installment";

                }
                else if(checkedId == R.id.unpaid)
                {
                    textPeriod.setVisibility(View.GONE);
                    installment_period.setVisibility(View.GONE);
                    params.setMargins(0,248,0,0);
                    total_price.setLayoutParams(params);
                    selectedPayment = "unpaid";
                }
                else if(checkedId == R.id.paid)
                {
                    textPeriod.setVisibility(View.GONE);
                    installment_period.setVisibility(View.GONE);
                    params.setMargins(0,248,0,0);
                    total_price.setLayoutParams(params);
                    selectedPayment = "paid";
                }
                hitung.setVisibility(View.VISIBLE);
                pesan.setVisibility(View.GONE);
            }
        });

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPayment.equals("paid") || selectedPayment.equals("unpaid"))
                {
                    total_price.setText(Double.toString(itemPrice));
                }
                else if(selectedPayment.equals("installment"))
                {
                    itemPrice /= Double.valueOf(installment_period.getText().toString());
                    total_price.setText(Double.toString(itemPrice));
                }

                hitung.setVisibility(View.GONE);
                pesan.setVisibility(View.VISIBLE);

            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final ArrayList<Integer> listItem = new ArrayList<>();
                final String id_customer = Integer.toString(currentUserId);

                listItem.add(itemId);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuatPesananActivity.this);
                                builder.setMessage("Buat Pesanan Success").create().show();
                                Intent mainIntent = new Intent(BuatPesananActivity.this, MainActivity.class);
                                mainIntent.putExtra("currentUserId", currentUserId);
                                startActivity(mainIntent);
                            }
                        }
                        catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BuatPesananActivity.this);
                            builder.setMessage("Buat Pesanan Failed").create().show();
                        }
                    }
                };

                BuatPesananRequest buyRequest = null;
                if(selectedPayment.equals("paid"))
                {
                    buyRequest = new BuatPesananRequest(listItem, id_customer, responseListener);
                }
                else if(selectedPayment.equals("unpaid"))
                {
                    buyRequest = new BuatPesananRequest(listItem, id_customer, responseListener, true);
                }
                else if(selectedPayment.equals("installment"))
                {
                    installmentPeriod = Integer.valueOf(installment_period.getText().toString());
                    buyRequest = new BuatPesananRequest(listItem, installmentPeriod, id_customer, responseListener);
                }

                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(buyRequest);
            }
        });

    }
}
