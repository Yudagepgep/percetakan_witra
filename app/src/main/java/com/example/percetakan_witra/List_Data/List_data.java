package com.example.percetakan_witra.List_Data;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.Keranjang.keranjang;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.R;
import com.example.percetakan_witra.Rating;
import com.example.percetakan_witra.View.Helper_View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List_data extends AppCompatActivity {

    public static Context context;
    public static ArrayList<helper_list_data> arrayListData;
    static ListView listView;
    com.example.percetakan_witra.LOGIN.shared_prefences shared_prefences;
    private static Adapter_list_data adapter;
    private static ProgressDialog mProgressDialog;
    String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        context = this;
        listView = findViewById(R.id.list);
        arrayListData = new ArrayList<>();
        adapter = new Adapter_list_data(List_data.this, arrayListData);
        Tampil_View();

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);
    }







    private void Tampil_View() {

        String user ="https://percetakanwitra001.000webhostapp.com/Data_Konfirmasi/Tampil_Data.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("Hasil");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayListData.add(new helper_list_data(
                                    object.getString("id_konfirmasi"),
                                    object.getString("id_pesan"),
                                    object.getString("nama_pelanggan"),
                                    object.getString("nama_produk"),
                                    object.getString("tanggal"),
                                    object.getString("unit"),
                                    object.getString("gambar_produk"),
                                    object.getString("total_harga"),
                                    object.getString("keterangan")




                            ));
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<helper_list_data> filterdataList = filter(arrayListData, s);
                adapter.setfilter((ArrayList<helper_list_data>) filterdataList);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_keranjang) {
            startActivity(new Intent(this, keranjang.class));

        } else if (item.getItemId() == R.id.rating) {
            startActivity(new Intent(this, Rating.class));

        }else if (item.getItemId() == R.id.logout) {
            new AlertDialog.Builder(this)
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
        return true;


    }


    private List<helper_list_data> filter(List<helper_list_data> hi, String s) {
        s = s.toLowerCase();
        final List<helper_list_data> filterdataList = new ArrayList<>();
        for (helper_list_data data : hi) {
            final String text = data.getNama_produk().toLowerCase();
            if (text.startsWith(s)) {
                filterdataList.add(data);
            }
        }
        return filterdataList;

    }

}
