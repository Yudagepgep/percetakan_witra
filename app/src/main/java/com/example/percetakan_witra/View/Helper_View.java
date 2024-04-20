package com.example.percetakan_witra.View;

public class Helper_View {

    private String id_produk;
    private String nama_produk;
    private  String gambar_produk;
    private  String deskripsi;
    private  String harga_jual ;

    public Helper_View(String id_produk, String nama_produk, String gambar_produk, String deskripsi, String harga_jual){
        this.id_produk = id_produk;
        this.nama_produk= nama_produk;
        this.gambar_produk = gambar_produk;
        this.deskripsi = deskripsi;
        this.harga_jual= harga_jual;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga_jual() {
        return harga_jual;
    }
}
