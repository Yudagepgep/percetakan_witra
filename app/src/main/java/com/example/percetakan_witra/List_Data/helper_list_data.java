package com.example.percetakan_witra.List_Data;

public class helper_list_data {
    private String id_konfirmasi;
    private String id_pesan;
    private String nama_pelanggan;
    private String nama_produk;
    private String tanggal;
    private String unit;
    private  String gambar_produk;
    private  String total_harga;
    private  String keterangan ;

    public helper_list_data(String id_konfirmasi,String id_pesan,String nama_pelanggan,String nama_produk,
                            String tanggal,String unit,String gambar_produk,String total_harga,String keterangan){


        this.id_konfirmasi=id_konfirmasi;
        this.id_pesan=id_pesan;
        this.nama_pelanggan= nama_pelanggan;
        this.nama_produk=nama_produk;
        this.tanggal=tanggal;
        this.unit=unit;
        this.gambar_produk=gambar_produk;
        this.total_harga=total_harga;
        this.keterangan=keterangan;
    }


    public String getId_konfirmasi() {
        return id_konfirmasi;
    }

    public String getId_pesan() {
        return id_pesan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getUnit() {
        return unit;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
