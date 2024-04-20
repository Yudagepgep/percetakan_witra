package com.example.percetakan_witra.Keranjang;

public class helper_keranjang {

    private String id_keranjang;
    private String tgl_keranjang;
    private String nama_produk ;

    public helper_keranjang (String id_keranjang,String tgl_keranjang,String nama_produk){
        this.id_keranjang=id_keranjang;
        this.tgl_keranjang= tgl_keranjang;
        this.nama_produk=nama_produk;
    }

    public String getId_keranjang() {
        return id_keranjang;
    }

    public String getTgl_keranjang() {
        return tgl_keranjang;
    }

    public String getNama_produk() {
        return nama_produk;
    }
}
