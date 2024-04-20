package com.example.percetakan_witra;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.List_Data.List_data;
import com.example.percetakan_witra.View.View_Cetak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bukti_Pesanan extends AppCompatActivity {
    TextView faktur,n_pelanggan,n_produk,jm_unit,keter,total,tgl;
    ImageView g_produk;
    private int position;
    Button hapus_pesan;
    shared_prefences shared_prefences;
    String getId;
    ArrayList<HashMap<String, String>> list_data;

    CircleImageView fhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti__pesanan);

        faktur=findViewById(R.id.no_faktur);
        n_pelanggan=findViewById(R.id.nm_pelanggan);
        n_produk=findViewById(R.id.nm_produk);
        jm_unit=findViewById(R.id.jml_pesanan);
        keter=findViewById(R.id.ket_pesanan);
        total=findViewById(R.id.tot_pesanan);
        tgl=findViewById(R.id.tg_pesanan);
        g_produk=findViewById(R.id.gmr_produk);



        fhoto=findViewById(R.id.foto_profil);

        hapus_pesan=findViewById(R.id.hapus_pesan);
        hapus_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hapus(List_data.arrayListData.get(position).getId_konfirmasi());
            }
        });

        list_data = new ArrayList<HashMap<String, String>>();

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);

        data_foto();

        Intent intent =getIntent();
        position = intent.getExtras().getInt("position");

        tgl.setText(List_data.arrayListData.get(position).getTanggal());
        faktur.setText(List_data.arrayListData.get(position).getId_pesan());
        n_pelanggan.setText(List_data.arrayListData.get(position).getNama_pelanggan());
        jm_unit.setText(List_data.arrayListData.get(position).getUnit());
        keter.setText(List_data.arrayListData.get(position).getKeterangan());
        total.setText(List_data.arrayListData.get(position).getTotal_harga());
        n_produk.setText(List_data.arrayListData.get(position).getNama_produk());
        Glide.with(Bukti_Pesanan.this)
                .load("https://percetakanwitra001.000webhostapp.com/Input/images/" + List_data.arrayListData.get(position).getGambar_produk())
                .into(g_produk);
    }

    private void data_foto() {

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
                            map.put("foto", object.getString("foto"));
                            list_data.add(map);
                        }
                        Glide.with(getApplicationContext())
                                .load("https://percetakanwitra001.000webhostapp.com/User/fhoto_images/"+ list_data.get(0).get("foto") )
                                .placeholder(R.drawable.user)
                                .into(fhoto);


                    }else {
                        Toast.makeText(Bukti_Pesanan.this, "Gagal Menampilkan" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Bukti_Pesanan.this, "Login" , Toast.LENGTH_LONG).show();



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

    private void hapus(final String id_konfirmasi) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://percetakanwitra001.000webhostapp.com/Data_Konfirmasi/hapus_data.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Terhapus")) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Bukti_Pesanan.this);
                            builder.setTitle("Sukses");
                            builder.setMessage("Data Berhasil DiHapus");
                            builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Bukti_Pesanan.this, List_data.class));
                                    finish();

                                }
                            });
                            androidx.appcompat.app.AlertDialog dialog = builder.create();
                            dialog.show();


                        } else {
                            Toast.makeText(Bukti_Pesanan.this, "Data Gagal DiHapus", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Bukti_Pesanan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("id_konfirmasi", id_konfirmasi);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
