package com.example.percetakan_witra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.percetakan_witra.Informasi.Informasi;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.LOGIN.Login;
import com.example.percetakan_witra.LOGIN.Register;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.List_Data.List_data;
import com.example.percetakan_witra.Profil.Data_Profil;
import com.example.percetakan_witra.View.View_Cetak;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.nio.file.Paths.get;

public class Menu extends AppCompatActivity {
    TextView nama;
    CircleImageView photo;
    Toolbar tool;
    private static String TAG = Menu.class.getSimpleName();
    shared_prefences shared_prefences;
    Button exit,ket;
    String getId;
    CardView pesan, lihat, informasi, list_cetak;
    ImageView iklan1, iklan2, iklan3;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        photo = findViewById(R.id.foto_profil);
        nama = findViewById(R.id.nama_pelanggan);
        exit = findViewById(R.id.keluar);
        iklan1 = findViewById(R.id.image_pesan);
        iklan2 = findViewById(R.id.image_view);
        iklan3 = findViewById(R.id.image_informasi);
        pesan = findViewById(R.id.input_cetak);
        lihat = findViewById(R.id.lihat_cetak);
        informasi = findViewById(R.id.informasi_cetak);
        list_cetak = findViewById(R.id.data_pesan);

        ket = findViewById(R.id.btn_ket);

        ket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Menu.this);
                builder.setTitle("Catatan :");
                builder.setMessage("1. PESAN CETAK : Berisi pemesanan cetak barang yang bisa di pesan oleh pengguna \n " +
                        "2. INFORMASI : Berisi informasi-informasi percetakan \n" +
                        "3. VIEW CETAKAN : Berisi informasi barang dan keterangan harga barang \n" +
                        "4. LIST PESANAN : Berisi barang cetakan yang sudah dipesan");
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

        list_data = new ArrayList<HashMap<String, String>>();


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Data_Profil.class);
                startActivity(intent);
            }
        });

        userData();
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Input_Pesanan.class);
                startActivity(intent);
            }
        });

        informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Informasi.class);
                startActivity(intent);
            }
        });
        list_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, List_data.class);
                startActivity(intent);
            }
        });
       lihat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Menu.this, View_Cetak.class);
               startActivity(intent);
           }
       });
        iklan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Input_Pesanan.class);
                startActivity(intent);
            }
        });
        iklan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, View_Cetak.class);
                startActivity(intent);
            }
        });
        iklan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Informasi.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Menu.this)
                        .setMessage("Apakah Anda Ingin Keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                shared_prefences.Logout();

                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);
        /*String mId = user.get(sessionManager.ID);
        String mNama = user.get(sessionManager.NAMA);
        nama.setText(mNama);*/
        /*Intent intent = getIntent();
        String extraNama = intent.getStringExtra("nama");
        nama.setText(extraNama);*/

    }






    private void userData() {
        String user ="https://percetakanwitra001.000webhostapp.com/User/nama_user.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("nama_pelanggan", object.getString("nama_pelanggan"));
                            map.put("foto", object.getString("foto"));
                            list_data.add(map);
                        }
                            Glide.with(getApplicationContext())
                                    .load("https://percetakanwitra001.000webhostapp.com/User/fhoto_images/"+ list_data.get(0).get("foto") )
                                    .placeholder(R.drawable.user)
                                    .into(photo);

                            nama.setText(list_data.get(0).get("nama_pelanggan"));

                    }else {
                        Toast.makeText(Menu.this, "Gagal Menampilkan" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pelanggan",  getId );


                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);




}





}
