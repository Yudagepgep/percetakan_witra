package com.example.percetakan_witra.LOGIN;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;



public class Register extends AppCompatActivity {
    EditText name,email,kode,hp,almt,pass,c_pass;
    Button regis,ket;
    ProgressBar loading;
    TextView login;
    CheckBox st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name=findViewById(R.id.nama);
        email=findViewById(R.id.email);
        kode=findViewById(R.id.kode_pos);
        almt=findViewById(R.id.alamat);
        hp=findViewById(R.id.handphone);
        pass=findViewById(R.id.pass);
        st=findViewById(R.id.chek);
        c_pass=findViewById(R.id.confirm_pass);
        regis=findViewById(R.id.register);
        ket = findViewById(R.id.btn_ket);

        ket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Register.this);
                builder.setTitle("Catatan :");
                builder.setMessage("Harap diperiksa data-data nya sebelum klik REGISTER");
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

        loading = findViewById(R.id.loading);




        login = findViewById(R.id.back_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Daftar();
            }
        });



    }





    private void Daftar() {


        loading.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        final String nm_lengkap     = this.name.getText().toString().trim();
        final String email_user     = this.email.getText().toString().trim();
        final String pos            = this.kode.getText().toString().trim();
        final String hand           = this.hp.getText().toString().trim();
        final String alamat_user    = this.almt.getText().toString().trim();
        final String pass_user      = this.pass.getText().toString().trim();


        String url = "https://percetakanwitra001.000webhostapp.com/User/Register.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    // Check for error node in json
                    if (success.equals("1")) {


                        Toast.makeText(Register.this,"Register Berhasil", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register.this,"Register Berhasil", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    regis.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this,"Jaringan Tidak Ada \n Periksa Kembali Jaringan Anda", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                regis.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_pelanggan", nm_lengkap);
                params.put("email", email_user);
                params.put("kode_pos", pos);
                params.put("no_hp", hand);
                params.put("alamat", alamat_user);
                params.put("password", pass_user);



                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }


}

