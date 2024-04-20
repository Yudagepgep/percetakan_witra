package com.example.percetakan_witra.Keranjang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.percetakan_witra.Input.Input_Pesanan;
import com.example.percetakan_witra.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Adapter_keranjang extends ArrayAdapter<helper_keranjang> {
    private Context context;
    LayoutInflater inflater;
    List<helper_keranjang> arrayListData;


    public Adapter_keranjang(keranjang context, List<helper_keranjang> arrayListData) {
        super(context, R.layout.keranjang_list, arrayListData);

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.arrayListData = arrayListData;
    }

    @Override
    public int getCount() {
        return arrayListData.size();
    }


    @Override
    public long getItemId(int position) {
        return (position);
    }

    public void setfilter(ArrayList<helper_keranjang> productAll) {
        arrayListData = new ArrayList<helper_keranjang>();
        arrayListData.addAll(productAll);
        notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Button hapus_k,pesan_k;

        View view = inflater.inflate(R.layout.keranjang_list, viewGroup, false);
        TextView tgl= view.findViewById(R.id.keranjang_tgl);
        TextView namaProduk = view.findViewById(R.id.keranjang_produk);
        hapus_k=view.findViewById(R.id.hapus_keranjang);
        pesan_k=view.findViewById(R.id.list_pesan);


        namaProduk.setText(arrayListData.get(position).getNama_produk());
        tgl.setText(arrayListData.get(position).getTgl_keranjang());

        pesan_k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Input_Pesanan.class);
                context.startActivity(intent);
            }
        });
        hapus_k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keranjang.deleteData(arrayListData.get(position).getId_keranjang());

            }
        });



        return view;

    }
}
