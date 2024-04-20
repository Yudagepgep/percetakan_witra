package com.example.percetakan_witra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.example.percetakan_witra.View.View_Cetak;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Rating extends AppCompatActivity {

    private TextView getRating;
    private Button Submit;
    private RatingBar RatingBar;
    EditText keterangan;

    String getId;
    shared_prefences shared_prefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        getRating = findViewById(R.id.rate);
        Submit = findViewById(R.id.submit);
        RatingBar = findViewById(R.id.penilaian);
        keterangan=findViewById(R.id.ket_fedback);

        shared_prefences = new shared_prefences(this);
        shared_prefences.chekLogin();

        HashMap<String, String> data = shared_prefences.UserData();
        getId = data.get(shared_prefences.ID);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kirim_feedback();
            }
        });

        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float nilai, boolean b) {

                getRating.setText("Rating: "+nilai);
            }
        });
    }

    private void kirim_feedback() {

        final String keter = keterangan.getText().toString().trim();
        final String rate = String.valueOf(RatingBar.getRating());

        String url = "https://percetakanwitra001.000webhostapp.com/Rating/input_rating.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Rating.this);
                        builder.setTitle("Terima Kasih!");
                        builder.setMessage("Anda telah menjadi prioritas kami");
                        builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Rating.this, View_Cetak.class));
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

                params.put("id_pelanggan",getId);
                params.put("jumlah",rate);
                params.put("keterangan", keter);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);

    }
}
