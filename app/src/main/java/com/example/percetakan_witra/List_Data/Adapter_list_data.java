package com.example.percetakan_witra.List_Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.percetakan_witra.Bukti_Pesanan;
import com.example.percetakan_witra.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Adapter_list_data extends ArrayAdapter<helper_list_data> {

    private Context context;
    LayoutInflater inflater;
    List<helper_list_data> arrayListData;

    public Adapter_list_data(List_data context, List<helper_list_data> arrayListData) {
        super(context, R.layout.data_list, arrayListData);
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

    public void setfilter(ArrayList<helper_list_data> productAll) {
        arrayListData = new ArrayList<helper_list_data>();
        arrayListData.addAll(productAll);
        notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = inflater.inflate(R.layout.data_list, viewGroup, false);
        TextView tgl = view.findViewById(R.id.list_tgl);
        TextView namaProduk = view.findViewById(R.id.list_produk);
        Button Detail_Data = view.findViewById(R.id.list_detail);



        namaProduk.setText(arrayListData.get(position).getNama_produk());
        tgl.setText(arrayListData.get(position).getTanggal());

        Detail_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Bukti_Pesanan.class)
                        .putExtra("position",position);
                context.startActivity(intent);
            }
        });


        return view;

    }
}
