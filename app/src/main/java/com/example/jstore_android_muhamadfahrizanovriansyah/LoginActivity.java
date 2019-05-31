package com.example.jstore_android_muhamadfahrizanovriansyah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.support.design.widget.TextInputLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.text.Html.fromHtml;

public class LoginActivity extends AppCompatActivity {
    private int id = 0;
    // session
    SharedPreferences pref;
    //activity
    Intent mainIntent;


    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainIntent = new Intent(LoginActivity.this, MainActivity.class);

        //cek session
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("Email") && pref.contains("Password")){
            startActivity(mainIntent);
        }

        final EditText emailInput = (EditText) findViewById(R.id.emaiInput);
        final EditText passInput = (EditText) findViewById(R.id.passInput);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String email = emailInput.getText().toString();
                final String password = passInput.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse != null) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                builder1.setMessage("Login Success!").create().show();
                                id = jsonResponse.getInt("id");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("Email",email);
                                editor.putString("Password",password);
                                editor.commit();
                                mainIntent.putExtra("currentUserId", id);
                                startActivity(mainIntent);
                                finish();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage("Login Failed!").create().show();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(email,password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
        final TextView registerClickable = (TextView) findViewById(R.id.registerHere);
        registerClickable.setText(fromHtml("<font color='Brown'>Belum Memiliki Akun? </font><font color='Red'>Buat disini</font>"));
        registerClickable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent regisIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regisIntent);
            }
        });

    }


}
