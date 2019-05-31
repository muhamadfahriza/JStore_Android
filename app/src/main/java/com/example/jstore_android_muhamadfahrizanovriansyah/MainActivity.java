package com.example.jstore_android_muhamadfahrizanovriansyah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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
    ExpandableListAdapter listAdapter;
    ExpandableListView lvMain;

    private int currentUserId;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ExpandableListView expListView = (ExpandableListView) findViewById(R.id.lvExp);
        final Button pesananButton = (Button) findViewById(R.id.pesanan);
        if(getIntent().getExtras()!=null)
        {
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId", 0);
        }

        lvMain = expListView;

        refreshList();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Item selected = childMapping.get(listSupplier.get(groupPosition)).get(childPosition);

                Intent buatPesananIntent = new Intent(MainActivity.this, BuatPesananActivity.class);
                buatPesananIntent.putExtra("currentUserId", currentUserId);
                buatPesananIntent.putExtra("itemId", selected.getId());
                buatPesananIntent.putExtra("itemName", selected.getName());
                buatPesananIntent.putExtra("itemCategory", selected.getCategory());
                buatPesananIntent.putExtra("itemStatus", selected.getStatus());
                buatPesananIntent.putExtra("itemPrice", selected.getPrice());
                startActivity(buatPesananIntent);
                return false;
            }
        });

        pesananButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selesaiPesananIntent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
                selesaiPesananIntent.putExtra("currentUserId", currentUserId);
                startActivity(selesaiPesananIntent);
            }
        });

    }
    protected void refreshList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    if (jsonResponse != null) {

                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject item = jsonResponse.getJSONObject(i);
                            int idItem = item.getInt("id");
                            String nameItem = item.getString("name");
                            int price = item.getInt("price");
                            String category = item.getString("category");
                            String status = item.getString("status");

                            JSONObject supplier = item.getJSONObject("supplier");

                            int id = supplier.getInt("id");
                            String name = supplier.getString("name");
                            String email = supplier.getString("email");
                            String phoneNumber = supplier.getString("phoneNumber");

                            JSONObject location = supplier.getJSONObject("location");

                            String province = location.getString("province");
                            String description = location.getString("description");
                            String city = location.getString("city");

                            Location loc = new Location(province, description, city);
                            Supplier sup = new Supplier(id, name, email, phoneNumber, loc);

                            Item ite = new Item(idItem, nameItem, price, category, status, sup);
                            listSupplier.add(sup);
                            listItem.add(ite);

                            childMapping.put(listSupplier.get(i), listItem);
                        }
                    }
                } catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Main Failed").create().show();
                }

                listAdapter = new MainListAdapter(MainActivity.this, listSupplier, childMapping);
                lvMain.setAdapter(listAdapter);
            }
        };

        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            prf = getSharedPreferences("user_details",MODE_PRIVATE);
            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
