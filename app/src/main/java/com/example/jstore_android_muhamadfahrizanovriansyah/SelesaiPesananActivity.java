package com.example.jstore_android_muhamadfahrizanovriansyah;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelesaiPesananActivity extends AppCompatActivity {

    private int currentUserId = 0;
    private int currentInvoiceId = 0;
    private String nama_item = "0";

    TextView staticIdInvoice;
    TextView staticNamaCustomer;
    TextView staticNamaItem;
    TextView staticTanggalPesan;
    TextView staticTotalBiaya;
    TextView staticStatusInvoice;
    TextView staticTipeInvoice;
    TextView staticDueDate;
    TextView staticPeriodeInstallment;

    TextView idInvoice;
    TextView namaCustomer;
    TextView namaItem;
    TextView tanggalPesan;
    TextView totalBiaya;
    TextView statusInvoice;
    TextView tipeInvoice;
    TextView dueDate;
    TextView periodeInstallment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_pesanan);

        staticIdInvoice = (TextView) findViewById(R.id.staticIdInvoice);
        staticNamaCustomer = (TextView) findViewById(R.id.staticNamaCustomer);
        staticNamaItem = (TextView) findViewById(R.id.staticNamaItem);
        staticTanggalPesan = (TextView) findViewById(R.id.staticTanggalPesan);
        staticTotalBiaya = (TextView) findViewById(R.id.staticTotalBiaya);
        staticStatusInvoice = (TextView) findViewById(R.id.staticStatusInvoice);
        staticTipeInvoice = (TextView) findViewById(R.id.staticTipeInvoice);
        staticDueDate = (TextView) findViewById(R.id.staticDueDate);
        staticPeriodeInstallment = (TextView) findViewById(R.id.staticPeriodeInstallment);

        staticIdInvoice.setVisibility(View.GONE);
        staticNamaCustomer.setVisibility(View.GONE);
        staticNamaItem.setVisibility(View.GONE);
        staticTanggalPesan.setVisibility(View.GONE);
        staticTotalBiaya.setVisibility(View.GONE);
        staticStatusInvoice.setVisibility(View.GONE);
        staticTipeInvoice.setVisibility(View.GONE);
        staticDueDate.setVisibility(View.GONE);
        staticPeriodeInstallment.setVisibility(View.GONE);

        idInvoice = (TextView) findViewById(R.id.idInvoice);
        namaCustomer = (TextView) findViewById(R.id.namaCustomer);
        namaItem = (TextView) findViewById(R.id.namaItem);
        tanggalPesan = (TextView) findViewById(R.id.tanggalPesan);
        totalBiaya = (TextView) findViewById(R.id.totalBiaya);
        statusInvoice = (TextView) findViewById(R.id.statusInvoice);
        tipeInvoice = (TextView) findViewById(R.id.tipeInvoice);
        dueDate = (TextView) findViewById(R.id.dueDate);
        periodeInstallment = (TextView) findViewById(R.id.periodeInstallment);

        idInvoice.setVisibility(View.GONE);
        namaCustomer.setVisibility(View.GONE);
        namaItem.setVisibility(View.GONE);
        tanggalPesan.setVisibility(View.GONE);
        totalBiaya.setVisibility(View.GONE);
        statusInvoice.setVisibility(View.GONE);
        tipeInvoice.setVisibility(View.GONE);
        dueDate.setVisibility(View.GONE);
        periodeInstallment.setVisibility(View.GONE);

        final Button batal = (Button) findViewById(R.id.batal);
        final Button selesai = (Button) findViewById(R.id.selesai);

        if(getIntent().getExtras()!=null)
        {
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId", 0);
            currentInvoiceId = intent.getIntExtra("currentInvoiceId", 0);
        }

        fetchPesanan();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(SelesaiPesananActivity.this);
        builder1.setMessage(Integer.toString(currentInvoiceId)).create().show();

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Pembatalan Pesanan Success").create().show();

                                Intent mainIntent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                                mainIntent.putExtra("currentUserId", currentUserId);
                                startActivity(mainIntent);
                            }
                        }
                        catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                            builder.setMessage("Pembatalan Pesanan Failed").create().show();
                        }
                    }
                };

                PesananBatalRequest pesananBatalRequest = new PesananBatalRequest(currentInvoiceId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(pesananBatalRequest);
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Penyelesaian Pesanan Success").create().show();

                                Intent mainIntent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                                mainIntent.putExtra("currentUserId", currentUserId);
                                startActivity(mainIntent);
                            }
                        }
                        catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                            builder.setMessage("Penyelesaian Pesanan Failed").create().show();
                        }
                    }
                };

                PesananSelesaiRequest pesananSelesaiRequest = new PesananSelesaiRequest(currentInvoiceId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(pesananSelesaiRequest);
            }
        });
    }



    public void fetchPesanan()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    if (jsonResponse != null) {

                        staticIdInvoice.setVisibility(View.VISIBLE);
                        staticNamaCustomer.setVisibility(View.VISIBLE);
                        staticNamaItem.setVisibility(View.VISIBLE);
                        staticTanggalPesan.setVisibility(View.VISIBLE);
                        staticTotalBiaya.setVisibility(View.VISIBLE);
                        staticStatusInvoice.setVisibility(View.VISIBLE);
                        staticTipeInvoice.setVisibility(View.VISIBLE);

                        idInvoice.setVisibility(View.VISIBLE);
                        namaCustomer.setVisibility(View.VISIBLE);
                        namaItem.setVisibility(View.VISIBLE);
                        tanggalPesan.setVisibility(View.VISIBLE);
                        totalBiaya.setVisibility(View.VISIBLE);
                        statusInvoice.setVisibility(View.VISIBLE);
                        tipeInvoice.setVisibility(View.VISIBLE);


                        for (int i = jsonResponse.length()-1; i < jsonResponse.length(); i++) {
                            JSONObject invoice = jsonResponse.getJSONObject(i);
                            int id_invoice = invoice.getInt("id");
                            String item = invoice.getString("item");
                            String date = invoice.getString("date");
                            int total_price = invoice.getInt("totalPrice");

                            String invoice_status = invoice.getString("invoiceStatus");
                            String invoice_type = invoice.getString("invoiceType");

                            if(invoice_status.equals("Installment"))
                            {
                                staticPeriodeInstallment.setVisibility(View.VISIBLE);
                                periodeInstallment.setVisibility(View.VISIBLE);
                                int installment_period = invoice.getInt("installmentPeriod");
                                periodeInstallment.setText(Integer.toString(installment_period));
                            }
                            else if(invoice_status.equals("Unpaid"))
                            {
                                staticDueDate.setVisibility(View.VISIBLE);
                                dueDate.setVisibility(View.VISIBLE);
                                String due_date = invoice.getString("dueDate");
                                dueDate.setText(due_date);
                            }
                            JSONObject customer = invoice.getJSONObject("customer");

                            String name_customer = customer.getString("name");

                            item = item.substring(1, item.length()-1);
                            fetchItem(Integer.valueOf(item));

                            idInvoice.setText(Integer.toString(id_invoice));
                            namaCustomer.setText(name_customer);
                            tanggalPesan.setText(date);
                            totalBiaya.setText(Integer.toString(total_price));
                            statusInvoice.setText(invoice_status);
                            tipeInvoice.setText(invoice_type);

                            if(currentInvoiceId == 0)
                            {
                                Intent mainIntent = new Intent(SelesaiPesananActivity.this, SelesaiPesananActivity.class);
                                mainIntent.putExtra("currentUserId", currentUserId);
                                mainIntent.putExtra("currentInvoiceId", id_invoice);
                                startActivity(mainIntent);
                            }
                        }
                    }
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Fetch Pesanan Failed").create().show();

                    Intent mainIntent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                    mainIntent.putExtra("currentUserId", currentUserId);
                    startActivity(mainIntent);
                }
            }
        };

        PesananFetchRequest pesananFetchRequest = new PesananFetchRequest(currentUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
        queue.add(pesananFetchRequest);
    }

    public void fetchItem(int id)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse != null) {
                        nama_item = jsonResponse.getString("name");
                        namaItem.setText(nama_item);
                    }
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Fetch Item Failed").create().show();
                }
            }
        };

        ItemRequest itemRequest = new ItemRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
        queue.add(itemRequest);
    }
}
