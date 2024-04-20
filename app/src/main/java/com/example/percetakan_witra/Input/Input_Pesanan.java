package com.example.percetakan_witra.Input;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.percetakan_witra.LOGIN.shared_prefences;
import com.example.percetakan_witra.Menu;
import com.example.percetakan_witra.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Input_Pesanan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView kembali;
    int jumlah = 0;
    EditText tgl,  keterangan;
    TextView data_unit;
    Spinner prodk;
    TextView namaU;
    SimpleDateFormat dateFormat;
    ArrayList<String> Data_Produk = new ArrayList<>();
    ArrayList<String> Data_User = new ArrayList<>();

    Context context;

    Button selectimage, kirim,keranjang,ket;
    ImageView gm_produk;
    shared_prefences shared_prefences;
    String getId;
    private JSONArray result;


    Bitmap bitmap;
    String encodedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pesanan);
        context = this;
        tgl = findViewById(R.id.tgl_pesanan);
        prodk = findViewById(R.id.produk);
        namaU = findViewById(R.id.nama_user);
        selectimage = findViewById(R.id.ambiLgambar);
        kirim = findViewById(R.id.pesanan_costumer);
        gm_produk = findViewById(R.id.gmbr_produk);
        data_unit = findViewById(R.id.unit);
        keterangan = findViewById(R.id.ket);

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);



        keranjang = findViewById(R.id.pesanan_keranjang);
        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                simpan_keranjang();
            }
        });

        ket = findViewById(R.id.btn_ket);

        ket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Input_Pesanan.this);
                builder.setTitle("Catatan :");
                builder.setMessage("Pastikan data pesanan diisi dengan benar, agar pihak pengelola mudah memproses pesanan");
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


        data_produk();
        nama_user();


        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Kirim_pesan();
            }
        });

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Input_Pesanan.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent  = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }


                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();


            }
        });




        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });





    }

    private void simpan_keranjang() {
        final String tanggal = tgl.getText().toString().trim();
        final String id_produk = prodk.getSelectedItem().toString();


        String url = "https://percetakanwitra001.000webhostapp.com/keranjang/input_keranjang.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Input_Pesanan.this, "Data  Tersimpan" , Toast.LENGTH_LONG).show();

                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap();
                // jika id kosong maka simpan, jika id ada nilainya maka updat

                params.put("id_pelanggan", getId );
                params.put("id_produk", id_produk);
                params.put("tanggal", tanggal);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);
    }



    private void Kirim_pesan() {


        final String id_pdk = prodk.getSelectedItem().toString();
        final String tanggal = tgl.getText().toString().trim();
        final String unt = data_unit.getText().toString().trim();
        final String keter = keterangan.getText().toString().trim();



        String url = "https://percetakanwitra001.000webhostapp.com/Input/Input_pesanan.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Input_Pesanan.this);
                        builder.setTitle("Sukses Terkirim");
                        builder.setMessage("Pesan Anda Sedang Di Proses, Lihat Pesanan Di List Pesanan Jika Sudah Di Konfirmasi Oleh Admin");
                        builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Input_Pesanan.this, Menu.class));
                                finish();

                            }
                        });
                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap();
                // jika id kosong maka simpan, jika id ada nilainya maka updat

                params.put("id_pelanggan", getId );
                params.put("id_produk",  id_pdk );
                params.put("tanggal", tanggal);
                params.put("unit", unt);
                params.put("gambar_produk", encodedImage);
                params.put("keterangan", keter);



                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);
    }



    private void nama_user() {
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

                             String nm =  object.getString("nama_pelanggan");
                             namaU.setText(nm);


                        }

                    }else {
                        Toast.makeText(Input_Pesanan.this, "Gagal Menampilkan" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Input_Pesanan.this, "Server Error" , Toast.LENGTH_LONG).show();



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

    private void data_produk() {
        String ID_Produk = "https://percetakanwitra001.000webhostapp.com/Input/nama_produk.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ID_Produk,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Hasil");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);


                                String nama = object.getString("id_produk");

                                Data_Produk.add(nama);


                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Data_Produk);
                            prodk.setAdapter(arrayAdapter);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);



}


    private void showDateDialog() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayofMonth);
                tgl.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    public void tambah(View view) {
        if (jumlah == 200) {
            Toast.makeText(this, "pesanan maximal 200", Toast.LENGTH_SHORT).show();
            return;
        }
        jumlah = jumlah + 1;
        display(jumlah);
    }

    public void kurang(View view) {
        if (jumlah == 1) {
            Toast.makeText(this, "pesanan minimal 1", Toast.LENGTH_SHORT).show();
            return;
        }
        jumlah = jumlah - 1;
        display(jumlah);
    }

    private void display(int jumlah) {
        TextView jumlahUnit = (TextView) findViewById(R.id.unit);
        jumlahUnit.setText("" + jumlah);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){

            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                gm_produk.setImageBitmap(bitmap);

                imageStore(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] imageBytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);


    }


    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


    }



    public void onNothingSelected(AdapterView<?> adapterView) {


    }

}
