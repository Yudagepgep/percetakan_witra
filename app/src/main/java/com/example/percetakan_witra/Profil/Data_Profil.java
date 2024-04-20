package com.example.percetakan_witra.Profil;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.LOGIN.Login;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Data_Profil extends AppCompatActivity {
    EditText ed_name,ed_email,ed_kode,ed_hp,ed_alamat;
    Button ed_fhoto,ed_data;

    CircleImageView ed_fhoto_profil;
    Bitmap bitmap;
    String encodedImage;
    String getId;
    shared_prefences shared_prefences;

    ArrayList<HashMap<String, String>> list_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_profil);
        ed_name = findViewById(R.id.input_name);
        ed_email = findViewById(R.id.input_email);
        ed_kode = findViewById(R.id.input_kode_pos);
        ed_hp = findViewById(R.id.input_hp);
        ed_alamat = findViewById(R.id.input_alamat);

        ed_fhoto = findViewById(R.id.edit_photo);
        ed_data = findViewById(R.id.btn_Edit);
        ed_fhoto_profil = findViewById(R.id.edit_photo_profil);

        list_data = new ArrayList<HashMap<String, String>>();

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);

        Detail_data();

        ed_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_Data_Profil();
            }
        });

        ed_fhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_fhoto();
            }
        });

        ed_fhoto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Data_Profil.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

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
    }

    private void Edit_Data_Profil() {
        final  String nama = ed_name.getText().toString().trim();
        final  String email = ed_email.getText().toString().trim();
        final  String pos = ed_kode.getText().toString().trim();
        final  String hp = ed_hp.getText().toString().trim();
        final  String amt = ed_alamat.getText().toString().trim();

        String url = "https://percetakanwitra001.000webhostapp.com/User/Edit_profile.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            if (success.equals("1")) {

                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Data_Profil.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Data Sukses Update");
                                builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(Data_Profil.this, Menu.class));
                                        finish();

                                    }
                                });
                                androidx.appcompat.app.AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                Toast.makeText(Data_Profil.this, "Gagal; Update", Toast.LENGTH_LONG).show();

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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap();
                // jika id kosong maka simpan, jika id ada nilainya maka updat


                params.put("id_pelanggan", getId);
                params.put("nama_pelanggan",nama);
                params.put("email",email);
                params.put("kode_pos",pos);
                params.put("no_hp",hp);
                params.put("alamat",amt);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);
    }

    private void Detail_data() {
        String user ="https://percetakanwitra001.000webhostapp.com/User/Lihat_Data.php";
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
                            map.put("email", object.getString("email"));
                            map.put("kode_pos", object.getString("kode_pos"));
                            map.put("no_hp", object.getString("no_hp"));
                            map.put("alamat", object.getString("alamat"));
                            map.put("foto", object.getString("foto"));
                            list_data.add(map);

                        }
                        Glide.with(getApplicationContext())
                                .load("https://percetakanwitra001.000webhostapp.com/User/fhoto_images/"+ list_data.get(0).get("foto") )
                                .placeholder(R.drawable.user)
                                .into(ed_fhoto_profil);

                            ed_name.setText(list_data.get(0).get("nama_pelanggan"));
                            ed_email.setText(list_data.get(0).get("email"));;
                            ed_kode.setText(list_data.get(0).get("kode_pos"));;
                            ed_hp.setText(list_data.get(0).get("no_hp"));;
                            ed_alamat.setText(list_data.get(0).get("alamat"));;




                    }else {
                        Toast.makeText(Data_Profil.this, "Gagal Menampilkan Data" , Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Data_Profil.this, " Jaringan Tidak Ada \n Periksa Kembali Jaringan Anda" , Toast.LENGTH_LONG).show();


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


    private void edit_fhoto() {
        String url = "https://percetakanwitra001.000webhostapp.com/User/Upload_Fhoto.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            if (success.equals("1")) {

                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Data_Profil.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Data Sukses Update");
                                builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(Data_Profil.this, Menu.class));
                                        finish();

                                    }
                                });
                                androidx.appcompat.app.AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                Toast.makeText(Data_Profil.this, "Gagal; Upload", Toast.LENGTH_LONG).show();

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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap();
                // jika id kosong maka simpan, jika id ada nilainya maka updat


                params.put("id_pelanggan",getId);
                params.put("foto", encodedImage);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);
    }


    @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            if(requestCode == 1 && resultCode == RESULT_OK && data!=null){

                Uri filePath = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    ed_fhoto_profil.setImageBitmap(bitmap);

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
}
