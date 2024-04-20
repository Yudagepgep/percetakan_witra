package com.example.percetakan_witra.LOGIN;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.Menu;
import com.example.percetakan_witra.R;
import com.example.percetakan_witra.View.View_Cetak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Login  extends AppCompatActivity {

    EditText mail,pas;
    Button login_user,register_user,ket;
    ProgressBar loading;
    shared_prefences shared_prefences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mail= findViewById(R.id.edt_email);
        pas = findViewById(R.id.edt_password);
        loading=findViewById(R.id.loading);
        ket = findViewById(R.id.btn_ket);

        ket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Catatan :");
                builder.setMessage("Sebelum memesan suatu barang, harap login terlebih dahulu dengan mengisi form diatas \n " +
                        "Jika belum memiliki akun harap register terlebih dahulu ");
                builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        shared_prefences = new shared_prefences(this);

        login_user=findViewById(R.id.btn_Login);
        register_user=findViewById(R.id.btn_register);

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail = mail.getText().toString();
                String mPassword = pas.getText().toString();
                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                    masuk(mEmail, mPassword);
                } else {
                    mail.setError("Mohon Masukan Email");
                    pas.setError("Mohon Masukan Password");
                }
            }

        });

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void masuk(final String mEmail, final String mPassword) {

        loading.setVisibility(View.VISIBLE);

        String user ="https://percetakanwitra001.000webhostapp.com/User/Login.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);


                            String id = object.getString("id_pelanggan").trim();

                            shared_prefences.usersession(id);

                            Intent intent = new Intent(Login.this, Menu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            loading.setVisibility(View.GONE);

                        }
                    }else {
                        Toast.makeText(Login.this, "Login Gagal \n Email Atau Password Anda Salah" , Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Login Gagal \n Mohon Buat Akun Baru \n Dengan Email Berbeda \n" , Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    login_user.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                loading.setVisibility(View.GONE);
                login_user.setVisibility(View.VISIBLE);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mEmail);
                params.put("password", mPassword);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }

}
