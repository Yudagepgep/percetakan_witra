package com.example.percetakan_witra.Keranjang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.R;
import com.example.percetakan_witra.Rating;
import com.example.percetakan_witra.View.Helper_View;
import com.example.percetakan_witra.View.View_Cetak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class keranjang extends AppCompatActivity {


    public static ArrayList<helper_keranjang> arrayListData ;
    static ListView listView;
    private static Adapter_keranjang adapter;
    private static ProgressDialog mProgressDialog;
      String getId;
    shared_prefences shared_prefences;
    private static Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);




        context=this;
        listView = findViewById(R.id.list);
        arrayListData = new ArrayList<>();
        adapter = new Adapter_keranjang(keranjang.this, arrayListData);
        Tampil_View();
        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);


    }

    public void Tampil_View() {

        String user ="https://percetakanwitra001.000webhostapp.com/keranjang/Tampil_Keranjang.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Hasil");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            arrayListData.add(new helper_keranjang(
                                    object.getString("id_keranjang"),
                                    object.getString("tanggal"),
                                    object.getString("nama_produk")






                            ));
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();




                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pelanggan",   getId );


                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);





    }
    public static void deleteData(final String id_keranjang) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://percetakanwitra001.000webhostapp.com/keranjang/hapus_keranjang.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Terhapus")) {
                            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
                            builder.setTitle("Sukses");
                            builder.setMessage("Data Sukses Terhapus");
                            builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    context.startActivity(new Intent(String.valueOf(context)));


                                }
                            });
                            androidx.appcompat.app.AlertDialog dialog=builder.create();
                            dialog.show();






                    } else {
                            Toast.makeText(context, "Data Gagal DiHapus", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("id_keranjang", id_keranjang);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);


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
                final List<helper_keranjang> filterdataList = filter(arrayListData, s);
                adapter.setfilter((ArrayList<helper_keranjang>) filterdataList);
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


    private List<helper_keranjang> filter(List<helper_keranjang> hi, String s) {
        s = s.toLowerCase();
        final List<helper_keranjang> filterdataList = new ArrayList<>();
        for (helper_keranjang data : hi) {
            final String text = data.getNama_produk().toLowerCase();
            if (text.startsWith(s)) {
                filterdataList.add(data);
            }
        }
        return filterdataList;

    }
}
