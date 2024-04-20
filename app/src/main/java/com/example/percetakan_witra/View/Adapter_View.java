package com.example.percetakan_witra.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class Adapter_View extends ArrayAdapter<Helper_View> {


    private Context context;
    LayoutInflater inflater;
    List<Helper_View> arrayListProduk;
    public Adapter_View(View_Cetak  context,  List<Helper_View> arrayListProduk) {
        super(context, R.layout.activt_view_list, arrayListProduk);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.arrayListProduk =  arrayListProduk;
    }

    @Override
    public int getCount() {
        return arrayListProduk.size();
    }




    @Override
    public long getItemId(int position) {
        return (position);
    }
    public void setfilter(ArrayList<Helper_View> productAll){
        arrayListProduk=new ArrayList<Helper_View>();
        arrayListProduk.addAll(productAll);
        notifyDataSetChanged();

    }


    @Override
    public View getView(int position,  View convertView, ViewGroup viewGroup) {
        Button pesan;

        View view = inflater.inflate(R.layout.activt_view_list, viewGroup, false);
        TextView namaProduk = view.findViewById(R.id.view_nama_produk);
        TextView Deskripsi = view.findViewById(R.id.view_deskripsi);
        TextView Harga = view.findViewById(R.id.view_harga);
        ImageView gambarProduk = view.findViewById(R.id.view_gambar_produk);
        pesan = view.findViewById(R.id.pesan_sekarang);


        namaProduk.setText(arrayListProduk.get(position).getNama_produk());
        Deskripsi.setText(arrayListProduk.get(position).getDeskripsi());
        Harga.setText(arrayListProduk.get(position).getHarga_jual());
        Glide.with(context)
                .load("https://percetakanwitra001.000webhostapp.com/Web2/admin/prog_file_gambar_produk/" + arrayListProduk.get(position).getGambar_produk())
                .into(gambarProduk);

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Input_Pesanan.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
