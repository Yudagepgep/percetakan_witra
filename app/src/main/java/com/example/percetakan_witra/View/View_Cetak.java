package com.example.percetakan_witra.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.Keranjang.keranjang;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.R;
import com.example.percetakan_witra.Rating;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class View_Cetak extends AppCompatActivity {

    public static Context context;
    public static ArrayList<Helper_View> arrayListProduk ;
    static ListView listView;

    private static Adapter_View adapter;
    private static ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__cetak);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        context=this;
        listView = findViewById(R.id.list_antrian);
        arrayListProduk = new ArrayList<>();
        adapter = new Adapter_View(View_Cetak.this, arrayListProduk);
        Tampil_View();
    }

    private void Tampil_View() {
        mProgressDialog = new ProgressDialog( context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();
        String URL_VIEW = "https://percetakanwitra001.000webhostapp.com/Produk/Tampil_Produk.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_VIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        arrayListProduk.clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject view = jsonArray.getJSONObject(i);
                                arrayListProduk.add(new Helper_View(
                                        view.getString("id_produk"),
                                        view.getString("nama_produk"),
                                        view.getString("gambar_produk"),
                                        view.getString("deskripsi"),
                                        view.getString("harga_jual")



                                ));


                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                mProgressDialog.dismiss();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar2, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<Helper_View> filterdataList = filter(arrayListProduk, s);
                adapter.setfilter((ArrayList<Helper_View>) filterdataList);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_keranjang) {
            startActivity(new Intent(this, keranjang.class));


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


    private List<Helper_View> filter(List<Helper_View> hi, String s) {
        s = s.toLowerCase();
        final List<Helper_View> filterdataList = new ArrayList<>();
        for (Helper_View data : hi) {
            final String text = data.getNama_produk().toLowerCase();
            if (text.startsWith(s)) {
                filterdataList.add(data);
            }
        }
        return filterdataList;

    }
    }

